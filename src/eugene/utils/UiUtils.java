package eugene.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Window;

public class UiUtils {
	private UiUtils() {
	}

	public static int getStatusBarHeight(Activity activity) {
		Rect rect = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}
}
