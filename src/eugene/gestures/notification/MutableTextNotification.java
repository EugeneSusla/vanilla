package eugene.gestures.notification;

public class MutableTextNotification extends TextNotification {
	
	public MutableTextNotification(String message) {
		super(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
