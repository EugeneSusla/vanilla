/*
 * Copyright (C) 2012 Christopher Eby <kreed@kreed.org>
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

import eugene.gestures.BasicGesture;
import eugene.gestures.Stroke;
import eugene.gestures.StrokeUtils;
import eugene.gestures.notification.Shouter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Displays a flingable/draggable View of cover art/song info images generated
 * by CoverBitmap.
 */
public final class CoverView extends View implements Handler.Callback {
	/**
	 * The system-provided snap velocity, used as a threshold for detecting
	 * flings.
	 */
	private static int sSnapVelocity = -1;
	/**
	 * The screen density, from {@link DisplayMetrics#density}.
	 */
	private static double sDensity = -1;
	/**
	 * The Handler with which to do background work. Will be null until
	 * setupHandler is called.
	 */
	private Handler mHandler;
	/**
	 * A handler running on the UI thread, for UI operations.
	 */
	private final Handler mUiHandler = new Handler(this);
	/**
	 * How to render cover art and metadata. One of CoverBitmap.STYLE_*
	 */
	private int mCoverStyle;

	/**
	 * Interface to respond to CoverView motion actions.
	 */
	public interface Callback {
		/**
		 * Called after the view has scrolled to the previous or next cover.
		 * 
		 * @param delta
		 *            -1 for the previous cover, 1 for the next.
		 */
		public void shiftCurrentSong(int delta);

		/**
		 * Called when the user has swiped a gesture on the view.
		 */
		public void gesture(BasicGesture gesture);
		
		/**
		 * Called when the swiped gesture is changed.
		 */
		public void midwayGesture(BasicGesture gesture);
	}

	/**
	 * The instance of the callback.
	 */
	private Callback mCallback;
	/**
	 * The current set of songs: 0 = previous, 1 = current, and 2 = next.
	 */
	private Song[] mSongs = new Song[3];
	/**
	 * The covers for the current songs: 0 = previous, 1 = current, and 2 =
	 * next.
	 */
	private Bitmap[] mBitmaps = new Bitmap[3];
	/**
	 * The bitmaps to be drawn. Usually the same as mBitmaps, unless scrolling.
	 */
	private Bitmap[] mActiveBitmaps = mBitmaps;
	/**
	 * Cover art to use when a song has no cover art in no info display styles.
	 */
	private Bitmap mDefaultCover;
	/**
	 * Computes scroll animations.
	 */
	private final Scroller mScroller;
	/**
	 * Computes scroll velocity to detect flings.
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * The x coordinate of the last touch down or move event.
	 */
	private float mLastMotionX;
	/**
	 * The y coordinate of the last touch down or move event.
	 */
	private float mLastMotionY;
	/**
	 * The x coordinate of the last touch down event.
	 */
	private float mStartX;
	/**
	 * The y coordinate of the last touch down event.
	 */
	private float mStartY;
	/**
	 * Ignore the next pointer up event, for long presses.
	 */
	private boolean mIgnoreNextUp;
	/**
	 * If true, querySongs was called before the view initialized and should be
	 * called when initialization finishes.
	 */
	private boolean mPendingQuery;
	/**
	 * The current x scroll position of the view.
	 * 
	 * Scrolling code from {@link View} is not used for this class since many of
	 * its features are not required.
	 */
	private int mScrollX;
	/**
	 * True if a scroll is in progress (i.e. mScrollX != getWidth()), false
	 * otherwise.
	 */
	private boolean mScrolling;
	/**
	 * The current gesture that is performed
	 */
	private BasicGesture mCurrentGesture;
	/**
	 * Next/previous track scroll duration
	 */
	private int mScrollDuration = 1000;

	/**
	 * Constructor intended to be called by inflating from XML.
	 */
	public CoverView(Context context, AttributeSet attributes) {
		super(context, attributes);

		mScroller = new Scroller(context);

		if (sSnapVelocity == -1) {
			sSnapVelocity = ViewConfiguration.get(context)
					.getScaledMinimumFlingVelocity();
			sDensity = context.getResources().getDisplayMetrics().density;
		}
	}

	/**
	 * Setup the Handler and callback. This must be called before the CoverView
	 * is used.
	 * 
	 * @param looper
	 *            A looper created on a worker thread.
	 * @param callback
	 *            The callback for nextSong/previousSong
	 * @param style
	 *            One of CoverBitmap.STYLE_*
	 */
	public void setup(Looper looper, Callback callback, int style) {
		mHandler = new Handler(looper, this);
		mCallback = callback;
		mCoverStyle = style;
	}

	/**
	 * Reset the scroll position to its default state.
	 */
	private void resetScroll() {
		if (!mScroller.isFinished())
			mScroller.abortAnimation();
		mScrollX = getWidth();
		invalidate();
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth,
			int oldHeight) {
		if (mPendingQuery && width != 0 && height != 0) {
			mPendingQuery = false;
			querySongs(PlaybackService.get(getContext()));
		}
	}

	/**
	 * Paint the cover art views to the canvas.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		int x = 0;
		int scrollX = mScrollX;

		canvas.drawColor(Color.BLACK);

		// for (Bitmap bitmap : mActiveBitmaps) {
		for (int i = 0; i < mActiveBitmaps.length; i++) {
			Bitmap bitmap = mActiveBitmaps[i];
			if (bitmap != null && scrollX + width > x && scrollX < x + width) {
				int xOffset = (width - bitmap.getWidth()) / 2;
				int yOffset = (height - bitmap.getHeight()) / 2;
				int bitmapX = x + xOffset - scrollX;
				drawAlbumArt(canvas, bitmap, mSongs[i], bitmapX, yOffset);
			}
			x += width;
		}
	}

	private void drawAlbumArt(Canvas canvas, Bitmap bitmap, Song song, int x,
			int y) {
		canvas.drawBitmap(bitmap, x, y, null);
	}

	/**
	 * Scrolls the view when dragged. Animates a fling to one of the three
	 * covers when finished. The cover flung to will be either the nearest
	 * cover, or if the fling is fast enough, the cover in the direction of the
	 * fling.
	 * 
	 * Also performs a click on the view when it is tapped without dragging.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null)
			mVelocityTracker = VelocityTracker.obtain();
		mVelocityTracker.addMovement(ev);

		float x = ev.getX();
		float y = ev.getY();
		int scrollX = mScrollX;
		int width = getWidth();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
				mActiveBitmaps = mBitmaps;
			}

			mStartX = x;
			mStartY = y;
			mLastMotionX = x;
			mLastMotionY = y;
			mScrolling = true;

			mCurrentGesture = new BasicGesture();

			mUiHandler.sendEmptyMessageDelayed(MSG_LONG_CLICK,
					ViewConfiguration.getLongPressTimeout());
			break;
		case MotionEvent.ACTION_MOVE: {
			float deltaX = mLastMotionX - x;
			float deltaY = mLastMotionY - y;

			if (mCurrentGesture.addStroke(StrokeUtils.getStrokeFromDeltas(
					deltaX, deltaY))) {
//				Shouter.shout(mCurrentGesture.toString());
				onMidwayGesture(mCurrentGesture);
			}

			// if (Math.abs(deltaX) > Math.abs(deltaY)) {
			// performScroll(deltaX);
			// }

			mLastMotionX = x;
			mLastMotionY = y;
			break;
		}
		case MotionEvent.ACTION_UP: {
			mUiHandler.removeMessages(MSG_LONG_CLICK);

			VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(250);
			int velocityX = (int) velocityTracker.getXVelocity();
			int velocityY = (int) velocityTracker.getYVelocity();
			int mvx = Math.abs(velocityX);
			int mvy = Math.abs(velocityY);

			onGesture(mCurrentGesture);
			mCurrentGesture = null;

			// If -1 or 1, play the previous or next song, respectively and
			// scroll
			// to that song's cover. If 0, just scroll back to current song's
			// cover.
			int whichCover = 0;

			if (Math.abs(mStartX - x) + Math.abs(mStartY - y) < 10) {
				// A long press was performed and thus the normal action should
				// not be executed.
				if (mIgnoreNextUp)
					mIgnoreNextUp = false;
				else
					performClick();
				// } else if (mvx > sSnapVelocity || mvy > sSnapVelocity) {
				// if (mvy > mvx) {
				// if (velocityY > 0)
				// mCallback.downSwipe();
				// else
				// mCallback.upSwipe();
				// } else {
				// if (velocityX > 0)
				// whichCover = min;
				// else
				// whichCover = max;
				// }
			} else {
				int nearestCover = (scrollX + width / 2) / width - 1;
				whichCover = Math.max(-1, Math.min(nearestCover, 1));
			}

			// if (whichCover != 0) {
			// onSideSwipe(whichCover);
			// }

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			break;
		}
		}
		return true;
	}

	public void onSideSwipe(int whichCover) {
		if (whichCover == -1 && mSongs[0] == null) {
			return;
		}

		mScrollX = mScrollX - getWidth() * whichCover;
		Bitmap[] bitmaps = mBitmaps;
		// Save the two covers being scrolled between, so that if one
		// of them changes from switching songs (which can happen when
		// shuffling), the new cover doesn't pop in during the scroll.
		// mActiveBitmaps is reset when the scroll is finished.
		if (whichCover == 1) {
			mActiveBitmaps = new Bitmap[] { bitmaps[1], bitmaps[2], null };
		} else {
			mActiveBitmaps = new Bitmap[] { null, bitmaps[0], bitmaps[1] };
		}
		mCallback.shiftCurrentSong(whichCover);

		int delta = getWidth() - mScrollX;
		mScroller.startScroll(mScrollX, 0, delta, 0, mScrollDuration);
		mUiHandler.sendEmptyMessage(MSG_SCROLL);
	}

	private void performScroll(float deltaX) {
		int scrollX = mScrollX;
		int width = getWidth();
		if (deltaX < 0) {
			int availableToScroll = scrollX - (mSongs[0] == null ? width : 0);
			if (availableToScroll > 0) {
				mScrollX += Math.max(-availableToScroll, (int) deltaX);
				invalidate();
			}
		} else if (deltaX > 0) {
			int availableToScroll = width * 2 - scrollX;
			if (availableToScroll > 0) {
				mScrollX += Math.min(availableToScroll, (int) deltaX);
				invalidate();
			}
		}
	}

	private void onGesture(BasicGesture gesture) {
		// if (gesture.equals(BasicGesture.valueOf(Stroke.LEFT))) {
		// onSideSwipe(1);
		// return;
		// }
		// if (gesture.equals(BasicGesture.valueOf(Stroke.RIGHT))) {
		// onSideSwipe(-1);
		// return;
		// }
		mCallback.gesture(gesture);
	}
	
	private void onMidwayGesture(BasicGesture gesture) {
		// if (gesture.equals(BasicGesture.valueOf(Stroke.LEFT))) {
		// onSideSwipe(1);
		// return;
		// }
		// if (gesture.equals(BasicGesture.valueOf(Stroke.RIGHT))) {
		// onSideSwipe(-1);
		// return;
		// }
		mCallback.midwayGesture(gesture);
	}

	/**
	 * Generates a bitmap for the given song.
	 * 
	 * @param i
	 *            The position of the song in mSongs.
	 */
	private void generateBitmap(int i) {
		Song song = mSongs[i];

		int style = mCoverStyle;
		Context context = getContext();
		Bitmap cover = song == null ? null : song.getCover(context);

		if (cover == null && style == CoverBitmap.STYLE_NO_INFO) {
			Bitmap def = mDefaultCover;
			if (def == null) {
				mDefaultCover = def = CoverBitmap.generateDefaultCover(
						getWidth(), getHeight());
			}
			mBitmaps[i] = CoverBitmap.createBitmap(context,
					CoverBitmap.STYLE_OVERLAPPING_BOX_FOR_DEFAULT_COVER, def,
					song, getWidth(), getHeight());
		} else {
			mBitmaps[i] = CoverBitmap.createBitmap(context, style, cover, song,
					getWidth(), getHeight());
		}

		postInvalidate();
	}

	/**
	 * Set the Song at position <code>i</code> to <code>song</code>, generating
	 * the bitmap for it in the background if needed.
	 */
	public void setSong(int i, Song song) {
		if (song == mSongs[i])
			return;

		mSongs[i] = song;
		mBitmaps[i] = null;
		if (song != null) {
			mHandler.sendMessage(mHandler.obtainMessage(MSG_GENERATE_BITMAP, i,
					0));
		}
	}

	/**
	 * Query all songs. Must be called on the UI thread.
	 * 
	 * @param service
	 *            Service to query from.
	 */
	public void querySongs(PlaybackService service) {
		if (getWidth() == 0 || getHeight() == 0) {
			mPendingQuery = true;
			return;
		}

		mHandler.removeMessages(MSG_GENERATE_BITMAP);

		Song[] songs = mSongs;
		Bitmap[] bitmaps = mBitmaps;
		Song[] newSongs = { service.getSong(-1), service.getSong(0),
				service.getSong(1) };
		Bitmap[] newBitmaps = new Bitmap[3];
		mSongs = newSongs;
		mBitmaps = newBitmaps;
		if (!mScrolling)
			mActiveBitmaps = newBitmaps;

		for (int i = 0; i != 3; ++i) {
			if (newSongs[i] == null)
				continue;

			for (int j = 0; j != 3; ++j) {
				if (newSongs[i] == songs[j]) {
					newBitmaps[i] = bitmaps[j];
					break;
				}
			}

			if (newBitmaps[i] == null) {
				mHandler.sendMessage(mHandler.obtainMessage(
						MSG_GENERATE_BITMAP, i, 0));
			}
		}

		resetScroll();
	}

	/**
	 * Call {@link CoverView#generateBitmap(int)} for the song at the given
	 * index.
	 * 
	 * arg1 should be the index of the song.
	 */
	private static final int MSG_GENERATE_BITMAP = 0;
	/**
	 * Perform a long click.
	 * 
	 * @see View#performLongClick()
	 */
	private static final int MSG_LONG_CLICK = 2;
	/**
	 * Update position for fling scroll animation and, when it is finished,
	 * notify PlaybackService that the user has requested a track change and
	 * update the cover art views. Will resend message until scrolling is
	 * finished.
	 */
	private static final int MSG_SCROLL = 3;

	@Override
	public boolean handleMessage(Message message) {
		switch (message.what) {
		case MSG_GENERATE_BITMAP:
			generateBitmap(message.arg1);
			break;
		case MSG_LONG_CLICK:
			if (Math.abs(mStartX - mLastMotionX)
					+ Math.abs(mStartY - mLastMotionY) < 10) {
				mIgnoreNextUp = true;
				performLongClick();
			}
			break;
		case MSG_SCROLL:
			if (mScroller.computeScrollOffset()) {
				mScrollX = mScroller.getCurrX();
				invalidate();
				mUiHandler.sendEmptyMessage(MSG_SCROLL);
			} else {
				mScrolling = false;
				mActiveBitmaps = mBitmaps;
			}
			break;
		default:
			return false;
		}

		return true;
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		// This implementation only tries to handle two cases: use in the
		// FullPlaybackActivity, where we want to fill the whole screen,
		// and use in the MiniPlaybackActivity, where we want to be square.

		int width = View.MeasureSpec.getSize(widthSpec);
		int height = View.MeasureSpec.getSize(heightSpec);

		if (View.MeasureSpec.getMode(widthSpec) == View.MeasureSpec.EXACTLY
				&& View.MeasureSpec.getMode(heightSpec) == View.MeasureSpec.EXACTLY) {
			// FullPlaybackActivity: fill screen
			setMeasuredDimension(width, height);
		} else {
			// MiniPlaybackActivity: be square
			int size = Math.min(width, height);
			setMeasuredDimension(size, size);
		}
	}

	public BasicGesture getCurrentGesture() {
		return mCurrentGesture;
	}

	public void setCurrentGesture(BasicGesture newGesture) {
		if (mCurrentGesture == null || !mCurrentGesture.equals(newGesture)) {
			this.mCurrentGesture = newGesture;
			onMidwayGesture(mCurrentGesture);
		}
	}
}
