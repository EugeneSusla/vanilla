package eugene.gestures.action.impl;

import android.content.Intent;
import ch.blinkenlights.android.vanilla.FullPlaybackActivity;
import ch.blinkenlights.android.vanilla.ShowQueueActivity;
import eugene.gestures.action.ClassNameDisplayNameAction;
import eugene.gestures.notification.ShoutNotification;
import eugene.ioc.ComponentResolver;

public class ShowQueueAction extends ClassNameDisplayNameAction {

	@Override
	public ShoutNotification invoke() {
		FullPlaybackActivity fullPlaybackActivity = ComponentResolver.getFullPlaybackActivity();
		fullPlaybackActivity.startActivity(new Intent(fullPlaybackActivity, ShowQueueActivity.class));
		return null;
	}

}
