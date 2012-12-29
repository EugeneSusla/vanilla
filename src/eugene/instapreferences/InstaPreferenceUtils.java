package eugene.instapreferences;

import java.lang.reflect.Field;

import android.preference.DialogPreference;
import android.preference.Preference;
import eugene.utils.StringUtils;

public class InstaPreferenceUtils {
	private InstaPreferenceUtils() {}
	
	public static String getPreferenceNameFromFieldName(Field field) {
		String preferenceName = StringUtils.camelCaseToPlainString(field.getName());
		StringBuilder sb = new StringBuilder(preferenceName);
		sb.setCharAt(0, Character.toUpperCase(preferenceName.charAt(0)));
		return sb.toString();
	}

	public static String getSettingsNameFromFieldName(String fieldName) {
		return StringUtils.camelCaseToUnderscoreLowerCase(fieldName);
	}
	
	public static void fillPreferenceEssentials(Field field, Preference preference) {
		String preferenceName = getPreferenceNameFromFieldName(field);
        
        preference.setKey(getSettingsNameFromFieldName(field.getName()));
        preference.setTitle(preferenceName);
        
        preference.setOnPreferenceChangeListener(SetterInvokeOnPreferenceChangeListener)
        
        if (preference instanceof DialogPreference) {
        	((DialogPreference)preference).setDialogTitle(preferenceName);
        }
	}
}
