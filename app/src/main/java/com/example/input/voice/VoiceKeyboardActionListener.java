package com.example.input.voice;

import android.content.Intent;
import android.content.res.Resources;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.input.voice.util.Log;

import java.util.ArrayList;

//FIXME このクラスでハードコーディングしている文字列が多くなってきたらstrings.xmlに定義する
public class VoiceKeyboardActionListener implements KeyboardView.OnKeyboardActionListener, RecognitionListener {

	private final InputMethodService SERVICE;


	private final SpeechRecognizer SPEECH_RECOGNIZER;
	private final ListView MESSAGES;

	protected VoiceKeyboardActionListener(InputMethodService service, ListView messages) {
		SPEECH_RECOGNIZER = SpeechRecognizer.createSpeechRecognizer(service.getApplicationContext());
		SPEECH_RECOGNIZER.setRecognitionListener(this);
		this.SERVICE = service;
		this.MESSAGES = messages;
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		Resources resources = SERVICE.getResources();
		InputConnection currentInputConnection = SERVICE.getCurrentInputConnection();

		if (primaryCode == resources.getInteger(R.integer.voice)) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			SPEECH_RECOGNIZER.startListening(intent);

		} else if (primaryCode == resources.getInteger(R.integer.delete)) {
			keyDownUp(KeyEvent.KEYCODE_DEL);

		} else if (primaryCode == resources.getInteger(R.integer.toutenn)) {
			currentInputConnection.commitText("、", 1);

		} else if (primaryCode == resources.getInteger(R.integer.kutenn)) {
			currentInputConnection.commitText("。", 1);

		} else if (primaryCode == resources.getInteger(R.integer.kaigyou)) {
			keyDownUp(KeyEvent.KEYCODE_ENTER);

		} else if (primaryCode == resources.getInteger(R.integer.tab)) {
			currentInputConnection.commitText("\t", 1);

		} else if (primaryCode == resources.getInteger(R.integer.kakko)) {
			currentInputConnection.commitText("「", 1);

		} else if (primaryCode == resources.getInteger(R.integer.kakkotozi)) {
			currentInputConnection.commitText("」", 1);

		} else if (primaryCode == resources.getInteger(R.integer.tenntenntenn)) {
			currentInputConnection.commitText("……", 1);

		} else if (primaryCode == resources.getInteger(R.integer.space)) {
			currentInputConnection.commitText("　", 1);

		} else if (primaryCode == resources.getInteger(R.integer.maru_kakko)) {
			currentInputConnection.commitText("（", 1);

		} else if (primaryCode == resources.getInteger(R.integer.maru_kakkotozi)) {
			currentInputConnection.commitText("）", 1);

		} else if (primaryCode == resources.getInteger(R.integer.bikkuri)) {
			currentInputConnection.commitText("！", 1);

		} else if (primaryCode == resources.getInteger(R.integer.hatena)) {
			currentInputConnection.commitText("？", 1);

		} else {
			Toast.makeText(SERVICE, "invalid primary code: " + primaryCode, Toast.LENGTH_SHORT).show();
		}
	}

	private void keyDownUp(int keyEventCode) {
		InputConnection currentInputConnection = SERVICE.getCurrentInputConnection();
		currentInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
		currentInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
	}

	@Override
	public void onPress(int primaryCode) {
	}

	@Override
	public void onRelease(int primaryCode) {
	}

	@Override
	public void onText(CharSequence text) {
	}

	@Override
	public void swipeDown() {
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeUp() {
	}

	@Override
	public void onReadyForSpeech(Bundle bundle) {
		String[] a = {"音声入力してください"};
		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.message_element, a);
		MESSAGES.setAdapter(adapter);
	}

	@Override
	public void onBeginningOfSpeech() {
		String[] a = {"音声入力中"};
		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.message_element, a);
		MESSAGES.setAdapter(adapter);
	}

	@Override
	public void onRmsChanged(float v) {
	}

	@Override
	public void onBufferReceived(byte[] bytes) {
		Log.d(new String(bytes));
	}

	@Override
	public void onEndOfSpeech() {
		String[] a = {"変換中"};
		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.message_element, a);
		MESSAGES.setAdapter(adapter);
	}

	@Override
	public void onError(int error) {
		ArrayList<String> errorMessages = new ArrayList<>();
		errorMessages.add("エラーが発生しました。");
		errorMessages.add("code:" + error);

		switch (error) {
			case SpeechRecognizer.ERROR_AUDIO:
				errorMessages.add("音声の録音に失敗しました");
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				errorMessages.add("その他のアプリのエラーです");
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				errorMessages.add("権限が不足しています");
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				errorMessages.add("ネットワーク関連のエラーです");
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				errorMessages.add("通信がタイムアウトしました");
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				errorMessages.add("音声にマッチする変換結果を見つけられませんでした");
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				errorMessages.add("音声認識がビジーです");
				break;
			case SpeechRecognizer.ERROR_SERVER:
				errorMessages.add("Googleのサーバ側のエラーです");
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				errorMessages.add("音声入力がありませんでした");
				break;
			default:
				errorMessages.add("分類不能なエラーです");
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.error_element, errorMessages);
		MESSAGES.setAdapter(adapter);
	}

	@Override
	public void onResults(Bundle results) {
		final ArrayList<String> voices = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

		voices.forEach(Log::d);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.list_element, voices);
		MESSAGES.setAdapter(adapter);
		MESSAGES.setOnItemClickListener((adapterView, view, i, l) -> {
			SERVICE.getCurrentInputConnection().commitText(voices.get(i), 1);
			MESSAGES.setAdapter(new ArrayAdapter<>(SERVICE, R.layout.list_element, new ArrayList<String>()));
		});
	}

	@Override
	public void onPartialResults(Bundle bundle) {
	}

	@Override
	public void onEvent(int i, Bundle bundle) {
	}


}
