package eugene.gestures.notification.song;

import ch.blinkenlights.android.vanilla.Song;
import eugene.ioc.ComponentResolver;

public class PreviousSongNotification extends AbstractSongInfoNotification {

	public static final PreviousSongNotification INSTANCE = new PreviousSongNotification();
	
	private PreviousSongNotification() {
		super();
	}
	
	@Override
	public Song getSong() {
		return ComponentResolver.getPlaybackService().getSong(-1);
	}

}
