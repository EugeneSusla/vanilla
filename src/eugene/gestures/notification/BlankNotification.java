package eugene.gestures.notification;

public class BlankNotification extends ShoutNotificationImpl {

	public static final BlankNotification INSTANCE = new BlankNotification();
	
	private BlankNotification() {
		super("");
	}

}
