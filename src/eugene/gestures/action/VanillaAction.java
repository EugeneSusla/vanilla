package eugene.gestures.action;

import eugene.gestures.notification.ShoutNotification;
import eugene.ioc.ComponentResolver;
import eugene.utils.StringUtils;

public class VanillaAction implements eugene.gestures.action.Action {
	
	private ch.blinkenlights.android.vanilla.Action vanillaAction;
	
	public static String getSettingsName(ch.blinkenlights.android.vanilla.Action action) {
		return "vanillaAction://" + action.name();
	}
	
	public static String getDisplayName(ch.blinkenlights.android.vanilla.Action action) {
		return StringUtils.camelCaseToPlainString(action.name());
	}
	
	public VanillaAction(ch.blinkenlights.android.vanilla.Action action) {
		super();
		this.vanillaAction = action;
	}

	@Override
	public ShoutNotification invoke() {
		ComponentResolver.getFullPlaybackActivity().performAction(vanillaAction);
		return null;
	}

	@Override
	public String getDisplayName() {
		return getDisplayName(vanillaAction);
	}
	
	@Override
	public String toSettingsString() {
		return getSettingsName(vanillaAction);
	}
}
