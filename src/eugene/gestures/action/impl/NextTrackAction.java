package eugene.gestures.action.impl;

import eugene.gestures.action.ClassNameDisplayNameAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;
import eugene.ioc.ComponentResolver;

public class NextTrackAction extends ClassNameDisplayNameAction {

	@Override
	public ShoutNotification invoke() {
		ComponentResolver.getCoverView().onSideSwipe(1);
		return new ShoutNotificationImpl("Next song");
	}

}
