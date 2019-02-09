package currency.recognize.currencyrecog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    //hey there
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    static {
       System.loadLibrary("native-lib");
        if (OpenCVLoader.initDebug()){

            Log.d(TAG,"Loaded");
        }
        else {

            Log.d(TAG,"Not Loaded");
        }
    }


    public void mainemthod() {

    }

    public void s()
    {}
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(MainActivity.this, "Hey Hi", Toast.LENGTH_SHORT).show();
    }
}
