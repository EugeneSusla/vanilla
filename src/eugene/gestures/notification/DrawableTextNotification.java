package eugene.gestures.notification;

import ch.blinkenlights.android.vanilla.CoverBitmap;
import eugene.config.Config;
import eugene.ioc.ComponentResolver;
import android.graphics.Canvas;

public class DrawableTextNotification extends DrawableNotification {

	private String message;

	public DrawableTextNotification(String message) {
		this.message = message;
	}

	@Override
	public String asText() {
		return message;
	}

	@Override
	public void draw(Canvas canvas) {
		CoverBitmap.drawText(canvas, message, canvas.getWidth(),
		// (int) (canvas.getHeight() * Config.INSTANCE
		// .getShoutBoxHeight()),
				canvas.getHeight(), ComponentResolver.getCoverView()
						.getContext());
	}

}
