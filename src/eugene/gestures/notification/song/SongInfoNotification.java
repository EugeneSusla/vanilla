package eugene.gestures.notification.song;

import ch.blinkenlights.android.vanilla.Song;
import eugene.config.Config;

public class SongInfoNotification extends AbstractSongInfoNotification {

	private Song song;

	public SongInfoNotification(Song song) {
		this.song = song;
	}

	@Override
	public Song getSong() {
		return song;
	}

}
