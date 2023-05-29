package com.autotest.photo.editor.crop;

public interface Easing {
    double easeIn(double d, double d2, double d3, double d4);

    double easeInOut(double d, double d2, double d3, double d4);

    double easeOut(double d, double d2, double d3, double d4);
}
