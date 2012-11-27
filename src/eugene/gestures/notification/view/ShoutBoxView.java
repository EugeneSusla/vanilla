package eugene.gestures.notification.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import eugene.gestures.notification.DrawableNotification;

public class ShoutBoxView extends View {

	private DrawableNotification currentNotification;
	
	public ShoutBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		invalidate();
	}
	
	public void displayNotification(DrawableNotification notification) {
		currentNotification = notification;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("ShoutBox", "drawing notification!");
//		canvas.drawColor(Color.CYAN);
		if (currentNotification != null) {
			currentNotification.asDrawable().draw(canvas);
		}
	}

	public DrawableNotification getCurrentNotification() {
		return currentNotification;
	}
}
