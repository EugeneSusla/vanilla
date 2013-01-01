package eugene.gestures.notification;

import eugene.config.Config;
import android.os.AsyncTask;
import android.util.Log;

public class ClearNotificationAsyncTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected synchronized Void doInBackground(Void... params) {
		Log.d("ClearNotificationAsyncTask", "started background task");
		try {
			wait(Config.INSTANCE.getShoutBoxNotificationsDisplayTime());
		} catch (InterruptedException e) {
			Log.d("ClearNotificationAsyncTask", "task cancelled while in progress");
		}
		Log.d("ClearNotificationAsyncTask", "finished background task");
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Log.d("ClearNotificationAsyncTask", "post execute");
		Shouter.clear();
	}
}
