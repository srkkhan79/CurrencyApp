package currency.recognize.currencyrecog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity
{

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Not Allowed",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.startup));
        }

        Thread thread=new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent i=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
