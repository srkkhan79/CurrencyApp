package currency.recognize.currencyrecog;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class permission extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if(!isConnected(permission.this)) {
            buildDialog(permission.this).show();
        }
        else
        {
            if(android.os.Build.VERSION.SDK_INT>=23) {
                if (checkPermission()) {
                    Intent intent=new Intent(permission.this,WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    requestPermission();
                }
            }
            else {
                try {
                    Intent intent=new Intent(permission.this,WelcomeActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.e("ASK", ex.getMessage());
                }
            }
        }
    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(permission.this, new String[]
                {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                }, RequestPermissionCode);

    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if (CameraPermission && ReadContactsPermission && ReadPhoneStatePermission) {
                        Intent intent=new Intent(permission.this,WelcomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        requestPermission();
                        Toast.makeText(permission.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

}
