package eugene.gestures.action.impl;

import ch.blinkenlights.android.vanilla.Action;
import eugene.gestures.action.VanillaAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.song.CurrentSongNotification;
import eugene.ioc.ComponentResolver;

public class PlayPauseAction extends VanillaAction {

	public PlayPauseAction() {
		super(Action.PlayPause);
	}

	@Override
	public ShoutNotification invoke() {
		super.invoke();
		return CurrentSongNotification.INSTANCE;
	}
	
	@Override
	public String getDisplayName() {
		if (ComponentResolver.getPlaybackService().isPlaying()) {
			return "Pause";
		} else {
			return "Play";
		}
	}
}
