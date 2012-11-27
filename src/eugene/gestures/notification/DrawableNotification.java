package eugene.gestures.notification;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;


public abstract class DrawableNotification implements ShoutNotification {

	ColorDrawable colorDrawable;
	
	private Drawable drawable = new Drawable() {

		@Override
		public void draw(Canvas canvas) {
			DrawableNotification.this.draw(canvas);
		}

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
	};
	
	public abstract void draw(Canvas canvas);
	
	public Drawable asDrawable() {
		return drawable;
	}
	
	@Override
	public void displayNotification() {
		Shouter.shout(this);
	}

}
