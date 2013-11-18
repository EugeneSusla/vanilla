package eugene.gestures.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eugene.gestures.action.impl.NextTrackAction;
import eugene.gestures.action.impl.PlayPauseAction;
import eugene.gestures.action.impl.PreviousTrackAction;
import eugene.gestures.action.impl.ShowQueueAction;

public enum ActionManager {
	INSTANCE;

	private static final String VANILLA_ACTION_PREFIX = "vanillaAction://";
	private static final String CLASS_PREFIX = "class://";

	private transient ConcurrentHashMap<String, Action> actionCache = new ConcurrentHashMap<String, Action>();
	@SuppressWarnings("unchecked")
	private transient List<Class<? extends StatelessAction>> statelessActions = new ArrayList<Class<? extends StatelessAction>>(
			Arrays.asList(NextTrackAction.class, PreviousTrackAction.class,
					ShowQueueAction.class));
	private transient Map<String, String> knownActions;

	public Action getActionBySettingsName(String settingsName) {
		Action cachedAction = actionCache.get(settingsName);
		if (cachedAction == null) {
			cachedAction = resolveName(settingsName);
			actionCache.putIfAbsent(settingsName, cachedAction);
		}
		return cachedAction;
	}

	private Action resolveName(String settingsName) {
		if (settingsName.startsWith(CLASS_PREFIX)) {
			String className = settingsName.substring(CLASS_PREFIX.length());
			try {
				Class<?> actionClass = Class.forName(className);
				if (Action.class.isAssignableFrom(actionClass)) {
					Object instance = actionClass.newInstance();
					Action action = (Action) instance;
					return action;
				} else {
					throw new IllegalArgumentException("Class " + className
							+ " in not a " + Action.class.getName());
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else if (settingsName.startsWith(VANILLA_ACTION_PREFIX)) {
			String vanillaActionName = settingsName
					.substring(VANILLA_ACTION_PREFIX.length());
			return new VanillaAction(
					ch.blinkenlights.android.vanilla.Action
							.valueOf(vanillaActionName));
		} else {
			throw new IllegalArgumentException("Can't recognize action: "
					+ settingsName);
		}
	}

	public Action getActionInstance(Class<? extends Action> actionClass) {
		return getActionBySettingsName(CLASS_PREFIX + actionClass.getName());
	}

	public VanillaAction getVanillaAction(
			ch.blinkenlights.android.vanilla.Action vanillaAction) {
		if (vanillaAction == ch.blinkenlights.android.vanilla.Action.PlayPause) {
			return (VanillaAction) getActionInstance(PlayPauseAction.class);
		}
		return (VanillaAction) getActionBySettingsName(VANILLA_ACTION_PREFIX
				+ vanillaAction.name());
	}

	public void invoke(Class<? extends Action> actionClass) {
		getActionInstance(actionClass).invoke();
	}

	public void invoke(ch.blinkenlights.android.vanilla.Action vanillaAction) {
		getVanillaAction(vanillaAction).invoke();
	}

	public void invoke(String settingsName) {
		getActionBySettingsName(settingsName).invoke();
	}

	/**
	 * SettingsName -> DisplayName
	 */
	public Map<String, String> getKnownActions() {
		if (knownActions == null) {
			knownActions = new LinkedHashMap<String, String>();
			for (ch.blinkenlights.android.vanilla.Action vanillaAction : ch.blinkenlights.android.vanilla.Action
					.values()) {
				knownActions.put(VanillaAction.getSettingsName(vanillaAction),
						VanillaAction.getDisplayName(vanillaAction));
			}
			for (Class<? extends Action> clazz : statelessActions) {
				Action action = getActionInstance(clazz);
				knownActions.put(action.getSettingsName(), action.getDisplayName());
			}
		}
		return knownActions;
	}
}
