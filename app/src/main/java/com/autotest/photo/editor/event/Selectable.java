package com.autotest.photo.editor.event;

import com.autotest.photo.editor.entity.Photo;

public interface Selectable {

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

}
