package eugene.gestures.action;

import ch.blinkenlights.android.vanilla.PlaybackService;
import eugene.gestures.StringUtils;
import eugene.gestures.notification.ShoutNotification;

public class VanillaAction extends PlaybackActivityAwareAction {
	
	private ch.blinkenlights.android.vanilla.Action vanillaAction;
	
	public VanillaAction(ch.blinkenlights.android.vanilla.Action action) {
		super();
		this.vanillaAction = action;
	}

	@Override
	public ShoutNotification invokeAction() {
		PlaybackService.get(getPlaybackActivity()).performAction(vanillaAction, getPlaybackActivity());
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
