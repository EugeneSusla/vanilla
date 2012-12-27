package eugene.instapreferences.builder;

import java.lang.reflect.Field;

import android.content.Context;
import android.preference.Preference;

public class RootPreferenceBuilder implements PreferenceBuilder<Object> {

	private DefaultPreferenceBuilder defaultPreferenceBuilder = new DefaultPreferenceBuilder();
	private TwoStatePreferenceBuilder twoStatePreferenceBuilder = new TwoStatePreferenceBuilderImpl();
	
	@Override
	public Preference build(Context context, Object defaultValue, Field field) {
		if (field.getType() == boolean.class) {
			return twoStatePreferenceBuilder.build(context, (Boolean) defaultValue, field);
		} else {
			return defaultPreferenceBuilder.build(context, defaultValue, field);
		}
	}

}
