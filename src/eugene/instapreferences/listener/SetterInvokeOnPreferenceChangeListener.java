package eugene.instapreferences.listener;

import eugene.config.Config;
import eugene.utils.StringUtils;
import android.preference.Preference;

public class SetterInvokeOnPreferenceChangeListener implements
		Preference.OnPreferenceChangeListener {

	public static final SetterInvokeOnPreferenceChangeListener INSTANCE = new SetterInvokeOnPreferenceChangeListener(Config.INSTANCE);
	
	private final Object configBean;

	public SetterInvokeOnPreferenceChangeListener(Object conficBean) {
		this.configBean = conficBean;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String setterName = "set"
				+ StringUtils.underscoreToCamelCaseFirstCapital(preference.getKey());
//		configBean.getClass().getMethod(setterName, );
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
