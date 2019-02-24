package currency.recognize.currencyrecog;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context){
        this.context=context;
    }
    public int[] slide_images={
            R.drawable.tenrup,
            R.drawable.oldtenrup,
            R.drawable.fiftyrup,
            R.drawable.hundredrup,
            R.drawable.fivehund,
            R.drawable.fivehundnew,
            R.drawable.twohund,
            R.drawable.thousandrup
    };
    public String[] slide_headings={
            "10 RS Note",
            "10 RS Old Note",
            "50 RS Note",
            "100 RS Note",
            "500 RS Note",
            "500 RS New Note",
            "200 RS Note",
            "1000 RS Note"
    };
    public String[] slide_desc={
            "10 RS Note","10 RS Old Note","50 RS Note","100 RS Note","500 RS Note","500 RS New Note","200 RS Note", "1000 RS Note"
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageView=(ImageView)view.findViewById(R.id.slide_image);
        TextView slideHeadindView=(TextView)view.findViewById(R.id.slide_heading);
        TextView slideDescView=(TextView)view.findViewById(R.id.slide_desc);
        slideImageView.setImageResource(slide_images[position]);
        slideHeadindView.setText(slide_headings[position]);
        slideDescView.setText(slide_desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
