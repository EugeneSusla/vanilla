package eugene.instapreferences.builder;

import java.lang.reflect.Field;

import eugene.instapreferences.InstaPreferenceUtils;
import eugene.utils.StringUtils;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;

public class TextPreferenceBuilder implements PreferenceBuilder<String> {

	@Override
	public Preference build(Context context, String defaultValue, Field field) {
        EditTextPreference preference = new EditTextPreference(context);
        InstaPreferenceUtils.fillPreferenceEssentials(field, preference);
        preference.setText(defaultValue);
        return preference;
	}

	
}
