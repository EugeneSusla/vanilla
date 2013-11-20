package eugene.preferences;

/**
 * Usage: {@code class C implements SerializableToPreferences<C>}
 * @author Eugene
 *
 * @param <T> Class, implementing this interface
 */
public interface SerializableToPreferences {
	public String toSettingsString();
}
