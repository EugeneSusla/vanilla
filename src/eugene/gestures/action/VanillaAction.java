package eugene.gestures.action;

import eugene.gestures.notification.ShoutNotification;
import eugene.ioc.ComponentResolver;
import eugene.utils.StringUtils;

public class VanillaAction implements eugene.gestures.action.Action {
	
	private ch.blinkenlights.android.vanilla.Action vanillaAction;
	
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
		return StringUtils.camelCaseToPlainString(vanillaAction.name());
	}
	
	@Override
	public String getSettingsName() {
		return "vanillaAction://" + vanillaAction.name();
	}
}
