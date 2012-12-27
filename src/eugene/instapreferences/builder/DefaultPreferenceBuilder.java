package eugene.instapreferences.builder;

import java.lang.reflect.Field;

import android.content.Context;
import android.preference.Preference;

public class DefaultPreferenceBuilder implements PreferenceBuilder<Object> {

	private TextPreferenceBuilder textPreferenceBuilder = new TextPreferenceBuilder();
	
	@Override
	public Preference build(Context context, Object defaultValue, Field field) {
		Preference preference = textPreferenceBuilder.build(context, "" + defaultValue, field);
		return preference;
	}
}
