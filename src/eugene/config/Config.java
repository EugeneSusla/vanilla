package eugene.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.DatabaseUtils;
import eugene.gestures.notification.ShoutNotification;
import eugene.gestures.notification.ShoutNotificationImpl;
import eugene.utils.EncodingUtils;
import eugene.utils.FolderFilteringUtils;

//TODO merge somehow with android settings mechanism
public enum Config {
	INSTANCE;

	private int gestureStrokeMinPixelThreshold = 10;
	private ShoutNotification unrecognisedGestureNotification = new ShoutNotificationImpl(
			"Unrecognized Gesture");
	/**
	 * Dim screen in compact mode. Includes software buttons and navigation
	 * panel.
	 */
	private boolean useLowProfileInCompactMode = true;
	private boolean shoutBoxDrawSongInfoWhenIdle = true;
	/**
	 * 0 to 1 (percentage of total height)
	 */
	private float shoutBoxHeight = 0.25f;

	private boolean treatAlbumAsPartOfSongInfo = false;

	private float defaultCoverLeftOffset = 36f / 400f;

	private boolean librarySwapArrowAndMainBodyAction = true;
	private boolean libraryGoToPlaybackOnFolderAction = true;
	private boolean libraryExpandFolderActionOn = true;

	private String defaultCharset = Charset.forName("windows-1251").name();
	private transient boolean performCharsetConversion = EncodingUtils
			.shouldConvertFromEncoding(defaultCharset);
	private boolean smartDetectAdditionalLatin = true;

	private List<String> includeFolders = new ArrayList<String>(Arrays.asList(
			"/storage/sdcard0/_Autosync/Audio", "/storage/sdcard0/Music"));
	private List<String> excludeFolders = new ArrayList<String>(
			Arrays.asList("/storage/sdcard0/Music/_Books"));
	private transient String folderFilterSQLPart = null;
	
	private int folderLimiterViewVerticalPadding = 17;	//default = 2
	private int folderLimiterViewHorizontalPadding = 8;	//default = 5
	private boolean folderLimiterDisplayX = false;

	/*-------------------------------------------------------------------*/

	public int getGestureStrokeMinPixelThreshold() {
		return gestureStrokeMinPixelThreshold;
	}

	public void setGestureStrokeMinPixelThreshold(
			int gestureStrokeMinPixelThreshold) {
		this.gestureStrokeMinPixelThreshold = gestureStrokeMinPixelThreshold;
	}

	public ShoutNotification getUnrecognisedGestureNotification() {
		return unrecognisedGestureNotification;
	}

	public void setUnrecognisedGestureNotification(
			ShoutNotification unrecognisedGestureNotification) {
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

	public void setShoutBoxDrawSongInfoWhenIdle(
			boolean shoutBoxDrawSongInfoWhenIdle) {
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

	public void setLibrarySwapArrowAndMainBodyAction(
			boolean librarySwapArrowAndMainBodyAction) {
		this.librarySwapArrowAndMainBodyAction = librarySwapArrowAndMainBodyAction;
	}

	public boolean isLibraryGoToPlaybackOnFolderAction() {
		return libraryGoToPlaybackOnFolderAction;
	}

	public void setLibraryGoToPlaybackOnFolderAction(
			boolean libraryGoToPlaylistOnFolderAction) {
		this.libraryGoToPlaybackOnFolderAction = libraryGoToPlaylistOnFolderAction;
	}

	public boolean isLibraryExpandFolderActionOn() {
		return libraryExpandFolderActionOn;
	}

	public void setLibraryExpandFolderActionOn(
			boolean libraryExpandFolderActionOn) {
		this.libraryExpandFolderActionOn = libraryExpandFolderActionOn;
	}

	public String getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
		performCharsetConversion = EncodingUtils
				.shouldConvertFromEncoding(defaultCharset);
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

	public void setFolderLimiterViewVerticalPadding(
			int folderLimiterViewVerticalPadding) {
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

	public void setFolderLimiterViewHorizontalPadding(
			int folderLimiterViewHorizontalPadding) {
		this.folderLimiterViewHorizontalPadding = folderLimiterViewHorizontalPadding;
	}
}
