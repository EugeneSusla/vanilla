package eugene.gestures.notification.song;

import android.graphics.Canvas;
import ch.blinkenlights.android.vanilla.CoverBitmap;
import ch.blinkenlights.android.vanilla.Song;
import eugene.gestures.notification.DrawableNotification;
import eugene.ioc.ComponentResolver;

public abstract class AbstractSongInfoNotification extends DrawableNotification {

	public static final String SEPARATOR = "/";

	public abstract Song getSong();

	@Override
	public String asText() {
		return getSong().title + SEPARATOR + getSong().artist + SEPARATOR + getSong().album;
	}

	@Override
	public void draw(Canvas canvas) {
		CoverBitmap.drawSongInfo(canvas, getSong(), canvas.getWidth(),
				canvas.getHeight(), ComponentResolver.getCoverView()
						.getContext());
	}

}