package eugene.preferencebeans;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eugene.gestures.notification.Shouter;

public class PreferenceBean {
    private Context context;
    private BeanBackedMap mapView;

    public void init(Context context) {
        this.context = context;
        load();
    }

    private BeanBackedMap getMapView() {
        if (mapView == null) {
            Set<String> excludeGetters = new HashSet<String>();
            for (Method method : PreferenceBean.class.getDeclaredMethods()) {
                String name = method.getName();
                if (name.startsWith("get") || name.startsWith("is")) {
                    excludeGetters.add(name);
                }
            }
            mapView = new BeanBackedMap(this, excludeGetters);
        }
        return mapView;
    }

    public void load() {
        for (Map.Entry<String, Object> entry : getMapView().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String preferenceName = getFullPreferenceName(key);
            Class<?> type = getMapView().getType(key);
            Object persistentValue = getFromPreferences(key, type, value);
            Shouter.shout(key + " = " + persistentValue);
            entry.setValue(persistentValue);
        }
    }

    public boolean commit() {
        SharedPreferences.Editor editor = getPreferences().edit();
        for (Map.Entry<String, Object> entry : getMapView().entrySet()) {
            putToPreferenceEditor(editor, entry.getKey(), entry.getValue(),
                    getMapView().getType(entry.getKey()));
        }
        return editor.commit();
    }

    private void putToPreferenceEditor(SharedPreferences.Editor editor, String key,
                                       Object value, Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            editor.putString(key, (String) value);
        } else {
            throw new IllegalArgumentException("Dont know how to retrieve " +
                    type.getName() + " from preferences");
        }
    }

    private Object getFromPreferences(String key, Class<?> type, Object defaultValue) {
        if (type.isAssignableFrom(String.class)) {
            return getPreferences().getString(key, (String) defaultValue);
        } else {
            throw new IllegalArgumentException("Dont know how to retrieve " +
                    type.getName() + " from preferences");
        }
    }

    public SharedPreferences getPreferences() {
        ensureState();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void ensureState() {
        if (context == null) {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return getMapView().toString();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPrefix() {
        return getClass().getSimpleName() + "_";
    }

    public String getSuffix() {
        return "";
    }

    public String getFullPreferenceName(String fieldName) {
        return getPrefix() + fieldName + getSuffix();
    }
}
