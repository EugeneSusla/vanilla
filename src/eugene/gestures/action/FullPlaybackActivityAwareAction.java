package eugene.gestures.action;

import ch.blinkenlights.android.vanilla.FullPlaybackActivity;

public abstract class FullPlaybackActivityAwareAction extends
		PlaybackActivityAwareAction {

	public void setFullPlaybackActivity(FullPlaybackActivity fullPlaybackActivity) {
		super.setPlaybackActivity(fullPlaybackActivity);
	}
	
	public FullPlaybackActivity getFullPlaybackActivity() {
		return (FullPlaybackActivity)playbackActivity;
	}
}
