package eugene.config.enumerations;

import android.text.SpannableStringBuilder;
import ch.blinkenlights.android.vanilla.Song;

public enum QueueSecondaryInfoFormat {
	ALBUM {
		@Override
		public void append(SpannableStringBuilder spannableStringBuilder, Song song) {
			spannableStringBuilder.append(song.album);
		}
	},
	ARTIST {
		@Override
		public void append(SpannableStringBuilder spannableStringBuilder, Song song) {
			spannableStringBuilder.append(song.artist);
		}
	},
	ARTIST_DASH_ALBUM {
		@Override
		public void append(SpannableStringBuilder spannableStringBuilder, Song song) {
			spannableStringBuilder.append(song.artist).append(" - ").append(song.album);
		}
	};

	public abstract void append(SpannableStringBuilder spannableStringBuilder, Song song);
}
