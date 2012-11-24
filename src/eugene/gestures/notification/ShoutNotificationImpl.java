package eugene.gestures.notification;

public class ShoutNotificationImpl implements ShoutNotification {

	private String message;
	
	public ShoutNotificationImpl(String message) {
		this.setMessage(message);
	}
	
	@Override
	public String asText() {
		return message;
	}

	@Override
	public void displayNotification() {
		Shouter.shout(this);
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
