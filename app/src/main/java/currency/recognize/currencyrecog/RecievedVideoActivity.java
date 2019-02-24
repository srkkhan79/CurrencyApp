package currency.recognize.currencyrecog;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import currency.recognize.currencyrecog.Externalfile.Message;
import currency.recognize.currencyrecog.Externalfile.MessageAdapter;

public class RecievedVideoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{

    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Message> messageList;
    String username=null;

    final String url="https://askajay3.000webhostapp.com/select_messages.php";
    @Override
    public void onBackPressed()
    {
        Toast.makeText(getApplicationContext(),"Not Allowed",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_recieved_video, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        username= getIntent().getStringExtra("USERID");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadRecyclerViewData();
            }
        });

    }
 public void loadRecyclerViewData()
    {

        mSwipeRefreshLayout.setRefreshing(true);
        recyclerView=(RecyclerView)findViewById(R.id.msgrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList=new ArrayList<>();

        RequestQueue myrequesqueue= Volley.newRequestQueue(this);
        StringRequest mystringreq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("user_data");
                    for(int i=0;i<cast.length();i++)
                    {
                        JSONObject actor = cast.getJSONObject(i);
                        Message msg=new Message(actor.getString("emailfrom"),actor.getString("msgtype"),actor.getString("message"),actor.getString("datetime"));
                        messageList.add(msg);
                        Collections.sort(messageList, new Comparator<Message>() {
                            public int compare(Message m1, Message m2) {
                                return m1.getDate().compareTo(m2.getDate());
                            }
                        });
                        Collections.reverse(messageList);
                        adapter=new MessageAdapter(messageList,RecievedVideoActivity.this);
                        recyclerView.setAdapter(adapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
                catch (Exception ex)
                {

                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parms=new HashMap<String,String>();
                parms.put("emailto",username);
                return parms;
            }
        };
        myrequesqueue.add(mystringreq);
    }
    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
