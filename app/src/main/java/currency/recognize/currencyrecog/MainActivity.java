package currency.recognize.currencyrecog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends BaseActivity {
    private static final String TAG="MainActivity";
    private Button btnChangePassword, btnRemoveUser, changePassword, signOut;
    private TextView email;
    private EditText newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    //hey there
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(6).setChecked(true);

        auth = FirebaseAuth.getInstance();
        email = (TextView) findViewById(R.id.useremail);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        changePassword = (Button) findViewById(R.id.changePass);
        signOut = (Button) findViewById(R.id.sign_out);

        newPassword = (EditText) findViewById(R.id.newPassword);
        newPassword.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.VISIBLE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                            alertDialog.setTitle("Done!");
                                            alertDialog.setMessage("Password is updated, sign in with new password!");
                                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int which) {
                                                    signOut();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                            alertDialog.show();
                                        } else {

                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                            alertDialog.setTitle("Error");
                                            alertDialog.setMessage("Failed to update password! "+task.getException());
                                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int which) {
                                                    signOut();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("Done!");
                                        alertDialog.setMessage("Your profile is deleted:");
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {
                                                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                                finish();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                        alertDialog.show();
                                    } else {

                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("Done!");
                                        alertDialog.setMessage("Failed to Delete Profile");
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

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


    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {

        email.setText("User Email: " + user.getEmail());

    }
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                setDataToView(user);

            }
        }


    };

    //sign out method
    public void signOut() {
        auth.signOut();

        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
