package currency.recognize.currencyrecog;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.Point;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by srk on 04/03/2018.
 */

public class CustomCurrency {
    Gesture Gs;
    Point[] points;
    ArrayList<GesturePoint> gt=new ArrayList<GesturePoint>();
    private final File mStoreFile=new File(Environment.getExternalStorageDirectory(),"gestures");
    private static GestureLibrary sStore;
    String Name="";
    CustomCurrency(Point[] mypoints, String name)
    {

        if(sStore==null)
        {
            Log.i("ASK","FILE FOUND");

            sStore = GestureLibraries.fromFile(mStoreFile);
        }
        if(sStore==null)
        {


            sStore = GestureLibraries.fromFile(mStoreFile);
        }

        this.points=mypoints;
        Name=name;
        CreateGusture();
    }
    private void CreateGusture()
    {
        for (int i=0;i<points.length;i++)
        {
            float x=Float.parseFloat(String.valueOf(points[i].x));
            float y=Float.parseFloat(String.valueOf(points[i].y));
            GesturePoint gesturePoint=new GesturePoint(x,y,40000);
            gt.add(gesturePoint);
        }

        Log.i("ASK","Gester Point Loaded");
        GestureStroke gestureStroke=new GestureStroke(gt);
        Gs=new Gesture();
        Gs.addStroke(gestureStroke);
        final GestureLibrary store= CurrencyBuilderActivity.getStore();
        store.addGesture(Name,Gs);
        store.save();

        Log.i("ASK","Gester SuccessFully Added");
    }
}
