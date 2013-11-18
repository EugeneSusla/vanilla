package eugene.gestures.action;

import eugene.gestures.notification.ShoutNotification;

public interface Action {
	public static final Action NO_OP = ActionManager.INSTANCE
			.getVanillaAction(ch.blinkenlights.android.vanilla.Action.Nothing);

	ShoutNotification invoke();

	String getDisplayName();

	/**
	 * Used to store action in settings
	 */
	String getSettingsName();
}
