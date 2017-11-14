package com.ap.utils;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.R;
import com.ap.base.BaseApplication;

/***
 * 类描述：提示类
 * 创建人：evan.yang
 * 创建时间：2016/5/13 9:25
 * Email: william.wang@kemai.cn
 * 修改备注：
 */
public class ToastUtil {

    private static Toast mToast;

    /**
     * 根据字符串提示
     *
     * @param text
     */
    public static void showToast(String text) {
        try {
            if (mToast == null) {
                initToast(text);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        } catch (Exception e) {
        }
    }

    private static void initToast(String text) {
        mToast = Toast.makeText(BaseApplication.getAppContext(), text, Toast.LENGTH_SHORT);
        LinearLayout linearLayout = (LinearLayout) mToast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(BaseApplication.getAppContext().getResources().getDimension(R.dimen.font_size_26));
        int y = (int) BaseApplication.getAppContext().getResources().getDimension(R.dimen.height50);
        mToast.setGravity(Gravity.BOTTOM, 0, y);
    }

}