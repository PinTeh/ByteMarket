package cn.imhtb.bytemarket.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.imhtb.bytemarket.R;

public class GuideActivity extends AppCompatActivity {

    private List<View> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        View view = View.inflate(this, R.layout.guide_three, null);
        list.add(View.inflate(this,R.layout.guide_one,null));
        list.add(View.inflate(this,R.layout.guide_two,null));
        list.add(view);
        ViewPager viewPager = findViewById(R.id.vp_scroll);
        viewPager.setAdapter(new ViewPagerAdapter());
        view.findViewById(R.id.btn_enter).setOnClickListener(v -> {
            Intent intent = new Intent(GuideActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(list.get(position));
        }
    }
}
