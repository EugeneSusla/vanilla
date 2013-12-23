package eugene.gestures.action.impl;

import eugene.gestures.action.Action;
import eugene.gestures.notification.ShoutNotification;

public class NotificationAction implements Action {

	private ShoutNotification notification;
	
	public NotificationAction(ShoutNotification notification) {
		this.notification = notification;
	}
	
	@Override
	public ShoutNotification invoke() {
		return notification;
	}

	@Override
	public String getDisplayName() {
		return "Notification: " + notification.asText();
	}

	@Override
	public String toSettingsString() {
		throw new UnsupportedOperationException("This type of action cannot be written to settings");
	}

}
