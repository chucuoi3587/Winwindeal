package vn.winwindeal.android.app.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {

	public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
	public static final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
	public static final String ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
	public static final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
	public static final String ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";

	public static Typeface getFontAssets(Context context, String fontName) {
		return Typeface.createFromAsset(context.getAssets(), fontName);
	}
}
