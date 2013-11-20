package eugene.gestures.action.impl;

import eugene.gestures.ActionableEvent;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.action.StatelessAction;
import eugene.gestures.listener.GestureListener;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.MutableTextNotification;
import eugene.gestures.notification.song.NextSongNotification;
import eugene.ioc.ComponentResolver;

public class NextTrackAction extends StatelessAction implements
		AssignmentAwareAction {

	@Override
	public ShoutNotification invoke() {
		ComponentResolver.getCoverView().onSideSwipe(1);
		return null;
	}

	@Override
	public void onActionAssignment(ActionableEvent gesture, ActionableEvent oldGesture,
			GestureListener gestureListener) {
		if (oldGesture != null) {
			gestureListener.unRegisterMidwayGesture(oldGesture);
		}
		gestureListener.registerActionOnMidwayGesture(gesture,
				new NotificationAction(NextSongNotification.INSTANCE));
	}

}
