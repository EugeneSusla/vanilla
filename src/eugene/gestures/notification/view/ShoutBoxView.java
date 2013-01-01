package eugene.gestures.notification.view;

import java.util.Deque;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import eugene.config.Config;
import eugene.gestures.notification.ClearNotificationAsyncTask;
import eugene.gestures.notification.DrawableNotification;

public class ShoutBoxView extends View {

	private DrawableNotification currentNotification;
	private ClearNotificationAsyncTask lastClearRequest;
	
	public ShoutBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		invalidate();
	}
	
	public void displayNotification(DrawableNotification notification) {
		currentNotification = notification;
		if (Config.INSTANCE.isShoutBoxClearNotifications() && !notification.asText().isEmpty()) {
			if (lastClearRequest != null) {
				lastClearRequest.cancel(true);
			}
			lastClearRequest = new ClearNotificationAsyncTask();
			lastClearRequest.execute();
		}
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (currentNotification != null) {
			currentNotification.asDrawable().draw(canvas);
		}
	}

	public DrawableNotification getCurrentNotification() {
		return currentNotification;
	}
}
