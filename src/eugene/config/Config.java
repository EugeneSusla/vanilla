package eugene.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.pm.ActivityInfo;
import ch.blinkenlights.android.vanilla.MediaUtils;
import eugene.config.enumerations.QueueSecondaryInfoFormat;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.MutableTextNotification;
import eugene.instapreferences.annotation.Hide;
import eugene.instapreferences.annotation.Title;
import eugene.utils.EncodingUtils;
import eugene.utils.FolderFilteringUtils;

//TODO merge somehow with android settings mechanism
public enum Config {
	INSTANCE;

	private int gestureStrokeMinPixelThreshold = 10;
	@Hide
	private ShoutNotification unrecognisedGestureNotification = new MutableTextNotification(
			"Unrecognized Gesture");
	/**
	 * Dim screen in compact mode. Includes software buttons and navigation
	 * panel.
	 */
	private boolean useLowProfileInCompactMode = true;	//default false
	private boolean shoutBoxDrawSongInfoWhenIdle = true;
	private boolean shoutBoxClearNotifications = true;
	private int shoutBoxNotificationsDisplayTime = 2000;
	/** This may require restart */
	private boolean hideActionBarOnPlaybackScreen = true; // default false
	@Title("TEST TITLE")
	private boolean hideNotificationBarInCompactMode = false;  // default false
	/**
	 * 0 to 1 (percentage of total height)
	 */
	private float shoutBoxHeight = 0.25f;

	private boolean treatAlbumAsPartOfSongInfo = false;

	@Hide
	private float defaultCoverLeftOffset = 36f / 400f;

	private boolean librarySwapArrowAndMainBodyAction = true;
	private boolean libraryGoToPlaybackOnFolderAction = true;
	private boolean libraryExpandFolderActionOn = true;

	private String defaultCharset = Charset.forName("windows-1251").name();
	private transient boolean performCharsetConversion = EncodingUtils
			.shouldConvertFromEncoding(defaultCharset);
	private boolean smartDetectAdditionalLatin = true;

	// TODO make non-transient
	private transient List<String> includeFolders = new ArrayList<String>(Arrays.asList(
			"/storage/emulated/0/_Autosync/Audio", "/storage/emulated/0/Music"));
	// TODO make non-transient
	private transient List<String> excludeFolders = new ArrayList<String>(
			Arrays.asList("/storage/emulated/0/Music/_Books"));
	private transient String folderFilterSQLPart = null;
	private boolean sortByFilename = true;

	@Hide
	private int folderLimiterViewVerticalPadding = 17; // default = 2
	@Hide
	private int folderLimiterViewHorizontalPadding = 8; // default = 5
	private boolean folderLimiterDisplayX = false;
	
	private int maximumStrokesInGesture = 4; // default = 5

	// TODO make non-transient
	// Effectively enum: ActivityInfo.SCREEN_ORIENTATION_*
	private transient int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; // default
																						// ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
	
	@Hide
	private QueueSecondaryInfoFormat queueSecondaryInfoFormat = QueueSecondaryInfoFormat.ARTIST; 

	/*-------------------------------------------------------------------*/

	public int getGestureStrokeMinPixelThreshold() {
		return gestureStrokeMinPixelThreshold;
	}

	public void setGestureStrokeMinPixelThreshold(int gestureStrokeMinPixelThreshold) {
		this.gestureStrokeMinPixelThreshold = gestureStrokeMinPixelThreshold;
	}

	public ShoutNotification getUnrecognisedGestureNotification() {
		return unrecognisedGestureNotification;
	}

	public void setUnrecognisedGestureNotification(ShoutNotification unrecognisedGestureNotification) {
		this.unrecognisedGestureNotification = unrecognisedGestureNotification;
	}

	public float getDefaultCoverLeftOffset() {
		return defaultCoverLeftOffset;
	}

	public void setDefaultCoverLeftOffset(float defaultCoverLeftOffset) {
		this.defaultCoverLeftOffset = defaultCoverLeftOffset;
	}

	public boolean isUseLowProfileInCompactMode() {
		return useLowProfileInCompactMode;
	}

	public void setUseLowProfileInCompactMode(boolean useLowProfileInCompactMode) {
		this.useLowProfileInCompactMode = useLowProfileInCompactMode;
	}

	public boolean isShoutBoxDrawSongInfoWhenIdle() {
		return shoutBoxDrawSongInfoWhenIdle;
	}

	public void setShoutBoxDrawSongInfoWhenIdle(boolean shoutBoxDrawSongInfoWhenIdle) {
		this.shoutBoxDrawSongInfoWhenIdle = shoutBoxDrawSongInfoWhenIdle;
	}

	public float getShoutBoxHeight() {
		return shoutBoxHeight;
	}

	public void setShoutBoxHeight(float shoutBoxHeight) {
		this.shoutBoxHeight = shoutBoxHeight;
	}

	public boolean isTreatAlbumAsPartOfSongInfo() {
		return treatAlbumAsPartOfSongInfo;
	}

	public void setTreatAlbumAsPartOfSongInfo(boolean treatAlbumAsPartOfSongInfo) {
		this.treatAlbumAsPartOfSongInfo = treatAlbumAsPartOfSongInfo;
	}

	public boolean isLibrarySwapArrowAndMainBodyAction() {
		return librarySwapArrowAndMainBodyAction;
	}

	public void setLibrarySwapArrowAndMainBodyAction(boolean librarySwapArrowAndMainBodyAction) {
		this.librarySwapArrowAndMainBodyAction = librarySwapArrowAndMainBodyAction;
	}

	public boolean isLibraryGoToPlaybackOnFolderAction() {
		return libraryGoToPlaybackOnFolderAction;
	}

	public void setLibraryGoToPlaybackOnFolderAction(boolean libraryGoToPlaylistOnFolderAction) {
		this.libraryGoToPlaybackOnFolderAction = libraryGoToPlaylistOnFolderAction;
	}

	public boolean isLibraryExpandFolderActionOn() {
		return libraryExpandFolderActionOn;
	}

	public void setLibraryExpandFolderActionOn(boolean libraryExpandFolderActionOn) {
		this.libraryExpandFolderActionOn = libraryExpandFolderActionOn;
	}

	public String getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
		performCharsetConversion = EncodingUtils.shouldConvertFromEncoding(defaultCharset);
	}

	public boolean isPerformCharsetConversion() {
		return performCharsetConversion;
	}

	public boolean isSmartDetectAdditionalLatin() {
		return smartDetectAdditionalLatin;
	}

	public void setSmartDetectAdditionalLatin(boolean smartDetectAdditionalLatin) {
		this.smartDetectAdditionalLatin = smartDetectAdditionalLatin;
	}

	public List<String> getIncludeFolders() {
		return includeFolders;
	}

	public void setIncludeFolders(List<String> includeFolders) {
		this.includeFolders = includeFolders;
		folderFilterSQLPart = null;
	}

	public List<String> getExcludeFolders() {
		return excludeFolders;
	}

	public void setExcludeFolders(List<String> excludeFolders) {
		this.excludeFolders = excludeFolders;
		folderFilterSQLPart = null;
	}

	public String getFolderFilterSQLPart() {
		if (folderFilterSQLPart == null) {
			folderFilterSQLPart = FolderFilteringUtils.getFolderFilterSQLPart();
		}
		return folderFilterSQLPart;
	}

	public int getFolderLimiterViewVerticalPadding() {
		return folderLimiterViewVerticalPadding;
	}

	public void setFolderLimiterViewVerticalPadding(int folderLimiterViewVerticalPadding) {
		this.folderLimiterViewVerticalPadding = folderLimiterViewVerticalPadding;
	}

	public boolean isFolderLimiterDisplayX() {
		return folderLimiterDisplayX;
	}

	public void setFolderLimiterDisplayX(boolean folderLimiterDisplayX) {
		this.folderLimiterDisplayX = folderLimiterDisplayX;
	}

	public int getFolderLimiterViewHorizontalPadding() {
		return folderLimiterViewHorizontalPadding;
	}

	public void setFolderLimiterViewHorizontalPadding(int folderLimiterViewHorizontalPadding) {
		this.folderLimiterViewHorizontalPadding = folderLimiterViewHorizontalPadding;
	}

	public boolean isSortByFilename() {
		return sortByFilename;
	}

	public void setSortByFilename(boolean sortByFilename) {
		this.sortByFilename = sortByFilename;
		MediaUtils.updateDefaultSort();
	}

	public boolean isHideActionBarOnPlaybackScreen() {
		return hideActionBarOnPlaybackScreen;
	}

	public void setHideActionBarOnPlaybackScreen(boolean hideActionBarOnPlaybackScreen) {
		this.hideActionBarOnPlaybackScreen = hideActionBarOnPlaybackScreen;
	}

	public int getScreenOrientation() {
		return screenOrientation;
	}

	public void setScreenOrientation(int screenOrientation) {
		this.screenOrientation = screenOrientation;
	}

	public boolean isHideNotificationBarInCompactMode() {
		return hideNotificationBarInCompactMode;
	}

	public void setHideNotificationBarInCompactMode(boolean hideNotificationBarInCompactMode) {
		this.hideNotificationBarInCompactMode = hideNotificationBarInCompactMode;
	}

	public boolean isShoutBoxClearNotifications() {
		return shoutBoxClearNotifications;
	}

	public void setShoutBoxClearNotifications(boolean shoutBoxClearNotifications) {
		this.shoutBoxClearNotifications = shoutBoxClearNotifications;
	}

	public int getShoutBoxNotificationsDisplayTime() {
		return shoutBoxNotificationsDisplayTime;
	}

	public void setShoutBoxNotificationsDisplayTime(int shoutBoxNotificationsDisplayTime) {
		this.shoutBoxNotificationsDisplayTime = shoutBoxNotificationsDisplayTime;
	}

	public QueueSecondaryInfoFormat getQueueSecondaryInfoFormat() {
		return queueSecondaryInfoFormat;
	}

	public void setQueueSecondaryInfoFormat(QueueSecondaryInfoFormat queueSecondaryInfoFormat) {
		this.queueSecondaryInfoFormat = queueSecondaryInfoFormat;
	}

	public int getMaximumStrokesInGesture() {
		return maximumStrokesInGesture;
	}

	public void setMaximumStrokesInGesture(int maximumStrokesInGesture) {
		this.maximumStrokesInGesture = maximumStrokesInGesture;
	}
}
