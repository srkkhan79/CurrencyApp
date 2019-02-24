package currency.recognize.currencyrecog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.LINE_AA;
import static org.opencv.imgproc.Imgproc.MARKER_DIAMOND;
import static org.opencv.imgproc.Imgproc.boundingRect;

public class RecordActivity extends BaseActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    JavaCameraView javaCameraView;
    Mat imgHSV,imgThershold;
    Scalar skin1,skin2;
    TextView txtview;
    private GestureLibrary gLibrary;
    Handler handler=new Handler();

    final Context context=this;
String username=null;


    BaseLoaderCallback mloadercallback=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_record, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        username= getIntent().getStringExtra("USERID");
      //  skin1=new Scalar(38,126,0);//Skin Color
        //skin2=new Scalar(74,255,255);
        skin1=new Scalar(110,50,50);//Skin Color
        skin2=new Scalar(130,255,255);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        javaCameraView=(JavaCameraView)findViewById(R.id.java_cameraviewnew);
        txtview=(TextView)findViewById(R.id.textView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(0);
        javaCameraView.setCvCameraViewListener(this);
        gLibrary= GestureLibraries.fromFile(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/gestures");
        if(gLibrary.load())
        {
            Log.i("ASK","GESTURE LIBRARY LOADED");
        }
        showdialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView!=null)
        {
            javaCameraView.disableView();
        }
    }


    public void showdialog()
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText edithuemin=(EditText)promptsView.findViewById(R.id.editHuemin);
        final EditText editsatmin=(EditText)promptsView.findViewById(R.id.editsatmin);
        final EditText editvalmin=(EditText)promptsView.findViewById(R.id.editvalmin);

        final EditText edithuemax=(EditText)promptsView.findViewById(R.id.editHuemax);
        final EditText editsatmax=(EditText)promptsView.findViewById(R.id.editsatmax);
        final EditText editvalmax=(EditText)promptsView.findViewById(R.id.editvalmax);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                int huemin=Integer.parseInt(edithuemin.getText().toString());
                                int satmin=Integer.parseInt(editsatmin.getText().toString());
                                int valmin=Integer.parseInt(editvalmin.getText().toString());

                                int huemax=Integer.parseInt(edithuemax.getText().toString());
                                int satmax=Integer.parseInt(editsatmax.getText().toString());
                                int valmax=Integer.parseInt(editvalmax.getText().toString());


                                skin1=new Scalar(huemin,satmin,valmin);//Skin Color
                                skin2=new Scalar(huemax,satmax,valmax);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.i("ASK","loaded");
            mloadercallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.i("ASK","Not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2,this,mloadercallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        imgHSV=new Mat(width,height, CvType.CV_16UC4);
        imgThershold=new Mat(width,height, CvType.CV_16UC4);
    }

    @Override
    public void onCameraViewStopped() {
        imgHSV.release();
        imgThershold.release();
    }


    Point[] points;

    double max=0;
    String Namefinal="";
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat com=new Mat();
        Mat Frame=inputFrame.rgba();
        Mat skinmat=new Mat();
        Imgproc.cvtColor(inputFrame.rgba(),imgHSV,COLOR_BGR2HSV);
        Core.inRange(imgHSV,skin1,skin2,skinmat);
        Core.bitwise_and(Frame,Frame,com,skinmat);
        Mat x=new Mat();
        x=com.clone();
        Imgproc.cvtColor(x,x,COLOR_RGB2GRAY);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        double area=0;
        int id=0;
        Rect boundingrect=new Rect();
        Imgproc.findContours(x, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        try {
            for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
                double newarea = Imgproc.contourArea(contours.get(contourIdx));
                if (area < newarea) {
                    area = newarea;
                    id = contourIdx;
                    boundingrect = boundingRect(contours.get(contourIdx));

                }
            }
            MatOfPoint contourpoint = contours.get(id);
            points = contourpoint.toArray();

            ArrayList<GesturePoint> gt = new ArrayList<GesturePoint>();

            for (int i = 0; i < points.length; ++i) {
                float x1 = Float.parseFloat(String.valueOf(points[i].x));
                float y1 = Float.parseFloat(String.valueOf(points[i].y));
                GesturePoint gesturePoint = new GesturePoint(x1, y1, 40000);
                gt.add(gesturePoint);
            }
            GestureStroke gestureStroke = new GestureStroke(gt);
            Gesture Gs = new Gesture();
            Gs.addStroke(gestureStroke);

            ArrayList<Prediction> predictions;
            String Name = "";
            ArrayList<Double> pred = new ArrayList<Double>();
            ArrayList<String> name = new ArrayList<String>();

            predictions = gLibrary.recognize(Gs);

            for (Prediction prediction : predictions) {
                if (prediction.score > 3.0) {
                 //   Log.i("ASK","PREDICTION SCORE : "+String.valueOf(prediction.score));
                    pred.add(Double.parseDouble(String.valueOf(prediction.score)));
                    name.add(prediction.name.toString());
                }
            }


            for (int i = 0; i < pred.size(); i++) {
                Namefinal = name.get(i);
                handler.postDelayed(hMyTimeTask, 1000);

               // Log.i("ASK", String.valueOf(pred.get(i)) + "--" + name.get(i));
                if (pred.get(i) >= max) {
                    max = pred.get(i);
                }
            }

            if (pred.size() > 0) {
                pred.clear();
                name.clear();
            }

            Imgproc.drawContours(com, contours, id, new Scalar(0, 0, 250), 5);
            Imgproc.rectangle(com, boundingrect.br(), boundingrect.tl(), new Scalar(255, 0, 0), 2);
            Point p = new Point((boundingrect.tl().x + boundingrect.br().x) / 2, (boundingrect.tl().y + boundingrect.br().y) / 2);
            Imgproc.drawMarker(com, p, new Scalar(0, 255, 0), MARKER_DIAMOND, 3, 1, LINE_AA);

        }
        catch(Exception ex)
        {

        }
        return com;
    }
    static int nCounter=0;
    List<String> mywordlist=new ArrayList<String>();
    private Runnable hMyTimeTask = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void run() {
            txtview.setText(Namefinal);
            nCounter++;
            if(mywordlist.size()>0)
            {
                if(Namefinal.equals("del"))
                {
                    mywordlist.remove(mywordlist.size()-1);
                }
                else
                {
                    if(mywordlist.get(mywordlist.size()-1).equals(Namefinal))
                    {

                    }
                    else
                    {
                        mywordlist.add(Namefinal);
                    }
                }
            }
            else
            {
                mywordlist.add(Namefinal);
            }
            Log.i("ASK",String.valueOf(mywordlist.size()));
            Log.i("POW","----------------------------");
            for(int j=0;j<mywordlist.size();j++)
            {
                Log.i("POW",mywordlist.get(j));
            }
            Log.i("POW","----------------------------");
        }
    };
    public void clearlilst(View view)
    {
        StringBuilder builder = new StringBuilder();
        for(String s : mywordlist) {
            builder.append(s);
        }
        String xyz=builder.toString();
        Intent intent=new Intent(RecordActivity.this,Sendvideoacti.class);
        intent.putExtra("USERID",username);
        intent.putExtra("MSG",xyz);
        startActivity(intent);

    }

}
