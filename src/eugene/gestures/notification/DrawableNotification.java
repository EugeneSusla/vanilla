package eugene.gestures.notification;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;


public abstract class DrawableNotification extends Drawable implements ShoutNotification {

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		throw new UnsupportedOperationException();
	}
	
	public abstract void draw(Canvas canvas);
	
	@Override
	public void displayNotification() {
		Shouter.shout(this);
	}

}
