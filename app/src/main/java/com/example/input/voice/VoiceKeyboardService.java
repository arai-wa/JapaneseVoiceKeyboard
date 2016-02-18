package com.example.input.voice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import com.example.input.voice.util.Log;

public class VoiceKeyboardService extends InputMethodService {

	private VoiceKeyboard mMyKeyboard;
	private int mLastDisplayWidth;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onInitializeInterface() {
		super.onInitializeInterface();
		if (mMyKeyboard != null) {
			int displayWidth = getMaxWidth();
			if (displayWidth == mLastDisplayWidth) return;
			mLastDisplayWidth = displayWidth;
		}
		mMyKeyboard = new VoiceKeyboard(this, R.layout.keyboard);
	}

	@Override
	public View onCreateInputView() {
		View inputView = getLayoutInflater().inflate(R.layout.input, null);

		ListView voices = (ListView) inputView.findViewById(R.id.voices);
		VoiceKeyboardView keyboard = (VoiceKeyboardView) inputView.findViewById(R.id.keyboard);
		keyboard.setKeyboard(mMyKeyboard);
		keyboard.setOnKeyboardActionListener(new VoiceKeyboardActionListener(this, voices));

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd,
	                              int candidatesStart, int candidatesEnd) {
		super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
				candidatesStart, candidatesEnd);
	}
}