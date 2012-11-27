package eugene.gestures.action.impl;

import eugene.gestures.BasicGesture;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.action.ClassNameDisplayNameAction;
import eugene.gestures.listener.GestureListener;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;
import eugene.gestures.notification.song.NextSongNotification;
import eugene.ioc.ComponentResolver;

public class NextTrackAction extends ClassNameDisplayNameAction implements
		AssignmentAwareAction {

	@Override
	public ShoutNotification invoke() {
		ComponentResolver.getCoverView().onSideSwipe(1);
		return null;
	}

	@Override
	public void onActionAssignment(BasicGesture gesture, BasicGesture oldGesture,
			GestureListener gestureListener) {
		if (oldGesture != null) {
			gestureListener.unRegisterMidwayGesture(oldGesture);
		}
		gestureListener.registerActionOnMidwayGesture(gesture,
				new NotificationAction(NextSongNotification.INSTANCE));
	}

}
