package eugene.gestures.notification.song;

import android.graphics.Canvas;
import ch.blinkenlights.android.vanilla.CoverBitmap;
import ch.blinkenlights.android.vanilla.Song;
import eugene.gestures.notification.DrawableNotification;
import eugene.ioc.ComponentResolver;
import eugene.utils.StringUtils;

public abstract class AbstractSongInfoNotification extends DrawableNotification {

	public static final String SEPARATOR = "/";

	public abstract Song getSong();

	@Override
	public String asText() {
		Song song = getSong();
		if (song == null) {
			return "";
		}
		return song.title + SEPARATOR + song.artist + SEPARATOR + song.album;
	}

	@Override
	public void draw(Canvas canvas) {
		Song song = getSong();
		if (song != null) {
			CoverBitmap
					.drawSongInfo(canvas, song, canvas.getWidth(), canvas
							.getHeight(), ComponentResolver.getCoverView()
							.getContext());
		}
	}
}