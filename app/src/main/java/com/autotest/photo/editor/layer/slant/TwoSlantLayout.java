package com.autotest.photo.editor.layer.slant;

import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLine;

public class TwoSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 2;
    }


    public TwoSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public TwoSlantLayout(int i) {
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
            default:
                return;
        }
    }

    public PolishLayout clone(PolishLayout quShotLayout) {
        return new TwoSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
