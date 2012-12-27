package eugene.instapreferences.builder;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.TwoStatePreference;

public class TwoStatePreferenceBuilderImpl extends TwoStatePreferenceBuilder {

	@Override
	protected TwoStatePreference createPreference(Context context) {
		return new CheckBoxPreference(context);
	}

}
