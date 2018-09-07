package com.rustfisher.uijoystick.listener;


public interface JoystickTouchViewListener {
    void onTouch(float horizontalPercent, float verticalPercent);
    void onReset();

    /**
     * 按下
     */
    void onActionDown();

    /**
     * 手指抬起
     */
    void onActionUp();
}
