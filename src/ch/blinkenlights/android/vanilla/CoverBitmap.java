/*
 * Copyright (C) 2010, 2011 Christopher Eby <kreed@kreed.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.blinkenlights.android.vanilla;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import eugene.config.Config;

/**
 * Class containing utility functions to create Bitmaps display song info and
 * album art.
 */
public final class CoverBitmap {

	/**
	 * Draw cover in background and a box with song info on top.
	 */
	public static final int STYLE_OVERLAPPING_BOX = 0;
	/**
	 * Draw cover on top or left with song info on bottom or right (depending on
	 * orientation).
	 */
	public static final int STYLE_INFO_BELOW = 1;
	/**
	 * Draw no song info, only the cover.
	 */
	public static final int STYLE_NO_INFO = 2;
	/**
	 * Draw cover in background and a box with song info on top of the fallback
	 * cover. Uses an offset to not draw on top of the fallback cover
	 */
	public static final int STYLE_OVERLAPPING_BOX_FOR_DEFAULT_COVER = 3;

	private static int TEXT_SIZE = -1;
	private static int TEXT_SIZE_BIG;
	private static int PADDING;
	private static int TEXT_SPACE;
	private static Bitmap SONG_ICON;
	private static Bitmap ALBUM_ICON;
	private static Bitmap ARTIST_ICON;

	/**
	 * Initialize the regular text size members.
	 * 
	 * @param context
	 *            A context to use.
	 */
	private static void loadTextSizes(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		TEXT_SIZE = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
		TEXT_SIZE_BIG = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
		PADDING = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				10, metrics);
		TEXT_SPACE = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 150, metrics);
	}

	/**
	 * Initialize the icon bitmaps.
	 * 
	 * @param context
	 *            A context to use.
	 */
	private static void loadIcons(Context context) {
		Resources res = context.getResources();
		SONG_ICON = BitmapFactory.decodeResource(res,
				R.drawable.ic_tab_songs_selected);
		ALBUM_ICON = BitmapFactory.decodeResource(res,
				R.drawable.ic_tab_albums_selected);
		ARTIST_ICON = BitmapFactory.decodeResource(res,
				R.drawable.ic_tab_artists_selected);
	}

	/**
	 * Helper function to draw text within a set width
	 * 
	 * @param canvas
	 *            The canvas to draw to
	 * @param text
	 *            The text to draw
	 * @param left
	 *            The x coordinate of the left edge of the text
	 * @param top
	 *            The y coordinate of the top edge of the text
	 * @param width
	 *            The measured width of the text
	 * @param maxWidth
	 *            The maximum width of the text. Text outside of this width will
	 *            be truncated.
	 * @param paint
	 *            The paint style to use
	 */
	private static void drawText(Canvas canvas, String text, int left, int top,
			int width, int maxWidth, Paint paint) {
		canvas.save();
		int offset = Math.max(0, maxWidth - width) / 2;
		canvas.clipRect(left, top, left + maxWidth, top + paint.getTextSize()
				* 2);
		canvas.drawText(text, left + offset, top - paint.ascent(), paint);
		canvas.restore();
	}

	/**
	 * Create an image representing the given song. Includes cover art and
	 * possibly song title/artist/ablum, depending on the given style.
	 * 
	 * @param context
	 *            A context to use.
	 * @param style
	 *            One of CoverBitmap.STYLE_*
	 * @param coverArt
	 *            The cover art for the song.
	 * @param song
	 *            Title and other data are taken from here for info modes.
	 * @param width
	 *            Maximum width of image
	 * @param height
	 *            Maximum height of image
	 * @return The image, or null if the song was null, or width or height were
	 *         less than 1
	 */
	public static Bitmap createBitmap(Context context, int style,
			Bitmap coverArt, Song song, int width, int height) {
		switch (style) {
		case STYLE_OVERLAPPING_BOX:
			return createOverlappingBitmap(context, coverArt, song, width,
					height);
		case STYLE_OVERLAPPING_BOX_FOR_DEFAULT_COVER:
			return createOverlappingBitmap(context, coverArt, song, width,
					height, Config.INSTANCE.getDefaultCoverLeftOffset());
		case STYLE_INFO_BELOW:
			return createSeparatedBitmap(context, coverArt, song, width, height);
		case STYLE_NO_INFO:
			return createScaledBitmap(coverArt, width, height);
		default:
			throw new IllegalArgumentException("Invalid bitmap type given: "
					+ style);
		}
	}

	private static Bitmap createOverlappingBitmap(Context context,
			Bitmap cover, Song song, int width, int height) {
		return createOverlappingBitmap(context, cover, song, width, height, 0f);
	}

	private static Bitmap createOverlappingBitmap(Context context,
			Bitmap cover, Song song, int width, int height, float boxOffset) {

		/* cover dimensions phase starts */
		int coverWidth;
		int coverHeight;

		if (cover == null) {
			coverWidth = 0;
			coverHeight = 0;
		} else {
			coverWidth = cover.getWidth();
			coverHeight = cover.getHeight();

			float scale = Math.min((float) width / coverWidth, (float) height
					/ coverHeight);

			coverWidth *= scale;
			coverHeight *= scale;
		}
		/* cover dimensions phase ends */

		Paint paint = initDrawing(context);

		Rect textBoxDimensions = measureTextBox(song,
				(int) (width * (1f - 2f * boxOffset)), height, paint);

		int bitmapWidth = Math.max(coverWidth, textBoxDimensions.right);
		int bitmapHeight = Math.max(coverHeight, textBoxDimensions.bottom);

		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);

		if (cover != null) {
			int x = (bitmapWidth - coverWidth) / 2;
			int y = (bitmapHeight - coverHeight) / 2;
			Rect rect = new Rect(x, y, x + coverWidth, y + coverHeight);
			canvas.drawBitmap(cover, null, rect, paint);
		}

		drawSongInfo(song, paint, textBoxDimensions, bitmapWidth, bitmapHeight,
				canvas);

		return bitmap;
	}

	public static void drawSongInfo(Canvas canvas, Song song, int maxWidth,
			int maxHeight, Context context) {
		Paint paint = initDrawing(context);

		Rect textBoxDimensions = measureTextBox(song, maxWidth, maxHeight,
				paint);

		drawSongInfo(song, paint, textBoxDimensions, maxWidth, maxHeight,
				canvas);
	}

	public static void drawText(Canvas canvas, String text, int maxWidth,
			int maxHeight, Context context) {
		Paint paint = initDrawing(context);
		int fontSize = TEXT_SIZE_BIG;
		Rect textBoxDimensions = measureTextBox(text, fontSize, maxWidth,
				maxHeight, paint);
		drawText(text, fontSize, paint, textBoxDimensions, maxWidth, maxHeight,
				canvas);
	}

	private static Paint initDrawing(Context context) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (TEXT_SIZE == -1)
			loadTextSizes(context);
		return paint;
	}

	private static void drawSongInfo(Song song, Paint paint,
			Rect textBoxDimensions, int availableWidth, int availableHeight,
			Canvas canvas) {
		int left = (availableWidth - textBoxDimensions.right) / 2;
		int top = (availableHeight - textBoxDimensions.bottom) / 2;
		int right = (availableWidth + textBoxDimensions.right) / 2;
		int bottom = (availableHeight + textBoxDimensions.bottom) / 2;

		paint.setARGB(150, 0, 0, 0);
		canvas.drawRect(left, top, right, bottom, paint);

		int maxWidth = textBoxDimensions.right - PADDING * 2;
		paint.setARGB(255, 255, 255, 255);
		top += PADDING;
		left += PADDING;

		paint.setTextSize(TEXT_SIZE_BIG);
		String title = getSontTitle(song);
		int titleWidth = (int) paint.measureText(title);
		drawText(canvas, title, left, top, titleWidth, maxWidth, paint);
		top += TEXT_SIZE_BIG + PADDING;

		paint.setTextSize(TEXT_SIZE);

		if (Config.INSTANCE.isTreatAlbumAsPartOfSongInfo()) {
			String album = getSongAlbum(song);
			int albumWidth = (int) paint.measureText(album);
			drawText(canvas, album, left, top, albumWidth, maxWidth, paint);
			top += TEXT_SIZE + PADDING;
		}

		String artist = getSongArtist(song);
		int artistWidth = (int) paint.measureText(artist);
		drawText(canvas, artist, left, top, artistWidth, maxWidth, paint);
	}

	private static void drawText(String text, int textSize, Paint paint,
			Rect textBoxDimensions, int availableWidth, int availableHeight,
			Canvas canvas) {
		int left = (availableWidth - textBoxDimensions.right) / 2;
		int top = (availableHeight - textBoxDimensions.bottom) / 2;
		int right = (availableWidth + textBoxDimensions.right) / 2;
		int bottom = (availableHeight + textBoxDimensions.bottom) / 2;

		paint.setARGB(150, 0, 0, 0);
		canvas.drawRect(left, top, right, bottom, paint);

		int maxWidth = textBoxDimensions.right - PADDING * 2;
		paint.setARGB(255, 255, 255, 255);
		top += PADDING;
		left += PADDING;

		paint.setTextSize(textSize);
		int titleWidth = (int) paint.measureText(text);

		paint.setTextSize(textSize);
		drawText(canvas, text, left, top, titleWidth, maxWidth, paint);
	}

	private static Rect measureTextBox(Song song, int maximumWidth,
			int maximumHeight, Paint paint) {
		paint.setTextSize(TEXT_SIZE_BIG);
		int titleWidth = (int) paint.measureText(getSontTitle(song));
		paint.setTextSize(TEXT_SIZE);
		boolean treatAlbumAsPartOfSongInfo = Config.INSTANCE
				.isTreatAlbumAsPartOfSongInfo();
		int albumWidth = treatAlbumAsPartOfSongInfo ? (int) paint
				.measureText(getSongAlbum(song)) : 0;
		int artistWidth = (int) paint.measureText(getSongArtist(song));

		int boxWidth = Math.min(maximumWidth,
				Math.max(titleWidth, Math.max(artistWidth, albumWidth))
						+ PADDING * 2);
		int textHeight = TEXT_SIZE_BIG + TEXT_SIZE + PADDING * 3;
		if (treatAlbumAsPartOfSongInfo) {
			textHeight += TEXT_SIZE + PADDING;
		}
		int boxHeight = Math.min(maximumHeight, textHeight);
		Rect textBoxDimensions = new Rect(0, 0, boxWidth, boxHeight);
		return textBoxDimensions;
	}

	private static Rect measureTextBox(String text, int textSize,
			int maximumWidth, int maximumHeight, Paint paint) {
		paint.setTextSize(textSize);
		int textWidth = (int) paint.measureText(text);

		int boxWidth = Math.min(maximumWidth, textWidth + PADDING * 2);
		int boxHeight = Math.min(maximumHeight, textSize + PADDING * 4);
		Rect textBoxDimensions = new Rect(0, 0, boxWidth, boxHeight);
		return textBoxDimensions;
	}

	private static String getSongArtist(Song song) {
		return song.artist == null ? "" : song.artist;
	}

	private static String getSongAlbum(Song song) {
		return song.album == null ? "" : song.album;
	}

	private static String getSontTitle(Song song) {
		return song.title == null ? "" : song.title;
	}

	private static Bitmap createSeparatedBitmap(Context context, Bitmap cover,
			Song song, int width, int height) {
		if (TEXT_SIZE == -1)
			loadTextSizes(context);
		if (SONG_ICON == null)
			loadIcons(context);

		boolean horizontal = width > height;

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		String title = getSontTitle(song);
		String album = getSongAlbum(song);
		String artist = getSongArtist(song);

		int textSize = TEXT_SIZE;
		int padding = PADDING;

		int coverWidth;
		int coverHeight;

		if (cover == null) {
			coverWidth = 0;
			coverHeight = 0;
		} else {
			coverWidth = cover.getWidth();
			coverHeight = cover.getHeight();

			int maxWidth = horizontal ? width - TEXT_SPACE : width;
			int maxHeight = horizontal ? height : height - textSize * 3
					- padding * 4;
			float scale = Math.min((float) maxWidth / coverWidth,
					(float) maxHeight / coverHeight);

			coverWidth *= scale;
			coverHeight *= scale;
		}

		paint.setTextSize(textSize);
		int titleWidth = (int) paint.measureText(title);
		int albumWidth = (int) paint.measureText(album);
		int artistWidth = (int) paint.measureText(artist);

		int maxBoxWidth = horizontal ? width - coverWidth : width;
		int maxBoxHeight = horizontal ? height : height - coverHeight;
		int boxWidth = Math.min(
				maxBoxWidth,
				textSize
						+ Math.max(titleWidth,
								Math.max(artistWidth, albumWidth)) + padding
						* 3);
		int boxHeight = Math.min(maxBoxHeight, textSize * 3 + padding * 4);

		int bitmapWidth = horizontal ? coverWidth + boxWidth : Math.max(
				coverWidth, boxWidth);
		int bitmapHeight = horizontal ? Math.max(coverHeight, boxHeight)
				: coverHeight + boxHeight;

		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);

		if (cover != null) {
			int x = horizontal ? 0 : (bitmapWidth - coverWidth) / 2;
			int y = horizontal ? (bitmapHeight - coverHeight) / 2 : 0;
			Rect rect = new Rect(x, y, x + coverWidth, y + coverHeight);
			canvas.drawBitmap(cover, null, rect, paint);
		}

		int top;
		int left;

		if (horizontal) {
			top = (bitmapHeight - boxHeight) / 2;
			left = padding + coverWidth;
		} else {
			top = padding + coverHeight;
			left = padding;
		}

		int maxWidth = boxWidth - padding * 3 - textSize;
		paint.setARGB(255, 255, 255, 255);

		canvas.drawBitmap(SONG_ICON, left, top, paint);
		drawText(canvas, title, left + padding + textSize, top, maxWidth,
				maxWidth, paint);
		top += textSize + padding;

		canvas.drawBitmap(ALBUM_ICON, left, top, paint);
		drawText(canvas, album, left + padding + textSize, top, maxWidth,
				maxWidth, paint);
		top += textSize + padding;

		canvas.drawBitmap(ARTIST_ICON, left, top, paint);
		drawText(canvas, artist, left + padding + textSize, top, maxWidth,
				maxWidth, paint);

		return bitmap;
	}

	/**
	 * Scales a bitmap to fit in a rectangle of the given size. Aspect ratio is
	 * preserved. At least one dimension of the result will match the provided
	 * dimension exactly.
	 * 
	 * @param source
	 *            The bitmap to be scaled
	 * @param width
	 *            Maximum width of image
	 * @param height
	 *            Maximum height of image
	 * @return The scaled bitmap.
	 */
	private static Bitmap createScaledBitmap(Bitmap source, int width,
			int height) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		float scale = Math.min((float) width / sourceWidth, (float) height
				/ sourceHeight);
		sourceWidth *= scale;
		sourceHeight *= scale;
		return Bitmap.createScaledBitmap(source, sourceWidth, sourceHeight,
				false);
	}

	/**
	 * Generate the default cover (a rendition of a CD). Returns a square iamge.
	 * Both dimensions are the lesser of width and height.
	 * 
	 * @param width
	 *            The max width
	 * @param height
	 *            The max height
	 * @return The default cover.
	 */
	public static Bitmap generateDefaultCover(int width, int height) {
		int size = Math.min(width, height);

		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(bitmap);

		drawCdCase(canvas, paint, size);

		// drawDisc(size, paint, canvas);

		return bitmap;
	}

	private static void drawDisc(int size, Paint paint, Canvas canvas) {
		int halfSize = size / 2;
		int eightSize = size / 8;

		LinearGradient gradient = new LinearGradient(size, 0, 0, size,
				0xff646464, 0xff464646, Shader.TileMode.CLAMP);
		RectF oval = new RectF(eightSize, 0, size - eightSize, size);

		canvas.rotate(-45, halfSize, halfSize);

		paint.setShader(gradient);
		canvas.translate(size / 20, size / 20);
		canvas.scale(0.9f, 0.9f);
		canvas.drawOval(oval, paint);

		paint.setShader(null);
		paint.setColor(0xff000000);
		canvas.translate(size / 3, size / 3);
		canvas.scale(0.333f, 0.333f);
		canvas.drawOval(oval, paint);

		paint.setShader(gradient);
		canvas.translate(size / 3, size / 3);
		canvas.scale(0.333f, 0.333f);
		canvas.drawOval(oval, paint);
	}

	private static void drawCdCase(Canvas canvas, Paint paint, int size) {
		int xStart = 2;
		int xEnd = size - 1;
		int x1 = (int) (size * (32f / 400f));
		int x2 = (int) (size * Config.INSTANCE.getDefaultCoverLeftOffset());
		int yStart = (int) (size * (23f / 400f));
		int yEnd = (int) (size * ((23f + 353f) / 400f));

		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(3);

		paint.setColor(Color.DKGRAY);
		paint.setStyle(Style.FILL);
		canvas.drawRect(xStart, yStart, x1, yEnd, paint);

		paint.setColor(Color.GRAY);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(xStart, yStart, xEnd, yEnd, paint);
		canvas.drawRect(xStart, yStart, x1, yEnd, paint);
		canvas.drawRect(x2, yStart, xEnd, yEnd, paint);
	}
}
