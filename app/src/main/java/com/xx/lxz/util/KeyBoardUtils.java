package com.xx.lxz.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by sunxl8 on 2017/12/8.
 */

public class KeyBoardUtils {

    public static void showKeyBoard(Activity activity) {
        if (activity == null) return;
        View view = activity.getWindow().peekDecorView();

        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }

//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
        if (activity == null) return;
        View view = activity.getWindow().getDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void addKeyBoadrListener(final View view, final KeyBoardChangeListener listener) {
        if(view==null) return;
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        //r will be populated with the coordinates of your view that area still visible.
                        view.getWindowVisibleDisplayFrame(r);

                        final int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                        if (heightDiff > 300) {
                            listener.status(true);
                        } else {
                            listener.status(false);
                        }
                    }
                }
        );
    }

    public interface KeyBoardChangeListener {
        void status(boolean status);
    }
}
