package eugene.config;

import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;

//TODO merge somehow with android settings mechanism
public enum Config {
	INSTANCE;
	
	private int gestureStrokeMinPixelThreshold = 10;
	private ShoutNotification unrecognisedGestureNotification = new ShoutNotificationImpl("Unrecognized Gesture");
	/**
	 * Dim screen in compact mode. Includes software buttons and navigation panel.
	 */
	private boolean useLowProfileInCompactMode = true;
	
	private float defaultCoverLeftOffset = 36f/400f;

	public int getGestureStrokeMinPixelThreshold() {
		return gestureStrokeMinPixelThreshold;
	}

	public void setGestureStrokeMinPixelThreshold(
			int gestureStrokeMinPixelThreshold) {
		this.gestureStrokeMinPixelThreshold = gestureStrokeMinPixelThreshold;
	}

	public ShoutNotification getUnrecognisedGestureNotification() {
		return unrecognisedGestureNotification;
	}

	public void setUnrecognisedGestureNotification(
			ShoutNotification unrecognisedGestureNotification) {
		this.unrecognisedGestureNotification = unrecognisedGestureNotification;
	}

	public float getDefaultCoverLeftOffset() {
		return defaultCoverLeftOffset;
	}

	public void setDefaultCoverLeftOffset(float defaultCoverLeftOffset) {
		this.defaultCoverLeftOffset = defaultCoverLeftOffset;
	}

	public boolean isUseLowProfileInCompactMode() {
		return useLowProfileInCompactMode;
	}

	public void setUseLowProfileInCompactMode(boolean useLowProfileInCompactMode) {
		this.useLowProfileInCompactMode = useLowProfileInCompactMode;
	}

}
