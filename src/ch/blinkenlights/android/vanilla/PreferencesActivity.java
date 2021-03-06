/*
 * Copyright (C) 2012 Adrian Ulrich <adrian@blinkenlights.ch>
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import eugene.config.Config;
import eugene.config.GestureMappingConfig;
import eugene.gestures.ActionableEvent;
import eugene.gestures.action.*;
import eugene.gestures.action.Action;
import eugene.instapreferences.InstaPreference;

/**
 * The preferences activity in which one can change application preferences.
 */
public class PreferencesActivity extends PreferenceActivity {
	private static InstaPreference instaPreference = new InstaPreference();

	/**
	 * Initialize the activity, loading the preference specifications.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(Config.INSTANCE.getScreenOrientation());
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public static class AudioActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_audio);
		}
	}

	public static class AudioFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_audio);
		}
	}

	public static class PlaybackActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_playback);
		}
	}

	public static class PlaybackFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_playback);
		}
	}

	public static class LibraryActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_library);
		}
	}

	public static class LibraryFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_library);
			PreferenceGroup group = getPreferenceScreen();
			group.removePreference(group.findPreference("controls_in_selector"));
		}
	}

	public static class NotificationsActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_notifications);
		}
	}

	public static class NotificationsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_notifications);
		}
	}

	public static class ShakeActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_shake);
		}
	}

	public static class ShakeFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_shake);
		}
	}

	public static class GesturePlayerActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			instaPreference.addToPreferenceActivity(this, Config.INSTANCE);
		}
	}

	public static class GesturePlayerFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// instaPreference.addToPreferenceFragment(this, Config.INSTANCE`);
			SharedPreferences preferences = getPreferenceManager()
					.getSharedPreferences();
			PreferenceScreen preferenceScreen = getPreferenceManager()
					.createPreferenceScreen(getActivity());
			// TODO remove
			preferenceScreen.setKey("key");
			preferenceScreen.setTitle("PREFERENCE SCREEN TITLE");
			preferenceScreen.setSummary("PREFERENCE SCREEN SUMMARY");

			List<ActionableEvent> bindedEvents = new ArrayList<ActionableEvent>();
			List<ActionableEvent> unbindedEvents = new ArrayList<ActionableEvent>();
			for (String key : ActionableEvent.getAllSettingsKeys()) {
				ActionableEvent event = ActionableEvent.fromSettingsString(key);
				if (preferences.contains(key)
						&& !preferences.getString(key, "").equals(
								eugene.gestures.action.Action.NO_OP
										.toSettingsString())) {
					bindedEvents.add(event);
				} else {
					unbindedEvents.add(event);
				}
			}
            Map<ActionableEvent,Action> deafultMappings =
                    GestureMappingConfig.getDefaultGestureActionMapping();
            Collections.sort(bindedEvents);
			Collections.sort(unbindedEvents);
			addGesturesCategory("In Use", bindedEvents, preferenceScreen, deafultMappings);
			addGesturesCategory("Unused", unbindedEvents, preferenceScreen, deafultMappings);

			setPreferenceScreen(preferenceScreen);
		}

		private void addGesturesCategory(
                String categoryTitle,
				List<ActionableEvent> bindedGestures,
                PreferenceScreen preferenceScreen,
                Map<ActionableEvent, Action> deafultMappings) {
//			PreferenceGroup category = new PreferenceCategory(getActivity());
			PreferenceGroup category = preferenceScreen;
			category.setTitle(categoryTitle);
			for (ActionableEvent gesture : bindedGestures) {
				addGesturePreference(category, gesture, deafultMappings);
			}
//			preferenceScreen.addPreference(category);
		}

		private void addGesturePreference(PreferenceGroup category,
				ActionableEvent gesture, Map<ActionableEvent, Action> deafultMappings) {
			ListPreferenceSummary preference = new ListPreferenceSummary(
					getActivity(), null);
            Action defaultAction = deafultMappings.get(gesture);
            if (defaultAction == null) {
//                defaultAction = Action.NO_OP;
            } else {
                preference.setDefaultValue(defaultAction.toSettingsString());
            }
			preference.setKey(gesture.toSettingsString());
			preference.setTitle(gesture.toString());
			// TODO remove these resources
			// CharSequence[] vanillaActionEntries =
			// getResources().getTextArray(R.array.swipe_action_entries);
			// CharSequence[] vanillaActionValues =
			// getResources().getTextArray(R.array.swipe_action_values);
			Map<String, String> knownActions = ActionManager.INSTANCE.getKnownActions();
			String[] values = knownActions.keySet().toArray(new String[knownActions.size()]);
			String[] entries = knownActions.values().toArray(new String[knownActions.size()]);
			preference.setEntryValues(values);
			preference.setEntries(entries);
			category.addPreference(preference);
//				preferenceScreen.addPreference(preference);
		}
	}

	public static class MiscActivity extends PreferenceActivity {
		@SuppressWarnings("deprecation")
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_misc);
		}
	}

	public static class MiscFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_misc);
		}
	}

	public static class AboutActivity extends Activity {
		@Override
		public void onCreate(Bundle state) {
			super.onCreate(state);

			WebView view = new WebView(this);
			view.getSettings().setJavaScriptEnabled(true);
			view.loadUrl("file:///android_asset/about.html");
			view.setBackgroundColor(Color.TRANSPARENT);
			setContentView(view);
		}
	}

	public static class AboutFragment extends WebViewFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			WebView view = (WebView) super.onCreateView(inflater, container,
					savedInstanceState);
			view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			view.getSettings().setJavaScriptEnabled(true);
			view.loadUrl("file:///android_asset/about.html");
			view.setBackgroundColor(Color.TRANSPARENT);
			return view;
		}
	}
}
