package eugene.gestures.notification.song;

import ch.blinkenlights.android.vanilla.Song;
import eugene.ioc.ComponentResolver;

public class NextSongNotification extends AbstractSongInfoNotification {

	public static final NextSongNotification INSTANCE = new NextSongNotification();
	
	@Override
	public Song getSong() {
		return ComponentResolver.getPlaybackService().getSong(1);
	}

}
