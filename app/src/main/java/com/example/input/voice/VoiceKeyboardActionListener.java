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
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.input.voice.util.Log;

import java.util.ArrayList;

public class VoiceKeyboardActionListener implements KeyboardView.OnKeyboardActionListener, RecognitionListener {

	private final InputMethodService SERVICE;


	private final SpeechRecognizer SPEECH_RECOGNIZER;
	private final ListView VOICES;

	protected VoiceKeyboardActionListener(InputMethodService service, ListView voices) {
		SPEECH_RECOGNIZER = SpeechRecognizer.createSpeechRecognizer(service.getApplicationContext());
		SPEECH_RECOGNIZER.setRecognitionListener(this);
		this.SERVICE = service;
		this.VOICES = voices;
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
		Log.d("ready");
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.d("beginning of speech");
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
		Log.d("end of speech");
	}

	@Override
	public void onError(int error) {
		//FIXME handle errors
		switch (error) {
			case SpeechRecognizer.ERROR_AUDIO:
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				Log.e("network error");
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				Log.e("network timeout");
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				break;
			case SpeechRecognizer.ERROR_SERVER:
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				Toast.makeText(SERVICE, "speech timeout", Toast.LENGTH_LONG).show();
				Log.e("speech timeout");
				break;
			default:
		}
	}

	@Override
	public void onResults(Bundle results) {
		final ArrayList<String> voices = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

		for (String voice : voices) {
			Log.d(voice);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(SERVICE, R.layout.voice, voices);
		VOICES.setAdapter(adapter);
		VOICES.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				SERVICE.getCurrentInputConnection().commitText(voices.get(i), 1);
				VOICES.setAdapter(new ArrayAdapter<>(SERVICE, R.layout.voice, new ArrayList<String>()));
			}
		});
	}

	@Override
	public void onPartialResults(Bundle bundle) {
	}

	@Override
	public void onEvent(int i, Bundle bundle) {
	}


}
