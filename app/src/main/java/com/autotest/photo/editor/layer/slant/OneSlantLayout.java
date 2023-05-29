package com.autotest.photo.editor.layer.slant;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class OneSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 4;
    }

    public OneSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public OneSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PolishLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, PolishLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addCross(0, 0.56f, 0.44f, 0.56f, 0.44f);
                return;
            case 3:
                cutArea(0, 1, 2);
                return;
            default:
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new OneSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
