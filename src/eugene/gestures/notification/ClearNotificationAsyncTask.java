package eugene.gestures.notification;

import eugene.config.Config;
import android.os.AsyncTask;
import android.util.Log;

public class ClearNotificationAsyncTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected synchronized Void doInBackground(Void... params) {
		try {
			wait(Config.INSTANCE.getShoutBoxNotificationsDisplayTime());
		} catch (InterruptedException e) {
			//task cancelled while in progress. no action needed
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Shouter.clear();
	}
}
