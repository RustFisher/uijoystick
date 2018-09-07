package com.rustfisher.uijoystick.business;

/**
 * 圆形计算器
 */
public class RoundCalculator {

    /**
     * @return 计算(x1, y1)和(x0, y0)两点之间的直线距离
     */
    public static double calTwoPointDistant(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow((x1 - x0), 2) + Math.pow((y1 - y0), 2));
    }

    /**
     * @return 计算(x1, y1)相对于(x0, y0)为圆点的圆的角度[0, 360]
     */
    public static double calTwoPointAngleDegree(double x0, double y0, double x1, double y1) {
        double z = calTwoPointDistant(x0, y0, x1, y1);
        double angle = Math.asin(Math.abs(y1 - y0) / z) * 180 / Math.PI;
        if (x1 < x0 && y1 < y0) {
            angle = 180 - angle;
        } else if (x1 < x0 && y1 >= y0) {
            angle = 180 + angle;
        } else if (x1 >= x0 && y1 >= y0) {
            angle = 360 - angle;
        }
        return angle;
    }

    /**
     * (x0, y0)为圆心
     * (x1, y1)为触摸点
     *
     * @param limitRadius 滚动球的圆心能移动的最大范围  限制滚动球（圆心）的移动半径
     * @return 计算得到滚动球的圆心坐标
     */
    public static double[] calPointLocationByAngle(double x0, double y0, double x1, double y1, double limitRadius) {
        double angle = RoundCalculator.calTwoPointAngleDegree(x0, y0, x1, y1);

        float x = (float) (x0 + limitRadius * Math.cos(angle * Math.PI / 180));
        float y = (float) (y0 - limitRadius * Math.sin(angle * Math.PI / 180));

        return new double[]{x, y};
    }
}
