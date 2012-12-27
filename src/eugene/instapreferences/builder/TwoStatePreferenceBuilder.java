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
        
        String preferenceName = InstaPreferenceUtils.getPreferenceNameFromFieldName(field);
        
        preference.setKey(InstaPreferenceUtils.getSettingsNameFromFieldName(field.getName()));
        preference.setTitle(preferenceName);
        preference.setChecked(Boolean.TRUE.equals(defaultValue));
//        editTextPref.setSummary(preferenceName);	//TODO Summary annotation
        return preference;
	}
	
	protected abstract TwoStatePreference createPreference(Context context);
}
