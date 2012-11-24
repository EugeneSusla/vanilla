package eugene.gestures;

import eugene.config.Config;

public class StrokeUtils {
	private StrokeUtils() {}
	
	public static Stroke getStrokeFromDeltas(float deltaX, float deltaY) {
		float deltaXAbs = Math.abs(deltaX);
		float deltaYAbs = Math.abs(deltaY);
		
		int strokeMinPixelThreshold = Config.INSTANCE.getGestureStrokeMinPixelThreshold();
		if (deltaXAbs < strokeMinPixelThreshold && deltaYAbs < strokeMinPixelThreshold) {
			return Stroke.STATIC;
		}
		
		if (deltaXAbs > deltaYAbs) {
			return (deltaX < 0 ? Stroke.RIGHT : Stroke.LEFT);
		} else {
			return (deltaY < 0 ? Stroke.DOWN : Stroke.UP);
		}
	}
}
