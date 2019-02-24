package currency.recognize.currencyrecog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class SkinActivity extends BaseActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    JavaCameraView javaCameraView;
    Mat imgHSV,imgThershold;
    Scalar skin1,skin2;
    EditText editText;
    int width,height;
    final Context context=this;



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
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_skin, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(4).setChecked(true);

        editText=(EditText) findViewById(R.id.edittextview);
        int permissionCheck = ContextCompat.checkSelfPermission(SkinActivity.this, android.Manifest.permission.CAMERA);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (ContextCompat.checkSelfPermission(SkinActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SkinActivity.this,
                    android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(SkinActivity.this,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
        skin1=new Scalar(38,126,0);//Skin Color
        skin2=new Scalar(74,255,255);
     /*   skin1=new Scalar(102,255,0);//Green Color
        skin2=new Scalar(130,255,255);*/
        javaCameraView=(JavaCameraView)findViewById(R.id.java_cameraview1);

        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(0);
        javaCameraView.setCvCameraViewListener(this);
        showdialog();
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.e("Ajay","loaded");
            mloadercallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.e("Ajay","Not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2,this,mloadercallback);
        }
    }
    @Override
    public void onCameraViewStarted(int width, int height) {
        this.width=width;
        this.height=height;
        imgHSV=new Mat(width,height, CvType.CV_16UC4);
        imgThershold=new Mat(width,height, CvType.CV_16UC4);
    }
    @Override
    public void onCameraViewStopped() {

        imgHSV.release();
        imgThershold.release();
    }
    Point[] points;
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
            Imgproc.drawContours(com, contours, id, new Scalar(0, 0, 250), 5);
            Imgproc.rectangle(com, boundingrect.br(), boundingrect.tl(), new Scalar(255, 0, 0), 2);
            Point p = new Point((boundingrect.tl().x + boundingrect.br().x) / 2, (boundingrect.tl().y + boundingrect.br().y) / 2);
            Imgproc.drawMarker(com, p, new Scalar(0, 255, 0), MARKER_DIAMOND, 3, 1, LINE_AA);
        }
        catch (Exception ex)
        {
        }
        return com;
    }

    public void buttonclick(View view)
    {
        String name=editText.getText().toString();
        if(!TextUtils.isEmpty(name)) {
            javaCameraView.disableView();
            CustomCurrency customCurrency =new CustomCurrency(points,editText.getText().toString());
            Toast.makeText(this, "Gesture Is Added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Enter Your Gesture Name",Toast.LENGTH_SHORT).show();
        }
    }
    public void buttonintent(View view)
    {

        javaCameraView.disableView();
        Intent i = new Intent(this, CurrencyBuilderActivity.class);
        startActivity(i);

    }
    public void buttondraw(View view)
    {
        javaCameraView.disableView();
        Intent i=new Intent(this,RecordActivity.class);
        startActivity(i);
    }
}
