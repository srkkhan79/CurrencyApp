package currency.recognize.currencyrecog;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Sendvideoacti extends AppCompatActivity {

    Button sendtext;
    EditText emailid,message;
    String username=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendvideoacti);


        sendtext=(Button)findViewById(R.id.btnsend);
        emailid=(EditText)findViewById(R.id.editTextemailid);
        message=(EditText)findViewById(R.id.editTextmessage);
        username= getIntent().getStringExtra("USERID");
        String msggg=getIntent().getStringExtra("MSG");
        Log.i("ASK","SENDvIdeo:--- "+msggg);
        message.setText(msggg);


        emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(new validationClass(emailid.getText().toString()).checkemail())
                {

                    emailid.setTextColor(Color.parseColor("WHITE"));
                }
                else
                {
                    emailid.setError("Invalid Email Address");
                    emailid.setTextColor(Color.parseColor("BLACK"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void SendText(View view)
    {
        if (TextUtils.isEmpty(emailid.getText().toString())||!new validationClass(emailid.getText().toString()).checkemail() ) {
            emailid.setError("Invalid Email address!");
            return;
        }
        if (TextUtils.isEmpty(message.getText().toString())) {
            message.setError("Enter Valid Message!");
            return;
        }
        FirebaseAuth auth= FirebaseAuth.getInstance();

        auth.fetchProvidersForEmail(emailid.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {

            @Override
            public void onComplete( Task<ProviderQueryResult> task) {
                if(task.getResult().getProviders().size()==1)
                {
                    sendtext(username);
                }
                else {
                    Toast.makeText(getApplicationContext(), "User Does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void sendtext(String user)
    {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setTitle("Sending..");
        pDialog.show();

        final String emailfrom=user;
        final String emailto=emailid.getText().toString();
        final String text=message.getText().toString();
        final String type="video";
        final RequestQueue mRequestQueue= Volley.newRequestQueue(this);
        final RequestQueue FirstRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://askajay3.000webhostapp.com/send_notification.php";


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());


        String firsturl="https://askajay3.000webhostapp.com/send_message.php";
        StringRequest firstreq=new StringRequest(Request.Method.POST, firsturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ASK", "FIRST log "+response);
                        pDialog.hide();
                        if(response.equals("true"))
                        {
                            Toast.makeText(getApplicationContext(),"Message Sent Successfully",Toast.LENGTH_LONG).show();
                            StringRequest mStringRequest=new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("ASK", "second"+response);
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("ASK", "second Reuest error"+error.getMessage());
                                        }
                                    })
                            {
                                @Override
                                protected Map<String, String> getParams()
                                {
                                    Map<String, String>  params = new HashMap<String, String>();
                                    params.put("emailid", emailto);
                                    params.put("title", "New Text Message From");
                                    params.put("message", emailfrom);
                                    return params;
                                }
                            };
                            mRequestQueue.add(mStringRequest);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Message Not sent : try Again",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(),"Message Not sent : try Again",Toast.LENGTH_LONG).show();
                        Log.i("ASK","FIRSTURL error "+error.getMessage());

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("emailfrom", emailfrom);
                params.put("emailto", emailto);
                params.put("message", text);
                params.put("msgtype", type);
                params.put("datetime", formattedDate);
                return params;
            }
        };
        FirstRequestQueue.add(firstreq);
    }

}
