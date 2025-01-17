package currency.recognize.currencyrecog;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class CreateCurrencyActivity extends Activity {

    private static final float LENGTH_THRESHOLD = 120.0f;
    private Gesture mGesture;
    private View mDoneButton;
    private Gesture gd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        mDoneButton = findViewById(R.id.done);
        GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
        overlay.addOnGestureListener(new GesturesProcessor());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mGesture != null) {
            outState.putParcelable("gesture", mGesture);
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGesture = savedInstanceState.getParcelable("gesture");
        if (mGesture != null) {
            final GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
            overlay.post(new Runnable() {
                public void run() {
                    overlay.setGesture(mGesture);
                }
            });
            mDoneButton.setEnabled(true);
        }
    }
    @SuppressWarnings({"UnusedDeclaration"})
    public void addGesture(View v) {
        if (mGesture != null) {
            final TextView input = (TextView) findViewById(R.id.gesture_name);
            final CharSequence name = input.getText();

            if (name.length() == 0) {
                input.setError(getString(R.string.error_missing_name));
                return;
            }
            final GestureLibrary store = CurrencyBuilderActivity.getStore();
            store.addGesture(name.toString(), mGesture);
            store.save();
            setResult(RESULT_OK);
            final String path = new File(Environment.getExternalStorageDirectory(),"gestures").getAbsolutePath();
            Toast.makeText(this, getString(R.string.save_success, path), Toast.LENGTH_LONG).show();
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void cancelGesture(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
    private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            mDoneButton.setEnabled(false);
            mGesture = null;
        }
        public void onGesture(GestureOverlayView overlay, MotionEvent event) {

        }
        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {

            mGesture = overlay.getGesture();
            if (mGesture.getLength() < LENGTH_THRESHOLD) {
                overlay.clear(false);
            }
            mDoneButton.setEnabled(true);
        }
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }
    }
}
