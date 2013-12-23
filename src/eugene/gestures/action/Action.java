package eugene.gestures.action;

import eugene.gestures.notification.ShoutNotification;
import eugene.preferences.SerializableToPreferences;

public interface Action extends SerializableToPreferences {
	public static final Action NO_OP = ActionManager.INSTANCE
			.getVanillaAction(ch.blinkenlights.android.vanilla.Action.Nothing);

	ShoutNotification invoke();

	String getDisplayName();
}
