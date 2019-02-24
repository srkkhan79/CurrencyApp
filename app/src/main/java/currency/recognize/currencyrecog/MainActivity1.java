package currency.recognize.currencyrecog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity1 extends BaseActivity implements View.OnClickListener
{
    private CardView recordcard,aboutuscard,sendcard,gesturecard,learncard;
    String USEREMAIL="";

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(android.content.Intent.ACTION_MAIN);
        intent.addCategory(android.content.Intent.CATEGORY_HOME);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main1, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        USEREMAIL=user.getEmail();

        recordcard=(CardView)findViewById(R.id.record);
        gesturecard=(CardView)findViewById(R.id.gesture);
        learncard=(CardView)findViewById(R.id.learn);
        aboutuscard=(CardView)findViewById(R.id.aboutuscard);
        recordcard.setOnClickListener(this);
        aboutuscard.setOnClickListener(this);
        gesturecard.setOnClickListener(this);
        learncard.setOnClickListener(this);
        

    }


    @Override
    public void onClick(View view) {

        Intent i;
        switch (view.getId()){

            case R.id.record:
                i=new Intent(this,RecordActivity.class);
                i.putExtra("USERID",USEREMAIL);
                startActivity(i);
                break;

            case R.id.gesture: i=new Intent(this,SkinActivity.class);startActivity(i);break;

            case R.id.learn:
              i=new Intent(this,LearnActivity.class);startActivity(i);break;

            default:break;
        }
    }
}