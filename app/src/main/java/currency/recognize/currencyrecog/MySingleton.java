package currency.recognize.currencyrecog;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 17/03/2018.
 */

public class MySingleton {
    private static MySingleton mySingleton;
    private static Context contex;
    private RequestQueue requestQueue;
    private MySingleton(Context contex)
    {
        this.contex=contex;
        requestQueue=getRequestQueue();
    }
    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(contex.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getMInstance(Context contex)
    {
        if(mySingleton==null)
        {
            mySingleton=new MySingleton(contex);
        }
        return mySingleton;
    }
    public<T> void addToRequestqueue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
