package com.xx.lxz.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.LinearLayout.LayoutParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.vp_home) ViewPager vp_home;

    @BindView(R.id.indicator) LinearLayout indicator;
    @BindView(R.id.btn_six_week)
    Button btn_six_week;
    @BindView(R.id.btn_nine_week)
    Button btn_nine_week;
    @BindView(R.id.btn_twelve_week)
    Button btn_twelve_week;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    private List<ImageView> indicator_list; // 圆点的集合
    private List<ImageView> imageView_list; // 滑动的图片集合
    private int currentItem = 0; // 当前图片的索引号
    private int current = 0;
    private int oldPosition = 0;
    private MyAdapter adapter;
    private Handler mHandler;
    int image[] = { R.mipmap.home_banner1, R.mipmap.home_banner1,
            R.mipmap.home_banner1,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mHandler=new Handler();
        imageView_list = new ArrayList<ImageView>();
        for (int i = 0; i < image.length; i++) {
            ImageView imgView = new ImageView(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            imgView.setId(i);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // params.setMargins(0, 0, 5, 0);
            imgView.setLayoutParams(params);
            imageView_list.add(imgView);
            imgView.setBackgroundResource(image[i]);
//            imageLoader.displayImage(advertisement.getSignUrl(), imgView,
//                    options);
//            Log.i("TEST",
//                    "advertisement.getSignUrl()-----》"
//                            + advertisement.getSignUrl());
            // imageLoader.displayImage("http://pic1.nipic.com/2008-12-09/200812910493588_2.jpg",
            // imgView, options);

        }

        indicator_list = new ArrayList<ImageView>();

        initIndicator(imageView_list.size());

        adapter = new MyAdapter();
        vp_home.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vp_home.setOnPageChangeListener(new MyPageChangeListener());
        Log.i("TEST", "广告个数=" + imageView_list.size());
        mHandler.removeCallbacks(runnable);//把之前的接口移除掉
        mHandler.postDelayed(runnable, 2000);
        Log.i("TEST", "imageView_list长度222-----》" + imageView_list.size());
    }

    /**
     * 监听事件
     * @param v
     */
    @OnClick({R.id.btn_six_week,R.id.btn_nine_week,R.id.btn_twelve_week,R.id.btn_apply})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_six_week:
                btn_six_week.setBackgroundResource(R.mipmap.home_select);
                btn_nine_week.setBackgroundResource(R.mipmap.home_unselect);
                btn_twelve_week.setBackgroundResource(R.mipmap.home_unselect);
                break;
            case R.id.btn_nine_week:
                btn_six_week.setBackgroundResource(R.mipmap.home_unselect);
                btn_nine_week.setBackgroundResource(R.mipmap.home_select);
                btn_twelve_week.setBackgroundResource(R.mipmap.home_unselect);
                break;
            case R.id.btn_twelve_week:
                btn_six_week.setBackgroundResource(R.mipmap.home_unselect);
                btn_nine_week.setBackgroundResource(R.mipmap.home_unselect);
                btn_twelve_week.setBackgroundResource(R.mipmap.home_select);
                break;
            case R.id.btn_apply:
                break;
        }
    }

    private void initIndicator(int length) {
        View v = findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        indicator_list.clear();
        for (int i = 0; i < length; i++) {
            ImageView imgView = new ImageView(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 5, 0);
            imgView.setLayoutParams(params);
            if (i == 0) { // 初始化第一个为选中状态
                imgView.setBackgroundResource(R.mipmap.indicator_focused);
            } else {
                imgView.setBackgroundResource(R.mipmap.indicator_normal);
            }
            ((ViewGroup) v).addView(imgView);
            indicator_list.add(imgView);
        }

    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageView_list == null ? 0 : imageView_list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ((ViewPager) view).addView(imageView_list.get(position));
            return imageView_list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView(object);
            ((ViewPager) container).removeView((View) object);
        }

    }

    public class ViewHandler {
        ImageView iv_adver;// 消息时间
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {


        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            indicator_list.get(oldPosition).setBackgroundResource(
                    R.mipmap.indicator_normal);
            indicator_list.get(position).setBackgroundResource(
                    R.mipmap.indicator_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            if(vp_home!=null){
                vp_home.setCurrentItem(current);
            }
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
            if(imageView_list!=null){
                current++;
                current = current % imageView_list.size();
            }
            if(mHandler!=null){
                mHandler.postDelayed(this, 2000);
            }
        }
    };
}
