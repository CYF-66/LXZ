package com.xx.lxz.widget.swipetoloadlayout;

/**
 * Created by cyf on 17/12/29.
 */
public interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}
