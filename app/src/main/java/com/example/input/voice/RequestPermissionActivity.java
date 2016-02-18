package com.example.input.voice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.example.input.voice.util.Log;

public class RequestPermissionActivity extends PreferenceActivity {
	private static final int CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case CODE: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("granted");

				} else {
					Log.d("denied");
				}

				finish();
			}
		}
	}
}
