package com.xx.lxz.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by XiaoMan on 2018/4/20 16:49
 * Email:1635401972@qq.com
 * Details:
 */


public class GlideUtils {

    //加载bitmap，如果是GIF则显示第一帧
    private static String LOAD_BITMAP = "GLIDEUTILS_GLIDE_LOAD_BITMAP";
    //加载gif动画
    private static String LOAD_GIF = "GLIDEUTILS_GLIDE_LOAD_GIF";

    private static GlideUtils instance;

    public static GlideUtils getInstance() {
        if (instance == null) {
            synchronized (GlideUtils.class) {
                if (instance == null) {
                    instance = new GlideUtils();
                }
            }
        }
        return instance;
    }

    public void loadBitmap(Context context, String path, ImageView imageView) {
        LoadContextBitmap(context, path, imageView, 0, 0, null);
    }

    /**
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * 使用activity 会受到Activity生命周期控制
     * 使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param path
     * @param imageView
     * @param placeid     占位
     * @param errorid     错误
     * @param bitmapOrgif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    void LoadContextBitmap(Context context, String path, ImageView imageView, int placeid, int errorid, String bitmapOrgif) {
        if (context != null) {
            if (bitmapOrgif == null || bitmapOrgif.equals(LOAD_BITMAP)) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(placeid)
                        .priorityOf(Priority.HIGH)
                        .error(errorid);
                Glide.with(context).load(path).apply(options).into(imageView);
            } else if (bitmapOrgif.equals(LOAD_GIF)) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(placeid)
                        .error(errorid)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(context).load(path).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).apply(options).into(imageView);
            }
        }
    }

    /**
     * Glide请求图片，会受到Fragment 生命周期控制。
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param placeid
     * @param errorid
     * @param bitmapOrgif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void LoadFragmentBitmap(android.app.Fragment fragment, String path, ImageView imageView, int placeid, int errorid, String bitmapOrgif) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeid)
                .error(errorid)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (bitmapOrgif == null || bitmapOrgif.equals(LOAD_BITMAP)) {
            Glide.with(fragment).load(path).transition(withCrossFade()).apply(options).into(imageView);
        } else if (bitmapOrgif.equals(LOAD_GIF)) {
            Glide.with(fragment).load(path).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).apply(options).into(imageView);
        }
    }

    /**
     * Glide请求图片，会受到support.v4.app.Fragment生命周期控制。
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param placeid
     * @param errorid
     * @param bitmapOrgif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void LoadSupportv4FragmentBitmap(android.support.v4.app.Fragment fragment, String path, ImageView imageView, int placeid, int errorid, String bitmapOrgif) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeid)
                .error(errorid)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (bitmapOrgif == null || bitmapOrgif.equals(LOAD_BITMAP)) {
            Glide.with(fragment).load(path).transition(withCrossFade()).apply(options).into(imageView);
        } else if (bitmapOrgif.equals(LOAD_GIF)) {
            Glide.with(fragment).load(path).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).apply(options).into(imageView);
        }
    }
    //---------------------圆形图片-------------------

    /**
     * 加载设置圆形图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadContextCircleBitmap(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    /**
     * Glide请求图片设置圆形，会受到android.app.Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadfragmentCircleBitmap(android.app.Fragment fragment, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new GlideCircleTransform(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }

    /**
     * Glide请求图片设置圆形，会受到android.support.v4.app.Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentCircleBitmap(android.support.v4.app.Fragment fragment, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new GlideCircleTransform(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }
    //-----------------------圆角图片----------------------

    /**
     * 加载设置圆角图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param roundradius 圆角大小（>0）
     */
    @SuppressWarnings("unchecked")
    public void LoadContextRoundBitmap(Context context, String path, ImageView imageView, int roundradius) {
        if (roundradius < 0) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(context).load(path).apply(options).into(imageView);
        } else {
            RequestOptions options = new RequestOptions()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(context, roundradius))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }

    /**
     * Glide请求图片设置圆角，会受到android.app.Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param roundradius
     */
    @SuppressWarnings("unchecked")
    public void LoadfragmentRoundBitmap(android.app.Fragment fragment, String path, ImageView imageView, int roundradius) {
        if (roundradius < 0) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(fragment.getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(fragment).load(path).apply(options).into(imageView);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(fragment.getActivity(), roundradius))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(fragment).load(path).apply(options).into(imageView);
        }
    }

    /**
     * Glide请求图片设置圆角，会受到android.support.v4.app.Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param roundradius
     */
    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentRoundBitmap(android.support.v4.app.Fragment fragment, String path, ImageView imageView, int roundradius) {
        if (roundradius < 0) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(fragment.getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(fragment).load(path).apply(options).into(imageView);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .bitmapTransform(new GlideRoundTransform(fragment.getActivity(), roundradius))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(fragment).load(path).apply(options).into(imageView);
        }
    }
    //-------------------------------------------------

    /**
     * Glide 加载模糊图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadContextBlurBitmap(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    /**
     * Glide 加载模糊图片 会受到Fragment生命周期控制
     *
     * @param
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadFragmentBlurBitmap(android.app.Fragment fragment, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }

    /**
     * Glide 加载模糊图片 会受到support.v4.app.Fragment生命周期控制
     *
     * @param
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentBlurBitmap(android.support.v4.app.Fragment fragment, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }
    //---------------------------------------------------------

    /**
     * 旋转图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param rotateRotationAngle 旋转角度
     */
    @SuppressWarnings("unchecked")
    public void LoadContextRotateBitmap(Context context, String path, ImageView imageView, Float rotateRotationAngle) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new RotateTransformation(context, rotateRotationAngle))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    /**
     * Glide 加载旋转图片 会受到Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param rotateRotationAngle
     */
    @SuppressWarnings("unchecked")
    public void LoadFragmentRotateBitmap(android.app.Fragment fragment, String path, ImageView imageView, Float rotateRotationAngle) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new RotateTransformation(fragment.getActivity(), rotateRotationAngle))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }

    /**
     * Glide 加载旋转图片 会受到support.v4.app.Fragment生命周期控制
     *
     * @param fragment
     * @param path
     * @param imageView
     * @param rotateRotationAngle
     */
    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentRotateBitmap(android.support.v4.app.Fragment fragment, String path, ImageView imageView, Float rotateRotationAngle) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .bitmapTransform(new RotateTransformation(fragment.getActivity(), rotateRotationAngle))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }
    //----------------------旋转---------------------------

    /**
     * 旋转
     */
    public class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(Context context, float rotateRotationAngle) {
            super(context);

            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
    //--------------------------------------------------

    /**
     * 图片转圆形
     */
    public class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_4444);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
    //-----------------------------图片模糊----------------------------------

    /**
     * 图片模糊
     */
    public class BlurTransformation extends BitmapTransformation {

        private RenderScript rs;

        public BlurTransformation(Context context) {
            super(context);

            rs = RenderScript.create(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(
                    rs,
                    blurredBitmap,
                    Allocation.MipmapControl.MIPMAP_FULL,
                    Allocation.USAGE_SHARED
            );
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(10);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

            toTransform.recycle();

            return blurredBitmap;
        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
    //-------------------图片转换圆角图片------------------------------

    /**
     * 图片转换圆角图片
     */
    public class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        /**
         * 自定义圆角大小
         *
         * @param context
         * @param dp
         */
        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
