package currency.recognize.currencyrecog;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LearnActivity extends BaseActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_learn, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mSlideViewPager=(ViewPager)findViewById(R.id.slideview);
        mDotLayout=(LinearLayout)findViewById(R.id.dotsLayout);
        mNextBtn=(Button)findViewById(R.id.nextbtn);
        mBackBtn=(Button)findViewById(R.id.backbtn);
        sliderAdapter=new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListner);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentpage+1);
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentpage-1);
            }
        });



    }
    public void addDotIndicator(int position){
        mDots=new TextView[8];
        mDotLayout.removeAllViews();
        for (int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.gesture_color1));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected( int i) {

            addDotIndicator(i);
            mCurrentpage=i;
            if (i==0){
                mBackBtn.setEnabled(false);
                mNextBtn.setEnabled(true);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("NEXT");
                mBackBtn.setText("");
            }
            else if (i== mDots.length-1)
            {
                mBackBtn.setEnabled(true);
                mNextBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("FINISH");
                mBackBtn.setText("BACK");

            }else
            {
                mBackBtn.setEnabled(true);
                mNextBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("NEXT");
                mBackBtn.setText("BACK");

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
