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
		// Edit text preference
        EditTextPreference editTextPref = new EditTextPreference(context);
        
        String preferenceName = InstaPreferenceUtils.getPreferenceNameFromFieldName(field);
        
        editTextPref.setDialogTitle(preferenceName);
        editTextPref.setKey(InstaPreferenceUtils.getSettingsNameFromFieldName(field.getName()));
        editTextPref.setTitle(preferenceName);
        editTextPref.setText(defaultValue);
//        editTextPref.setSummary(preferenceName);	//TODO Summary annotation
        return editTextPref;
	}

	
}
