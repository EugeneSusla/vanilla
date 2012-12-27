package eugene.config;

import ch.blinkenlights.android.vanilla.Action;
import ch.blinkenlights.android.vanilla.PlaybackService;
import ch.blinkenlights.android.vanilla.preference.PrefKeys;
import android.content.SharedPreferences;
import eugene.gestures.BasicGesture;
import eugene.gestures.Stroke;
import eugene.gestures.action.ActionManager;
import eugene.gestures.action.impl.NextTrackAction;
import eugene.gestures.action.impl.PreviousTrackAction;
import eugene.gestures.action.impl.ShowQueueAction;
import eugene.gestures.listener.ActionMapGestureListener;
import eugene.gestures.listener.GestureListener;
import eugene.ioc.ComponentResolver;

public class GestureMappingConfig {

	public static GestureListener getGestureListener() {
		SharedPreferences prefs = PlaybackService.getSettings(ComponentResolver.getFullPlaybackActivity());
		Action upAction = Action.getAction(prefs, PrefKeys.SWIPE_UP_ACTION,
				Action.Nothing);
		Action downAction = Action.getAction(prefs, PrefKeys.SWIPE_DOWN_ACTION,
				Action.Nothing);
		Action coverPressAction = Action.getAction(prefs,
				PrefKeys.COVER_PRESS_ACTION, Action.ToggleControls);
		Action coverLongPressAction = Action.getAction(prefs,
				PrefKeys.COVER_LONGPRESS_ACTION, Action.PlayPause);
		
		GestureListener gestureListener = new ActionMapGestureListener();
		gestureListener.registerActionOnGesture(BasicGesture.valueOf(Stroke.UP), ActionManager.INSTANCE.getVanillaAction(upAction));
		gestureListener.registerActionOnGesture(BasicGesture.valueOf(Stroke.DOWN), ActionManager.INSTANCE.getVanillaAction(downAction));
		gestureListener.registerActionOnGesture(BasicGesture.valueOf(Stroke.LEFT), ActionManager.INSTANCE.getActionInstance(NextTrackAction.class));
		gestureListener.registerActionOnGesture(BasicGesture.valueOf(Stroke.RIGHT), ActionManager.INSTANCE.getActionInstance(PreviousTrackAction.class));
		gestureListener.registerActionOnGesture(BasicGesture.TAP, ActionManager.INSTANCE.getVanillaAction(coverPressAction));
		//gestureListener.registerActionOnGesture(BasicGesture.LONG_TAP, ActionManager.INSTANCE.getVanillaAction(coverLongPressAction));
		gestureListener.registerActionOnGesture(BasicGesture.LONG_TAP, ActionManager.INSTANCE.getActionInstance(ShowQueueAction.class));
		return gestureListener;
	}
}