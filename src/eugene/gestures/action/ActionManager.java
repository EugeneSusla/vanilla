package eugene.gestures.action;

import java.util.concurrent.ConcurrentHashMap;

import eugene.gestures.action.impl.PlayPauseAction;
import eugene.ioc.ComponentResolver;

public enum ActionManager {
	INSTANCE;
	
	private static final String VANILLA_ACTION_PREFIX = "vanillaAction://";
	private static final String CLASS_PREFIX = "class://";
	private ConcurrentHashMap<String, Action> actionCache = new ConcurrentHashMap<String, Action>();
	
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
					throw new IllegalArgumentException("Class " + className + " in not a " + Action.class.getName());
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else if (settingsName.startsWith(VANILLA_ACTION_PREFIX)) {
			String vanillaActionName = settingsName.substring(VANILLA_ACTION_PREFIX.length());
			return new VanillaAction(ch.blinkenlights.android.vanilla.Action.valueOf(vanillaActionName));
		} else {
			throw new IllegalArgumentException("Can't recognize action: " + settingsName);
		}
	}
	
	public Action getActionInstance(Class<? extends Action> actionClass) {
		return getActionBySettingsName(CLASS_PREFIX + actionClass.getName());
	}
	
	public VanillaAction getVanillaAction(ch.blinkenlights.android.vanilla.Action vanillaAction) {
		if (vanillaAction == ch.blinkenlights.android.vanilla.Action.PlayPause) {
			return (VanillaAction) getActionInstance(PlayPauseAction.class);
		}
		return (VanillaAction) getActionBySettingsName(VANILLA_ACTION_PREFIX + vanillaAction.name());
	}
}
