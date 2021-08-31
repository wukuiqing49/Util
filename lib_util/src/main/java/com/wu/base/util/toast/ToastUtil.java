package com.wu.base.util.toast;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
    private static final String TAG = "ToastUtil";
    private static long show_time;
    private static String show_msg = "";
    private Context context;
    private Build config;

    public static Build newBuild() {
        return new Build();
    }

    private ToastUtil(Context context) {
        this.context = context;
    }

    public ToastUtil(Context context, Build config) {
        this.context = context;
        this.config = config;
    }

    public void showToast(String str, int duration) {
        if (this.context != null && !TextUtils.isEmpty(str)) {
            if (show_time + 2000L < System.currentTimeMillis() || !show_msg.equals(str)) {
                show_msg = str;
                View view = this.getView(str);
                Context var10000 = this.context;
                show_msg = str;
                Toast toast =
//                        (android.os.Build.VERSION.SDK_INT == 25 || android.os.Build.VERSION.SDK_INT == 24) ?
//                                Toast.makeText(var10000, str, duration) :
                        ToastCompat.makeText(var10000, str, duration);
                toast.setGravity(this.config == null ? 0 : this.config.gravity, this.config == null ? 0 : this.config.xOffset, this.config == null ? 0 : this.config.yOffset);
                if (view != null) {
                    toast.setView(view);
                }

                toast.show();
            }

            show_time = System.currentTimeMillis();
        }
    }

    public void showToastAgriculture(String str, int duration) {
        if (this.context != null && !TextUtils.isEmpty(str)) {
            View view = this.getView(str);
            Context var10000 = this.context;
            Toast toast =
//                    (android.os.Build.VERSION.SDK_INT == 25 || android.os.Build.VERSION.SDK_INT == 24) ?
//                            Toast.makeText(var10000, str, duration) :
                            ToastCompat.makeText(var10000, str, duration);
            toast.setGravity(this.config == null ? 0 : this.config.gravity, this.config == null ? 0 : this.config.xOffset, this.config == null ? 0 : this.config.yOffset);
            if (view != null) {
                toast.setView(view);
            }
            toast.show();
        }
    }

    public void showToast(String str) {
        this.showToast(str, 0);
    }

    public void showNoEqulesToast(String str) {
        this.showToastAgriculture(str, 0);
    }

    private View getView(String message) {
        View view = null;

        try {
            view = LayoutInflater.from(this.context).inflate(this.config == null ? 0 : this.config.layoutId, (ViewGroup) null, false);
            TextView textView = (TextView) view.findViewById(this.config.viewId);
            textView.setText(message);
        } catch (Exception var4) {
        }

        return view;
    }

    public static class Build {
        private int gravity;
        private int xOffset;
        private int yOffset;
        private int layoutId;
        private int viewId;

        private Build() {
            this.gravity = 0;
            this.xOffset = 0;
            this.yOffset = 0;
            this.layoutId = 0;
            this.viewId = 0;
        }

        public int getGravity() {
            return this.gravity;
        }

        public Build setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Build setGravity(int gravity, int xOffset, int yOffset) {
            this.gravity = gravity;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public int getxOffset() {
            return this.xOffset;
        }

        public Build setxOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public int getyOffset() {
            return this.yOffset;
        }

        public Build setyOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public int getViewId() {
            return this.viewId;
        }

        public Build setViewId(int viewId) {
            this.viewId = viewId;
            return this;
        }

        public Build setView(int layoutId, int viewId) {
            this.viewId = viewId;
            this.layoutId = layoutId;
            return this;
        }

        public int getLayoutId() {
            return this.layoutId;
        }

        public Build setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public ToastUtil build(Context context) {
            return new ToastUtil(context, this);
        }
    }
}
