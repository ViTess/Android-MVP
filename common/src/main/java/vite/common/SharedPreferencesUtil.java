package vite.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.Objects;
import java.util.Set;

/**
 * Created by trs on 17-6-8.
 */

public final class SharedPreferencesUtil {
    private static final String Preferences_NAME = null;

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Preferences_NAME, Context.MODE_PRIVATE);
    }

    public static void set(Context context, String key, Object value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        if (value == null)
            editor.putString(key, null);
        else if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof Float)
            editor.putFloat(key, (Float) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else
            editor.putString(key, value.toString());

        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static void set(Context context, String key, Set<String> value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putStringSet(key, value);

        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static Object get(Context context, String key, Object defaultValue) {
        SharedPreferences sp = getSharedPreferences(context);

        if (defaultValue == null)
            return sp.getString(key, null);
        if (defaultValue instanceof String)
            return sp.getString(key, (String) defaultValue);
        else if (defaultValue instanceof Integer)
            return sp.getInt(key, (Integer) defaultValue);
        else if (defaultValue instanceof Boolean)
            return sp.getBoolean(key, (Boolean) defaultValue);
        else if (defaultValue instanceof Float)
            return sp.getFloat(key, (Float) defaultValue);
        else if (defaultValue instanceof Long)
            return sp.getLong(key, (Long) defaultValue);

        return null;
    }

    public static Set<String> get(Context context, String key, Set<String> defaultValue) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getStringSet(key, defaultValue);
    }
}
