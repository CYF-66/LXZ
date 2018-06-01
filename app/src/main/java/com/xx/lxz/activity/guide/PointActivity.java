package com.xx.lxz.activity.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.xx.lxz.MainActivity;
import com.xx.lxz.R;
import com.xx.lxz.adapter.GuidePageAdapter;
import com.xx.lxz.base.AppManager;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.PublicParams;

import java.util.ArrayList;

/**
 * 引导页
 *
 * @author pc
 */
public class PointActivity extends BaseActivity {

    private ViewPager viewPager;
    /**
     * 装分页显示的view的数组
     */
    private ArrayList<View> pageViews;
    private GestureDetector detector;
    private Context context;
    private static String TAG = "PointActivity";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PointActivity.this;
        AppManager.getAppManager().addActivity(this);
        // 标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置窗体状态
        this.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);
        // 将要分页显示的View装入数组中
        pageViews = new ArrayList<View>();
        ImageView imageview1 = new ImageView(context);
        ImageView imageview2 = new ImageView(context);
        ImageView imageview3 = new ImageView(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        imageview1.setLayoutParams(layoutParams);
        imageview2.setLayoutParams(layoutParams);
        imageview3.setLayoutParams(layoutParams);

        imageview1.setBackgroundResource(R.mipmap.point1);
        imageview2.setBackgroundResource(R.mipmap.point2);
        imageview3.setBackgroundResource(R.mipmap.point3);
        imageview3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PublicParams.overonestart();
                Intent mainIntent = null;
                mainIntent = new Intent(PointActivity.this,
                        MainActivity.class);
                PointActivity.this.startActivity(mainIntent);
                PointActivity.this.finish();

            }
        });

        pageViews.add(imageview1);
        pageViews.add(imageview2);
        pageViews.add(imageview3);
        this.setContentView(R.layout.point);
        viewPager = (ViewPager) findViewById(R.id.guidePages);
        // 设置viewpager的适配器和监听事件
        viewPager.setAdapter(new GuidePageAdapter(pageViews));
        viewPager.setOnPageChangeListener(null);
        detector = new GestureDetector(new GestureListener());
        viewPager.setOnTouchListener(new TouhListener());

    }

    class TouhListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }

    }

    private int verticalMinDistance = 180;

    private int minVelocity = 0;

    // 手势滑动监听

    class GestureListener implements OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // e1 触摸的起始位置，e2 触摸的结束位置，velocityX X轴每一秒移动的像素速度（大概这个意思） velocityY
            // 就是Ｙ咯
            // 手势左,上为正 ——，右，下为负正
            if (e1.getX() - e2.getX() > verticalMinDistance
                    && Math.abs(velocityX) > minVelocity) {
                if (viewPager.getCurrentItem() == pageViews.size() - 1) {
                    PublicParams.overonestart();
                    Intent mainIntent = null;
                    mainIntent = new Intent(PointActivity.this,
                            MainActivity.class);
                    PointActivity.this.startActivity(mainIntent);
                    PointActivity.this.finish();
                }
                LogUtil.d(TAG, "向左滑动");
            } else if (e2.getX() - e1.getX() > verticalMinDistance
                    && Math.abs(velocityX) > minVelocity) {
                LogUtil.d(TAG, "向右滑动");
            }
            return false;
        }

    }

    class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.d(TAG, "position==" + position);
            LogUtil.d(TAG, "pageViews.size()==" + pageViews.size());
            if (position == pageViews.size() - 1) {
            } else {
            }
        }
    }

}
