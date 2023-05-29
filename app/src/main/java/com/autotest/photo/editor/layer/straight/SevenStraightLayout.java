package com.autotest.photo.editor.layer.straight;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class SevenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 9;
    }

    public SevenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public SevenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 4, PolishLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                return;
            case 1:
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                cutAreaEqualPart(1, 4, PolishLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
            case 2:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 1, 2);
                return;
            case 3:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.6666667f);
                cutAreaEqualPart(1, 3, PolishLine.Direction.VERTICAL);
                addCross(0, 0.5f);
                return;
            case 4:
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 3, PolishLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
            case 5:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.75f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                return;
            case 6:
                addLine(0, PolishLine.Direction.VERTICAL, 0.6666667f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PolishLine.Direction.VERTICAL, 0.5f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.4f);
                cutAreaEqualPart(0, 3, PolishLine.Direction.HORIZONTAL);
                return;
            case 7:
                addLine(0, PolishLine.Direction.VERTICAL, 0.25f);
                addLine(1, PolishLine.Direction.VERTICAL, 0.6666667f);
                addLine(2, PolishLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 8:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, PolishLine.Direction.HORIZONTAL, 0.6666667f);
                cutAreaEqualPart(2, 3, PolishLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 3, PolishLine.Direction.VERTICAL);
                return;
            default:
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new SevenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
