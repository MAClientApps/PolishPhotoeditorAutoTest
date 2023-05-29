package com.autotest.photo.editor.layer.straight;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class NineStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 8;
    }

    public NineStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public NineStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 2, 2);
                return;
            case 1:
                addLine(0, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 4, PolishLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 4, PolishLine.Direction.HORIZONTAL);
                return;
            case 2:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(2, 4, PolishLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 4, PolishLine.Direction.VERTICAL);
                return;
            case 3:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(2, 3, PolishLine.Direction.VERTICAL);
                addLine(1, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                return;
            case 4:
                addLine(0, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 3, PolishLine.Direction.HORIZONTAL);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
            case 5:
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(1, 3, PolishLine.Direction.HORIZONTAL);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 6:
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                addLine(2, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(2, PolishLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(1, 3, PolishLine.Direction.VERTICAL);
                addLine(0, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 7:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 1, 3);
                return;
            default:
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new NineStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
