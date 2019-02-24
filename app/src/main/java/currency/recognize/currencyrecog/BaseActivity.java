package currency.recognize.currencyrecog;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    static int REQUEST_VIDEO_CAPTURE=1;
    public static final int RequestPermissionCode = 10;
    public String path="";
    String USEREMAIL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        USEREMAIL=user.getEmail();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        Intent dash = new Intent(getApplicationContext(), MainActivity1.class);
                        startActivity(dash);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.record_Video:

                        Intent aIntent = new Intent(getApplicationContext(), RecordActivity.class);
                        aIntent.putExtra("USERID",USEREMAIL);
                        startActivity(aIntent);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.reciev_video:
                        Intent nIntent = new Intent(getApplicationContext(), RecievedVideoActivity.class);
                        nIntent.putExtra("USERID",USEREMAIL);
                        startActivity(nIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.sent_text:
                        Intent Intent = new Intent(getApplicationContext(), SentTextActivity.class);
                        Intent.putExtra("USERID",USEREMAIL);
                        startActivity(Intent);
                        drawerLayout.closeDrawers();
                        break;
//                    case R.id.nav_about_us:
//                        Intent anIntent = new Intent(getApplicationContext(), AboutUS.class);
//                        startActivity(anIntent);
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id.custom_ges:
                        Intent v=new Intent(getApplicationContext(),SkinActivity.class);
                        startActivity(v);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        Intent setv=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(setv);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit_me:
                        Intent intent = new Intent(android.content.Intent.ACTION_MAIN);
                        intent.addCategory(android.content.Intent.CATEGORY_HOME);
                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}