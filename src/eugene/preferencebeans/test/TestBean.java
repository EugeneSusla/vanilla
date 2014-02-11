package eugene.preferencebeans.test;

import android.content.Context;

import eugene.preferencebeans.PreferenceBean;

public class TestBean extends PreferenceBean {
    private String hasDefault = "default";
    private String changed = "initial";
    private String noDefault;

    public void test() {
        setChanged("custom");
        commit();
    }

    public String getHasDefault() {
        return hasDefault;
    }

    public void setHasDefault(String hasDefault) {
        this.hasDefault = hasDefault;
    }

    public String getNoDefault() {
        return noDefault;
    }

    public void setNoDefault(String noDefault) {
        this.noDefault = noDefault;
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }
}
