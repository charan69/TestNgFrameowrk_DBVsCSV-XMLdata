package com.mdsol.ctms.utils;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CommonUtils {
	static String newString = null;

	public static String replaceDoubleBraces(String bracedstring) {
		try {
			String replaceOpenBrace = bracedstring.replace("[", "");
			String replaceCloseBrace = replaceOpenBrace.replace("]", "");
			newString = replaceCloseBrace;
			return newString;
		} catch (Exception e) {
			throw new UncheckedIOException((IOException) e);
		}

	}

	public static String replaceAllRegexToNull(String regexstring, String ch) {
		try {
			String replacedRegexString = regexstring.replace(ch, "");
			newString = replacedRegexString;
			return newString;
		} catch (Exception e) {
			throw new UncheckedIOException((IOException) e);
		}

	}

}
