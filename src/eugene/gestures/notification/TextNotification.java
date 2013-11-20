package eugene.gestures.notification;

import ch.blinkenlights.android.vanilla.CoverBitmap;
import eugene.config.Config;
import eugene.ioc.ComponentResolver;
import android.graphics.Canvas;

public class TextNotification extends DrawableNotification {

	protected String message;

	public TextNotification(String message) {
		this.message = message;
	}

	@Override
	public String asText() {
		return message;
	}

	@Override
	public void draw(Canvas canvas) {
		CoverBitmap.drawText(canvas, message, canvas.getWidth(),
				canvas.getHeight(), ComponentResolver.getCoverView()
						.getContext());
	}

}
