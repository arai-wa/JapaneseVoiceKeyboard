package com.example.input.voice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import com.example.input.voice.util.Log;

public class VoiceKeyboardService extends InputMethodService {

	private VoiceKeyboard mMyKeyboard;
	private View inputView;
	private SpeechRecognizer speechRecognizer;

	@Override
	public void onInitializeInterface() {
		super.onInitializeInterface();
		mMyKeyboard = new VoiceKeyboard(this, R.layout.keyboard);
	}


	@Override
	public View onCreateInputView() {
		inputView = getLayoutInflater().inflate(R.layout.input, null);

		int permission = checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO);
		boolean permitted = permission == PackageManager.PERMISSION_GRANTED;
		Log.d("RECORD_AUDIO permission is: " + String.valueOf(permitted));

		if (!permitted) {
			Log.d("requesting RECORD_AUDIO permission");
			Intent dialogIntent = new Intent(VoiceKeyboardService.this, RequestPermissionActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(dialogIntent);
		}

		return inputView;
	}

	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		super.onStartInputView(info, restarting);
		ListView messages = inputView.findViewById(R.id.messages);
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		VoiceKeyboardActionListener listener = new VoiceKeyboardActionListener(this, messages, speechRecognizer);
		speechRecognizer.setRecognitionListener(listener);

		VoiceKeyboardView keyboard = inputView.findViewById(R.id.keyboard);
		keyboard.setKeyboard(mMyKeyboard);
		keyboard.setOnKeyboardActionListener(listener);
	}

	@Override
	public void onFinishInputView(boolean finishingInput) {
		super.onFinishInputView(finishingInput);
		speechRecognizer.destroy();
	}
}