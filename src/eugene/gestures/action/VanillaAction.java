package eugene.gestures.action;

import ch.blinkenlights.android.vanilla.FullPlaybackActivity;
import ch.blinkenlights.android.vanilla.PlaybackService;
import eugene.gestures.StringUtils;
import eugene.gestures.notification.ShoutNotification;
import eugene.ioc.ComponentResolver;

public class VanillaAction extends ClassNameDisplayNameAction {
	
	private ch.blinkenlights.android.vanilla.Action vanillaAction;
	
	public VanillaAction(ch.blinkenlights.android.vanilla.Action action) {
		super();
		this.vanillaAction = action;
	}

	@Override
	public ShoutNotification invoke() {
		FullPlaybackActivity playbackActivity = ComponentResolver.getFullPlaybackActivity();
		ComponentResolver.getPlaybackService().performAction(vanillaAction, playbackActivity);
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
