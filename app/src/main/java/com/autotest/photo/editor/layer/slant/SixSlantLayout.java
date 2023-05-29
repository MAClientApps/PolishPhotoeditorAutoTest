package com.autotest.photo.editor.layer.slant;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class SixSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 2;
    }

    public SixSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public SixSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.VERTICAL, 0.33333334f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.7f, 0.7f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.5f, 0.5f);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.3f, 0.3f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.3f, 0.3f);
                addLine(2, PolishLine.Direction.VERTICAL, 0.5f, 0.5f);
                addLine(4, PolishLine.Direction.VERTICAL, 0.7f, 0.7f);
                return;
            default:
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new SixSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
