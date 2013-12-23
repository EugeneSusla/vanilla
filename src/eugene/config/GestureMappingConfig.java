package eugene.config;

import java.util.List;

import android.content.SharedPreferences;
import ch.blinkenlights.android.vanilla.Action;
import ch.blinkenlights.android.vanilla.PrefKeys;
import eugene.gestures.ActionableEvent;
import eugene.gestures.Gesture;
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
		SharedPreferences prefs = ComponentResolver.getSharedPreferences();
		GestureListener gestureListener = new ActionMapGestureListener();
		List<String> allSettingsKeys = ActionableEvent.getAllSettingsKeys();
		for (String key : allSettingsKeys) {
			if (prefs.contains(key)) {
				String value = prefs.getString(key, null);
				if (value != null && value != eugene.gestures.action.Action.NO_OP.toSettingsString()) {
					gestureListener.registerActionOnGesture(
							ActionableEvent.fromSettingsString(key), 
							ActionManager.INSTANCE.getActionBySettingsName(value));
				}
			}
		}
		return gestureListener;
	}

	@Deprecated
	public static GestureListener getTestGestureListener() {
		SharedPreferences prefs = ComponentResolver.getSharedPreferences();
		Action upAction = Action.getAction(prefs, PrefKeys.SWIPE_UP_ACTION,
				Action.Nothing);
		Action downAction = Action.getAction(prefs, PrefKeys.SWIPE_DOWN_ACTION,
				Action.Nothing);
		Action coverPressAction = Action.getAction(prefs,
				PrefKeys.COVER_PRESS_ACTION, Action.ToggleControls);
		Action coverLongPressAction = Action.getAction(prefs,
				PrefKeys.COVER_LONGPRESS_ACTION, Action.PlayPause);

		GestureListener gestureListener = new ActionMapGestureListener();
		gestureListener.registerActionOnGesture(Gesture.valueOf(Stroke.UP),
				ActionManager.INSTANCE.getVanillaAction(upAction));
		gestureListener.registerActionOnGesture(Gesture.valueOf(Stroke.DOWN),
				ActionManager.INSTANCE.getVanillaAction(downAction));
		gestureListener
				.registerActionOnGesture(Gesture.valueOf(Stroke.LEFT),
						ActionManager.INSTANCE
								.getActionInstance(NextTrackAction.class));
		gestureListener.registerActionOnGesture(Gesture.valueOf(Stroke.RIGHT),
				ActionManager.INSTANCE
						.getActionInstance(PreviousTrackAction.class));
		gestureListener.registerActionOnGesture(ActionableEvent.TAP,
				ActionManager.INSTANCE.getVanillaAction(coverPressAction));
		// gestureListener.registerActionOnGesture(BasicGesture.LONG_TAP,
		// ActionManager.INSTANCE.getVanillaAction(coverLongPressAction));
		gestureListener
				.registerActionOnGesture(ActionableEvent.LONG_TAP,
						ActionManager.INSTANCE
								.getActionInstance(ShowQueueAction.class));
		return gestureListener;
	}
}