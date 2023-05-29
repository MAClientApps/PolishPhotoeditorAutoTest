package com.autotest.photo.editor.polish;

public class PolishSettings {
    private boolean isClearViewsEnabled;
    private boolean isTransparencyEnabled;

    public boolean isTransparencyEnabled() {
        return this.isTransparencyEnabled;
    }

    public boolean isClearViewsEnabled() {
        return this.isClearViewsEnabled;
    }

    private PolishSettings(Builder builder) {
        this.isClearViewsEnabled = builder.isClearViewsEnabled;
        this.isTransparencyEnabled = builder.isTransparencyEnabled;
    }

    public static class Builder {

        public boolean isClearViewsEnabled = true;

        public boolean isTransparencyEnabled = true;

        public PolishSettings build() {
            return new PolishSettings(this);
        }
    }
}
