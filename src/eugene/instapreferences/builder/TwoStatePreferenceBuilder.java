package eugene.instapreferences.builder;

import java.lang.reflect.Field;

import eugene.instapreferences.InstaPreferenceUtils;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.TwoStatePreference;

public abstract class TwoStatePreferenceBuilder implements PreferenceBuilder<Boolean> {

	@Override
	public TwoStatePreference build(Context context, Boolean defaultValue, Field field) {
		// Edit text preference
		TwoStatePreference preference = createPreference(context);
        
		InstaPreferenceUtils.fillPreferenceEssentials(field, preference);
        preference.setChecked(Boolean.TRUE.equals(defaultValue));
        return preference;
	}

	protected abstract TwoStatePreference createPreference(Context context);
}
