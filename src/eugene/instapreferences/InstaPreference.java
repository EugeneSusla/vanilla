package eugene.instapreferences;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import eugene.instapreferences.builder.RootPreferenceBuilder;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

public class InstaPreference {

	private RootPreferenceBuilder rootPreferenceBuilder = new RootPreferenceBuilder();

	public void addToPreferenceActivity(PreferenceFragment preferenceFragment,
			Object defaultConfigBean) {
		PreferenceScreen preferenceScreen = getFromBean(preferenceFragment, defaultConfigBean);
		preferenceScreen.setKey("key");
		preferenceScreen.setTitle("PREFERENCE SCREEN TITLE");
		preferenceScreen.setSummary("PREFERENCE SCREEN SUMMARY");
		preferenceFragment.setPreferenceScreen(preferenceScreen);
	}

	public void addToPreferenceActivity(PreferenceActivity preferenceActivity,
			Object defaultConfigBean) {
		PreferenceScreen preferenceScreen = getFromBean(preferenceActivity, defaultConfigBean);
		preferenceScreen.setKey("key");
		preferenceScreen.setTitle("PREFERENCE SCREEN TITLE");
		preferenceScreen.setSummary("PREFERENCE SCREEN SUMMARY");
		preferenceActivity.setPreferenceScreen(preferenceScreen);
	}

	@SuppressWarnings("deprecation")
	public PreferenceScreen getFromBean(PreferenceActivity preferenceActivity,
			Object defaultConfigBean) {
		PreferenceScreen preferenceScreen = preferenceActivity.getPreferenceManager()
				.createPreferenceScreen(preferenceActivity);

		populatePreferenceScreen(preferenceScreen, preferenceActivity, defaultConfigBean);

		return preferenceScreen;
	}

	public PreferenceScreen getFromBean(PreferenceFragment preferenceFragment,
			Object defaultConfigBean) {
		PreferenceScreen preferenceScreen = preferenceFragment.getPreferenceManager()
				.createPreferenceScreen(preferenceFragment.getActivity());

		populatePreferenceScreen(preferenceScreen, preferenceFragment.getActivity(),
				defaultConfigBean);

		return preferenceScreen;
	}

	private void populatePreferenceScreen(PreferenceScreen preferenceScreen,
			Context context, Object defaultConfigBean) {
		for (Field field : defaultConfigBean.getClass().getDeclaredFields()) {
			int fieldModifiers = field.getModifiers();
			if (Modifier.isTransient(fieldModifiers) || Modifier.isStatic(fieldModifiers)) {
				continue;
			}
			field.setAccessible(true);
			Preference preference = createPreferenceFromField(context, field,
					defaultConfigBean);
			preferenceScreen.addPreference(preference);
		}
	}

	private Preference createPreferenceFromField(Context context, Field field,
			Object defaultConfigBean) {
		try {
			return rootPreferenceBuilder.build(context, field.get(defaultConfigBean), field);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
