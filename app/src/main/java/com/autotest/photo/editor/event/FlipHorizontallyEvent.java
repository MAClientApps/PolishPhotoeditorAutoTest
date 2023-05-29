package com.autotest.photo.editor.event;

public class FlipHorizontallyEvent extends AbstractFlipEvent {
    protected int getFlipDirection() {
        return 1;
    }
}
