package eugene.gestures.notification;

import android.util.Log;
import eugene.ioc.ComponentResolver;

public class Shouter {
	
	private Shouter() {}
	
	public static void shout(String message) {
		ComponentResolver.getFullPlaybackActivity().shout(message);
		shout(new DrawableTextNotification(message));
	}

	public static void logMessage(String message) {
		Log.i(Shouter.class.getSimpleName(), message);
	}
	
	public static void shout(ShoutNotification shoutNotification) {
		shout(shoutNotification.asText());
	}
	
	public static void shout(DrawableNotification drawableNotification) {
		logMessage(drawableNotification.asText());
		ComponentResolver.getFullPlaybackActivity().getShoutBoxView().displayNotification(drawableNotification);
	}
}
