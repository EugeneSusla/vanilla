package eugene.gestures.notification;

import eugene.ioc.ComponentResolver;

public class Shouter {
	
	private Shouter() {}
	
	public static void shout(String message) {
		ComponentResolver.getFullPlaybackActivity().shout(message);
	}
	
	public static void shout(ShoutNotification shoutNotification) {
		ComponentResolver.getFullPlaybackActivity().shout(shoutNotification.asText());
	}
}
