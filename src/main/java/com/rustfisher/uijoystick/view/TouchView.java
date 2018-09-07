package com.rustfisher.uijoystick.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import org.jetbrains.annotations.Nullable;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rustfisher.uijoystick.business.RoundCalculator;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;
import com.rustfisher.uijoystick.model.PadLocationType;
import com.rustfisher.uijoystick.model.PadStyle;
import com.rustfisher.uijoystick.model.TouchViewModel;

/**
 * 触摸式控制盘
 * 假设背景是一个圆盘
 * 圆盘中有一个小圆球
 * 圆盘外围绕着一个箭头
 */
public class TouchView extends View {
    private static final String TAG = "rustAppTouchView";

    private Bitmap bgBmp;        // 视图背景图片  假设是一个圆盘
    private Bitmap touchBmp;     // 视图中间的随手指移动的图片  假设是一个圆球
    private Bitmap mDirectionBmp;// 指示方向的图片  假设是一个箭头  整体是一个正方形的图片
    private boolean shouldShowDirectionBmp; // 是否显示方向指示图片
    private PadStyle mPadStyle = PadStyle.FIXED; // 默认为固定位置的
    private PadLocationType mPadLocationType = PadLocationType.LEFT_BOT;

    private JoystickTouchViewListener jListener;

    private float mRoundBgPadding = 20;// 背景圆到view边界的像素
    protected float mWholeViewWid;
    protected float mWholeViewHeight;// 整个View的高
    private float mWholePadWid;    // 盘的宽度，包括箭头；并不是View的总宽度
    private float mWholePadHeight; // 盘的高度，包括箭头；并不是View的总宽度

    private int mRoundBgRadius;

    protected float touchImageX;
    protected float touchImageY;

    private float touchBmpDefaultX; // 滚动球图片默认左上角x
    private float touchBmpDefaultY; // 滚动球图片默认左上角y

    private boolean isMoving;

    private ValueAnimator valueAnimatorResetX;
    private ValueAnimator valueAnimatorResetY;

    private float mContentCenterX; // 控制盘中心点x坐标
    private float mContentCenterY; // 控制盘中心点y坐标

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 初始化
     * 控制盘整体宽高
     * 触摸球的半径
     */
    public void init(TouchViewModel model) {
        mPadStyle = model.getPadStyle();
        mPadLocationType = model.getPadLocationType();
        Bitmap tmpBgBmp = BitmapFactory.decodeResource(getResources(), model.getBgResId());
        Bitmap tmpTouchBmp = BitmapFactory.decodeResource(getResources(), model.getTouchBmpResId());
        this.mRoundBgPadding = model.getRoundBgPadding();

        mWholeViewHeight = model.getWholeViewHeight();
        mWholeViewWid = model.getWholeViewWid();
        mWholePadWid = model.getWholePadWid();
        mWholePadHeight = model.getWholePadHeight();
        mRoundBgRadius = model.getRoundBgRadius();

        isMoving = false;

        shouldShowDirectionBmp = model.isShowDirectionPic();
        if (shouldShowDirectionBmp) {
            Bitmap tmpDirectionBmp = BitmapFactory.decodeResource(getResources(), model.getDirectionPicResId());
            mDirectionBmp = Bitmap.createScaledBitmap(tmpDirectionBmp,
                    (int) mWholePadWid, (int) mWholePadHeight, true);
        }

        bgBmp = Bitmap.createScaledBitmap(tmpBgBmp, (int) (mRoundBgRadius - mRoundBgPadding) * 2, (int) (mRoundBgRadius - mRoundBgPadding) * 2, true);
        touchBmp = Bitmap.createScaledBitmap(tmpTouchBmp,
                model.getTouchBallRadius() * 2, model.getTouchBallRadius() * 2, true);

        setupContentCenter();
        touchBmpDefaultX = mContentCenterX - touchBmp.getWidth() / 2;
        touchBmpDefaultY = mContentCenterY - touchBmp.getWidth() / 2;
        touchImageX = touchBmpDefaultX;
        touchImageY = touchBmpDefaultY;
    }

    // 设定初始位置
    private void setupContentCenter() {
        switch (mPadLocationType) {
            case LEFT_BOT:
                mContentCenterX = mWholePadWid / 2;
                mContentCenterY = mWholeViewHeight - mWholePadHeight / 2;
                break;
            case RIGHT_BOT:
                mContentCenterX = mWholeViewWid - mWholePadWid / 2;
                mContentCenterY = mWholeViewHeight - mWholePadHeight / 2;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bgBmp != null && getWidth() > 0) {
            // 画透明边框以确定Pad的大小
//            canvas.drawRect(0, 0, mWholeViewWid, mWholeViewHeight, viewRectPaint);

            // 画背景圆
            canvas.drawBitmap(bgBmp, mContentCenterX - bgBmp.getWidth() / 2,
                    mContentCenterY - bgBmp.getHeight() / 2, null);

            if (shouldShowDirectionBmp && touchBmpDefaultX != touchImageX && touchBmpDefaultY != touchImageY) {
                // 画方向指示箭头
                float rotationDegree = (float) RoundCalculator.calTwoPointAngleDegree(mContentCenterX, mContentCenterY,
                        touchImageX + touchBmp.getWidth() / 2, touchImageY + touchBmp.getWidth() / 2);
                drawRotateBitmap(canvas, mDirectionBmp, 180 - rotationDegree, mContentCenterX - mWholePadWid / 2, mContentCenterY - mWholePadHeight / 2);
            }

            // 画中心控制圆圈
            canvas.drawBitmap(touchBmp, touchImageX, touchImageY, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isMoving = false;
            setupContentCenter();
            reset();
            if (null != jListener) {
                jListener.onActionUp();
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (mPadStyle) {
                case FLOATING:
                    if (event.getX() > mWholeViewWid / 2 + mWholePadWid / 2) {
                        return false; // 为了让pad移动后显示完整  保证它在合适的范围里
                    } else if (event.getY() < mWholePadHeight / 2) {
                        return false;
                    }
                    // 根据点击位置重置控制盘中心位置
                    mContentCenterX = event.getX();
                    mContentCenterY = event.getY();
                    isMoving = true;
                    userMoving(event);
                    break;
                case FIXED:
                    if (event.getX() < mContentCenterX - mWholePadWid / 2 || event.getX() > mContentCenterX + mWholePadWid / 2) {
                        return false; // 点击在圆盘外面的不处理
                    } else if (event.getY() < mContentCenterY - mWholePadHeight / 2 || event.getY() > mContentCenterY + mWholePadHeight / 2) {
                        return false;
                    }
                    isMoving = true;  // 直接移动圆球到点击位置
                    userMoving(event);
                    if (null != jListener) {
                        jListener.onActionDown();
                    }
                    break;
            }
        } else if (isMoving) {
            userMoving(event);
        }
        return true;
    }

    private void userMoving(MotionEvent event) {
        if (valueAnimatorResetX != null && valueAnimatorResetY != null) {
            valueAnimatorResetX.removeAllUpdateListeners();
            valueAnimatorResetY.removeAllUpdateListeners();
        }

        float tr = (float) RoundCalculator.calTwoPointDistant(mContentCenterX, mContentCenterY, event.getX(), event.getY());
        double insideBgDis = (bgBmp.getWidth() - touchBmp.getWidth()) / 2;
        if (tr <= insideBgDis) {
            // 点击在背景圆圈内
            onBallMove(event.getX(), event.getY());
        } else {
            // 点击后拖出了边界  计算出拖动圆的圆心坐标
            double dotCenterOnShow[] = RoundCalculator.calPointLocationByAngle(
                    mContentCenterX, mContentCenterY, event.getX(), event.getY(), insideBgDis);
            onBallMove((float) dotCenterOnShow[0], (float) dotCenterOnShow[1]);
        }
    }

    protected void onBallMove(float ballCenterX, float ballCenterY) {
        touchImageX = ballCenterX - touchBmp.getWidth() / 2;
        touchImageY = ballCenterY - touchBmp.getHeight() / 2;

        invalidate();

        if (jListener != null) {
            float horizontalPercent = (ballCenterX - mContentCenterX) / (bgBmp.getWidth() - touchBmp.getWidth()) * 2.0f;
            float verticalPercent = (mContentCenterY - ballCenterY) / (bgBmp.getHeight() - touchBmp.getHeight()) * 2.0f;
            jListener.onTouch(horizontalPercent, verticalPercent);
        }
    }

    public void setPadStyle(PadStyle mPadStyle) {
        this.mPadStyle = mPadStyle;
    }

    protected void reset() {
        valueAnimatorResetX = new ValueAnimator();
        valueAnimatorResetX.setFloatValues(touchImageX, touchBmpDefaultX);
        valueAnimatorResetX.setDuration(200);
        valueAnimatorResetX.start();
        valueAnimatorResetX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                touchImageX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimatorResetY = new ValueAnimator();
        valueAnimatorResetY.setFloatValues(touchImageY, touchBmpDefaultY);
        valueAnimatorResetY.setDuration(200);
        valueAnimatorResetY.start();
        valueAnimatorResetY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                touchImageY = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        if (jListener != null) {
            jListener.onReset();
        }
    }

    public void setListener(JoystickTouchViewListener listener) {
        this.jListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled && isMoving) {
            isMoving = false;
            reset();
        }
    }

    /**
     * @param canvas   画布
     * @param bitmap   要绘制的bitmap
     * @param rotation 旋转角度
     * @param posX     左上角顶点的x值 - left
     * @param posY     左上角顶点的y值 - top
     */
    private static void drawRotateBitmap(Canvas canvas, Bitmap bitmap,
                                         float rotation, float posX, float posY) {
        Matrix matrix = new Matrix();
        int offsetX = bitmap.getWidth() / 2;
        int offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
