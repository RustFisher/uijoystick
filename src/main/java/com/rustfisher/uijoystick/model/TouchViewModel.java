package com.rustfisher.uijoystick.model;

/**
 * 控制盘的模型
 */
public class TouchViewModel {

    private int bgResId;           // 背景图片资源ID
    private int touchBmpResId;     // 触摸图资源ID - 例如一个圆球
    private int directionPicResId; // 指示当前触摸点与圆心相对方向的图片ID

    private float mWholeViewWid;    // 整个View的宽
    private float mWholeViewHeight; // 整个View的高
    private float mWholePadWid;    // 盘的宽度，包括箭头；并不是View的总宽度
    private float mWholePadHeight; // 盘的高度，包括箭头；并不是View的总宽度

    private int mRoundBgRadius;    // 背景圆的半径 背景圆位置可以变化
    private int mTouchBallRadius = 100; // 触摸球的半径
    private int mRoundBgPadding;   // 背景圆到Pad边界的px  一般是留给方向箭头的位置

    private boolean showDirectionPic = false;    // 是否显示指示图片
    private PadStyle mPadStyle = PadStyle.FIXED; // 默认为固定位置的
    private PadLocationType mPadLocationType = PadLocationType.LEFT_BOT;

    public TouchViewModel(int bgResId, int touchPicResId) {
        this.bgResId = bgResId;
        this.touchBmpResId = touchPicResId;
    }

    // 设置整个View的宽高  控制盘可以在这个范围内活动
    public void setWholeViewSize(float wid, float height) {
        mWholeViewWid = wid;
        mWholeViewHeight = height;
    }

    // 设置控制盘的尺寸
    public void setPadSize(float wid, float height) {
        mWholePadWid = wid;
        mWholePadHeight = height;
    }

    // 设置背景和触摸球的半径
    public void setContentSize(int roundBgRadius, int touchBallRadius) {
        mRoundBgRadius = roundBgRadius;
        mTouchBallRadius = touchBallRadius;
    }

    public int getTouchBallRadius() {
        return mTouchBallRadius;
    }

    public void setStyle(PadStyle padStyle, PadLocationType padLocationType) {
        this.mPadStyle = padStyle;
        this.mPadLocationType = padLocationType;
    }

    public void setTouchBallRadius(int mTouchBallRadius) {
        this.mTouchBallRadius = mTouchBallRadius;
    }

    public boolean isShowDirectionPic() {
        return showDirectionPic;
    }

    public int getDirectionPicResId() {
        return directionPicResId;
    }

    public void setDirectionPicResId(int directionPicResId) {
        this.directionPicResId = directionPicResId;
        showDirectionPic = true;
    }

    public int getRoundBgPadding() {
        return mRoundBgPadding;
    }

    public void setRoundBgPadding(int px) {
        this.mRoundBgPadding = px;
    }

    public int getBgResId() {
        return bgResId;
    }

    public int getTouchBmpResId() {
        return touchBmpResId;
    }

    public float getWholeViewWid() {
        return mWholeViewWid;
    }

    public float getWholeViewHeight() {
        return mWholeViewHeight;
    }

    public PadStyle getPadStyle() {
        return mPadStyle;
    }

    public PadLocationType getPadLocationType() {
        return mPadLocationType;
    }

    public float getWholePadHeight() {
        return mWholePadHeight;
    }

    public float getWholePadWid() {
        return mWholePadWid;
    }

    public int getRoundBgRadius() {
        return mRoundBgRadius;
    }
}
