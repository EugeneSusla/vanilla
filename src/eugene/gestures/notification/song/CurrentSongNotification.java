package eugene.gestures.notification.song;

import ch.blinkenlights.android.vanilla.Song;
import eugene.ioc.ComponentResolver;

public class CurrentSongNotification extends AbstractSongInfoNotification {

	public static final CurrentSongNotification INSTANCE = new CurrentSongNotification();

	@Override
	public Song getSong() {
		return ComponentResolver.getFullPlaybackActivity().getCurrentSong();
	}

}
