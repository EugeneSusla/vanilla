package eugene.gestures.action;

import eugene.gestures.StringUtils;

public abstract class ClassNameDisplayNameAction implements Action {

	@Override
	public String getDisplayName() {
		String className = getClass().getSimpleName();
		String actionNameCamelCase;
		if (className.endsWith("Action")) {
			actionNameCamelCase = className.substring(0, className.length() - 6);
		} else {
			actionNameCamelCase = className;
		}
		return StringUtils.camelCaseToPlainString(actionNameCamelCase);
	}
	
	@Override
	public String getSettingsName() {
		return "class://" + getClass().getName();
	}
}
