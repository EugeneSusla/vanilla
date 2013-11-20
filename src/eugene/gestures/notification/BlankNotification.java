package eugene.gestures.notification;

public class BlankNotification extends MutableTextNotification {

	public static final BlankNotification INSTANCE = new BlankNotification();
	
	private BlankNotification() {
		super("");
	}

}
