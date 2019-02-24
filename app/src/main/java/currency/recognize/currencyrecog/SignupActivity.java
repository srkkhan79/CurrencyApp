package currency.recognize.currencyrecog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputname,inputphoneno;     //hit option + enter if you on mac , for windows hit ctrl + enter
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
//    SendProfile sendProfile;
//    String app_server_url="https://askajay3.000webhostapp.com/fcm_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputname = (EditText) findViewById(R.id.name);
        inputphoneno = (EditText) findViewById(R.id.phoneno);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        firebaseDatabase=firebaseDatabase.getInstance();

      //  sendProfile=new SendProfile();
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(new validationClass(inputEmail.getText().toString()).checkemail())
                    {

                        inputEmail.setTextColor(Color.parseColor("WHITE"));
                    }
                    else
                    {
                        inputEmail.setError("Invalid Email Address");
                        inputEmail.setTextColor(Color.parseColor("RED"));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputphoneno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(new validationClass(inputphoneno.getText().toString()).checkmobileno())
                    {
                            inputphoneno.setTextColor(Color.parseColor("WHITE"));
                    }
                    else
                    {
                        inputphoneno.setError("Invalid Mobile No");
                        inputphoneno.setTextColor(Color.parseColor("RED"));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)||!new validationClass(inputEmail.getText().toString()).checkemail() ) {
                    inputEmail.setError("Invalid Email address!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter Your Password!");
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too Short!");
                    return;
                }
                if (TextUtils.isEmpty(inputname.getText().toString())) {
                    inputname.setError("Enter Your Name!");
                    return;
                }
                if (TextUtils.isEmpty(inputphoneno.getText().toString())||!new validationClass(inputphoneno.getText().toString()).checkmobileno()) {
                    inputphoneno.setError("Invalid Mobile No");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                               // Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
                                    alertDialogBuilder.setTitle("Error");
                                    alertDialogBuilder
                                            .setMessage("Authentication failed." + task.getException())
                                            .setCancelable(false)
                                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                } else {

//                                    getvalue();
//                                    databaseReference=firebaseDatabase.getInstance().getReference("DATA");
//                                    String Key=databaseReference.push().getKey();
//                                    databaseReference.child(Key).child("Name").setValue(sendProfile.getName());
//                                    databaseReference.child(Key).child("Emailid").setValue(sendProfile.getEmailid());
//                                    databaseReference.child(Key).child("password").setValue(sendProfile.getPassword());
//                                    databaseReference.child(Key).child("phone").setValue(sendProfile.getPhoneno());
//
//                                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
//                                    final String token=sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
//                                    StringRequest stringRequest=new StringRequest(Request.Method.POST, app_server_url,
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    Log.i("ASK",response);
//                                                }
//                                            }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//
//                                        }
//                                    })
//                                    {
//                                        @Override
//                                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                                            Map<String,String> params=new HashMap<String,String>();
//                                            params.put("fcm_token",token);
//                                            params.put("emailid",sendProfile.getEmailid());
//                                            return params;
//                                        }
//                                    };
//                                    MySingleton.getMInstance(SignupActivity.this).addToRequestqueue(stringRequest);




                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
                                    alertDialogBuilder.setTitle("Done");
                                    alertDialogBuilder
                                            .setMessage("Registration SuccessFull")
                                            .setCancelable(false)
                                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    startActivity(new Intent(SignupActivity.this, MainActivity1.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                        });

            }
        });
    }
//    public void getvalue()
//    {
//        sendProfile.setEmailid(inputEmail.getText().toString());
//        sendProfile.setName(inputname.getText().toString());
//        sendProfile.setPassword(inputPassword.getText().toString());
//        sendProfile.setPhoneno(inputphoneno.getText().toString());
//    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}

