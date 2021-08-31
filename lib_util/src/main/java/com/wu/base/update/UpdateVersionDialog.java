package com.wu.base.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wu.base.R;


/**
 * 创建：wukuiqing
 * <p>
 * 时间：2017/12/22
 * <p>
 * 描述： 购买
 */

public class UpdateVersionDialog extends Dialog implements View.OnClickListener {

    private String content;
    private String title;
    private UpdateVersionListener listener;

    public void setOnUpdateNickListener(UpdateVersionListener updateNickListener) {
        listener = updateNickListener;
    }
    public UpdateVersionDialog(@NonNull Context context, String title, String content) {
        super(context, R.style.dialog_reward);
        this.content = content;
        this.title = title;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_version);
        initView();
        setViewLocation();
        setCanceledOnTouchOutside(false);
    }

    private void initView() {
        if (!TextUtils.isEmpty(content)) {
            ((TextView) findViewById(R.id.tv_content)).setText(content);
        } else {
            ((TextView) findViewById(R.id.tv_content)).setText("新版本更新了!!!");
        }

        ((TextView) findViewById(R.id.tv_title)).setText(title);
        findViewById(R.id.tv_ok).setOnClickListener(this);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    /**
     * 设置dialog位于屏幕底部
     */
    private void setViewLocation() {
        if (getWindow() == null || getWindow().getAttributes() == null) return;
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) listener.clickUpdate();
        if (v.getId()==R.id.tv_cancle)listener.cancel();
    }
    public interface UpdateVersionListener {
        void clickUpdate();
        void cancel();
    }
}
