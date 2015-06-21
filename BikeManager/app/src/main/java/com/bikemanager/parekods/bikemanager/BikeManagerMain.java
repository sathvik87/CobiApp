package com.bikemanager.parekods.bikemanager;
/**
 * @Author Sathvik Parekodi
 *
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.SoundPool;
import android.opengl.Visibility;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class BikeManagerMain extends Activity {

    private Bitmap imageOriginal, imageScaled;
    private static Matrix matrix;
    private ImageView dialer;
    private int dialerHeight, dialerWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_manager_main);

        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.needle);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        TextView speedView = (TextView) findViewById(R.id.major_val);
        dialer = (ImageView) findViewById(R.id.direction);
        final DiallerOnTouchListener touchListener = new DiallerOnTouchListener(speedView);

        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = dialer.getHeight();
                    dialerWidth = dialer.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                    // translate to the image view's center
                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);

                    dialer.setImageBitmap(imageScaled);
                    dialer.setImageMatrix(matrix);

                    touchListener.setParameters(dialerWidth, dialerHeight, matrix, dialer, imageScaled);
                    dialer.setOnTouchListener(touchListener);
                }
            }
        });


        final RelativeLayout leftPanel = (RelativeLayout) findViewById(R.id.left_panel);
        leftPanel.startAnimation(CustomAnimationUtils.getSlideFromLeftToRightAnimation());

        final RelativeLayout rightPanel = (RelativeLayout) findViewById(R.id.right_panel);
        rightPanel.startAnimation(CustomAnimationUtils.getSlideFromRightToLeftAnimation());

        final TextView majorVal = (TextView) findViewById(R.id.major_val);
        final TextView minorVal = (TextView) findViewById(R.id.minor_val);
        majorVal.startAnimation(CustomAnimationUtils.getBlinkingAnimation());
        minorVal.startAnimation(CustomAnimationUtils.getBlinkingAnimation());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bike_manager_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
