package eugene.gestures.action.impl;

import eugene.gestures.Gesture;
import eugene.gestures.action.AssignmentAwareAction;
import eugene.gestures.action.StatelessAction;
import eugene.gestures.listener.GestureListener;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.song.PreviousSongNotification;
import eugene.ioc.ComponentResolver;

public class PreviousTrackAction extends StatelessAction implements
		AssignmentAwareAction {

	@Override
	public ShoutNotification invoke() {
		ComponentResolver.getCoverView().onSideSwipe(-1);
		return null;
	}

	@Override
	public void onActionAssignment(Gesture gesture, Gesture oldGesture,
			GestureListener gestureListener) {
		if (oldGesture != null) {
			gestureListener.unRegisterMidwayGesture(oldGesture);
		}
		gestureListener.registerActionOnMidwayGesture(gesture,
				new NotificationAction(PreviousSongNotification.INSTANCE));
	}

}
