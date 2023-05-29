package com.autotest.photo.editor.layer.slant;

import com.autotest.photo.editor.polish.grid.PolishLayout;

import java.util.ArrayList;
import java.util.List;

public class SlantLayoutHelper {
    private SlantLayoutHelper() {
    }

    public static List<PolishLayout> getAllThemeLayout(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        switch (i) {
            case 1:
                while (i2 < 4) {
                    arrayList.add(new OneSlantLayout(i2));
                    i2++;
                }
                break;
            case 2:
                while (i2 < 2) {
                    arrayList.add(new TwoSlantLayout(i2));
                    i2++;
                }
                break;
            case 3:
                while (i2 < 6) {
                    arrayList.add(new ThreeSlantLayout(i2));
                    i2++;
                }
                break;
            case 4:
                while (i2 < 7) {
                    arrayList.add(new FourSlantLayout(i2));
                    i2++;
                }
                break;
            case 6:
                while (i2 < 2) {
                    arrayList.add(new SixSlantLayout(i2));
                    i2++;
                }
                break;
            case 7:
                while (i2 < 1) {
                    arrayList.add(new SevenSlantLayout(i2));
                    i2++;
                }
                break;
        }
        return arrayList;
    }
}
