package eugene.gestures.action;

import java.util.concurrent.ConcurrentHashMap;

import eugene.ioc.ComponentResolver;

public enum ActionManager {
	INSTANCE;
	
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
		if (settingsName.startsWith("class://")) {
			String className = settingsName.substring("class://".length());
			try {
				Class<?> actionClass = Class.forName(className);
				if (Action.class.isAssignableFrom(actionClass)) {
					Object instance = actionClass.newInstance();
					Action action = (Action) instance;
					injectActionDependencies(action);
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
		} else if (settingsName.startsWith("vanillaAction://")) {
			String vanillaActionName = settingsName.substring("vanillaAction://".length());
			return new VanillaAction(ch.blinkenlights.android.vanilla.Action.valueOf(vanillaActionName));
		} else {
			throw new IllegalArgumentException("Can't recognize action: " + settingsName);
		}
	}

	private void injectActionDependencies(Action action) {
		if (action instanceof FullPlaybackActivityAwareAction) {
			((FullPlaybackActivityAwareAction)action).setFullPlaybackActivity(ComponentResolver.getFullPlaybackActivity());
			return;
		}
		if (action instanceof PlaybackActivityAwareAction) {
			((PlaybackActivityAwareAction)action).setPlaybackActivity(ComponentResolver.getFullPlaybackActivity());
		}
	}
}
