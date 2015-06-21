package com.bikemanager.parekods.bikemanager;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by parekods on 20.06.2015.
 */
public class DiallerOnTouchListener implements View.OnTouchListener {

    private double startAngle;
    private int dialerHeight, dialerWidth;
    private Bitmap imageScaled;
    private Matrix matrix;
    private ImageView dialer;

    private TextView speedView;
    double currentAngle;

    int rotateCount= 0;

    public DiallerOnTouchListener(TextView speedView) {
        this.speedView = speedView;
    }

    public void setParameters(int dialerWidth, int dialerHeight, Matrix matrix, ImageView dialer, Bitmap imageScaled) {
        this.dialerWidth = dialerWidth;
        this.dialerHeight = dialerHeight;
        this.matrix = matrix;
        this.dialer = dialer;
        this.imageScaled = imageScaled;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startAngle = getAngle(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                currentAngle = getAngle(event.getX(), event.getY());
                rotateDialer((float) (startAngle - currentAngle));
                startAngle = currentAngle;
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    /**
     * @return The angle of the unit circle with the image view's center
     */
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    /**
     * Rotate the dialer.
     *
     * @param degrees The degrees, the dialer should get rotated.
     */
    private void rotateDialer(float degrees) {

        rotateCount ++;
        matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);
        dialer.setImageMatrix(matrix);

        if(speedView != null) {
            speedView.setText(""+ rotateCount);
        }

    }

}