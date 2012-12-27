package eugene.instapreferences.builder;

import java.lang.reflect.Field;

import android.content.Context;
import android.preference.Preference;

public interface PreferenceBuilder<T> {
	public Preference build(Context context, T defaultValue, Field field);
}
