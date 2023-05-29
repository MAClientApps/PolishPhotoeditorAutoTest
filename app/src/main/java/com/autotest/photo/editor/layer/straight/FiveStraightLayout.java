package com.autotest.photo.editor.layer.straight;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class FiveStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 17;
    }

    public FiveStraightLayout() {
    }

    public FiveStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public FiveStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.VERTICAL, 0.25f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.6666667f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.6f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                addLine(3, PolishLine.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addLine(0, PolishLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 3:
                addLine(0, PolishLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(1, 3, PolishLine.Direction.HORIZONTAL);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.75f);
                cutAreaEqualPart(1, 4, PolishLine.Direction.VERTICAL);
                return;
            case 5:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.25f);
                cutAreaEqualPart(0, 4, PolishLine.Direction.VERTICAL);
                return;
            case 6:
                addLine(0, PolishLine.Direction.VERTICAL, 0.75f);
                cutAreaEqualPart(1, 4, PolishLine.Direction.HORIZONTAL);
                return;
            case 7:
                addLine(0, PolishLine.Direction.VERTICAL, 0.25f);
                cutAreaEqualPart(0, 4, PolishLine.Direction.HORIZONTAL);
                return;
            case 8:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(3, PolishLine.Direction.VERTICAL, 0.5f);
                return;
            case 9:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.4f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                cutAreaEqualPart(2, 3, PolishLine.Direction.VERTICAL);
                return;
            case 10:
                addCross(0, 0.33333334f);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 11:
                addCross(0, 0.6666667f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 12:
                addCross(0, 0.33333334f, 0.6666667f);
                addLine(3, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 13:
                addCross(0, 0.6666667f, 0.33333334f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 14:
                cutSpiral(0);
                return;
            case 15:
                cutAreaEqualPart(0, 5, PolishLine.Direction.HORIZONTAL);
                return;
            case 16:
                cutAreaEqualPart(0, 5, PolishLine.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 5, PolishLine.Direction.HORIZONTAL);
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new FiveStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
