package com.autotest.photo.editor.utils;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.layer.slant.SlantLayoutHelper;
import com.autotest.photo.editor.layer.straight.StraightLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CollageUtils {

    private CollageUtils() {}

    public static List<PolishLayout> getCollageLayouts(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(SlantLayoutHelper.getAllThemeLayout(i));
        arrayList.addAll(StraightLayoutHelper.getAllThemeLayout(i));
        return arrayList;
    }
}
