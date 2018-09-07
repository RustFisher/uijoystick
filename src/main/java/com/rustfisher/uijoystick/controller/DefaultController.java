package com.rustfisher.uijoystick.controller;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.rustfisher.uijoystick.R;
import com.rustfisher.uijoystick.listener.JoystickTouchViewListener;
import com.rustfisher.uijoystick.model.PadLocationType;
import com.rustfisher.uijoystick.model.PadStyle;
import com.rustfisher.uijoystick.model.TouchViewModel;
import com.rustfisher.uijoystick.view.TouchView;

/**
 * 触摸式控制器
 */
public class DefaultController implements IJoystickController {
    private Context ctx;
    private RelativeLayout containerView;
    private PadStyle padStyle = PadStyle.FIXED;

    private TouchView leftControlTouchView;
    private TouchView rightControlTouchView;

    /**
     * @param context       需要关联context获取资源文件
     * @param containerView 父view
     */
    public DefaultController(Context context, RelativeLayout containerView) {
        this(context, containerView, PadStyle.FIXED);
    }

    public DefaultController(Context context, RelativeLayout containerView, PadStyle padStyle) {
        this.ctx = context;
        this.containerView = containerView;
        this.padStyle = padStyle;
    }

    @Override
    public void createViews() {
        createLeftControlTouchView();
        containerView.addView(leftControlTouchView);

        createRightControlTouchView();
        containerView.addView(rightControlTouchView);
    }

    @Override
    public void showViews(boolean showAnimation) {
        leftControlTouchView.clearAnimation();
        leftControlTouchView.setVisibility(View.VISIBLE);

        rightControlTouchView.clearAnimation();
        rightControlTouchView.setVisibility(View.VISIBLE);
    }

    private void createLeftControlTouchView() {
        TouchViewModel model = new TouchViewModel(
                R.drawable.ui_pic_joystick_left_pad,
                R.drawable.ui_pic_joystick_control_ball);
        model.setWholeViewSize(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        model.setPadSize(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size));
        int roundBgRadius = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_round_bg_radius);
        model.setContentSize(roundBgRadius, (int) (roundBgRadius / 3.5));
        model.setStyle(padStyle, PadLocationType.LEFT_BOT);
        model.setRoundBgPadding(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));

        leftControlTouchView = new TouchView(ctx);
        leftControlTouchView.init(model);

        // View的总大小
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height)
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftControlTouchView.setLayoutParams(params);
    }

    private void createRightControlTouchView() {
        TouchViewModel model = new TouchViewModel(
                R.drawable.ui_pic_joystick_right_pad,
                R.drawable.ui_pic_joystick_control_ball);
        model.setWholeViewSize(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        model.setPadSize(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size));
        int roundBgRadius = ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_round_bg_radius);
        model.setContentSize(roundBgRadius, (int) (roundBgRadius / 3.5));
        model.setDirectionPicResId(R.drawable.ui_pic_joystick_arrow);
        model.setStyle(padStyle, PadLocationType.RIGHT_BOT);

        model.setRoundBgPadding(ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));
        rightControlTouchView = new TouchView(ctx);
        rightControlTouchView.init(model);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid),
                ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height)
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightControlTouchView.setLayoutParams(params);

    }

    /**
     * 必须先初始化view
     */
    public void setLeftTouchViewListener(JoystickTouchViewListener leftTouchViewListener) {
        if (null != leftControlTouchView) {
            leftControlTouchView.setListener(leftTouchViewListener);
        }
    }

    public void setRightTouchViewListener(JoystickTouchViewListener rightTouchViewListener) {
        if (null != rightControlTouchView) {
            rightControlTouchView.setListener(rightTouchViewListener);
        }
    }

    public void setPadStyle(PadStyle padStyle) {
        this.padStyle = padStyle;
        leftControlTouchView.setPadStyle(padStyle);
        rightControlTouchView.setPadStyle(padStyle);
    }

    public PadStyle getPadStyle() {
        return padStyle;
    }
}
