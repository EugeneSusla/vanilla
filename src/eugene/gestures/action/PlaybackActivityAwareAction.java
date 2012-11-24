package eugene.gestures.action;

import ch.blinkenlights.android.vanilla.PlaybackActivity;
import eugene.gestures.notification.ShoutNotification;


public abstract class PlaybackActivityAwareAction extends ClassNameDisplayNameAction {
	protected PlaybackActivity playbackActivity;
	
	@Override
	public ShoutNotification invoke() {
		if (playbackActivity == null) {
			throw new IllegalStateException("Action not properly instantiated. Use setPlaybackActivity() before invoking this activity"); 
		}
		return invokeAction();
	}
	
	public abstract ShoutNotification invokeAction();
	
	public void setPlaybackActivity(PlaybackActivity playbackActivity) {
		this.playbackActivity = playbackActivity;
	}
	
	public PlaybackActivity getPlaybackActivity() {
		return playbackActivity;
	}
}
