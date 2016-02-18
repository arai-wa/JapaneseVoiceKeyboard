package com.example.input.voice.util;

public class Log {
	public static void d(String message) {
		android.util.Log.d(tag(), message);
	}

	public static void e(String message) {
		android.util.Log.e(tag(), message);
	}

	private static String tag() {
		StackTraceElement caller = new Throwable().getStackTrace()[2];
		return caller.getClassName() + "#" + caller.getMethodName() + "(" + caller.getLineNumber() + ")";
	}
}
