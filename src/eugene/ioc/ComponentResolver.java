package eugene.ioc;

import ch.blinkenlights.android.vanilla.CoverView;
import ch.blinkenlights.android.vanilla.FullPlaybackActivity;
import ch.blinkenlights.android.vanilla.PlaybackService;

public class ComponentResolver {
	
	private static FullPlaybackActivity fullPlaybackActivity;
	
	private ComponentResolver() {
	}

	public static PlaybackService getPlaybackService() {
		return PlaybackService.get(fullPlaybackActivity);
	}
	
	public static CoverView getCoverView() {
		return fullPlaybackActivity.getCoverView();
	}
	
	public static FullPlaybackActivity getFullPlaybackActivity() {
		return fullPlaybackActivity;
	}

	public static void setFullPlaybackActivity(FullPlaybackActivity fullPlaybackActivity) {
		ComponentResolver.fullPlaybackActivity = fullPlaybackActivity;
	}
}
