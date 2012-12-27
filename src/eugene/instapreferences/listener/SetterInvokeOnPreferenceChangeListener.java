package eugene.instapreferences.listener;

import eugene.utils.StringUtils;
import android.preference.Preference;

public class SetterInvokeOnPreferenceChangeListener implements
		Preference.OnPreferenceChangeListener {

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
