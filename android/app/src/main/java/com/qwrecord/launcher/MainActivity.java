package com.qwrecord.launcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    private static final String TAG = "QWRecord";
    private static final String DEEPLINK = "tongyi://page/startRecording";
    private static final String PKG = "com.aliyun.tongyi";
    private boolean launched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launched = false;
        launchQianwen();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次可见都尝试跳转（跳转成功后会 finish，不会形成循环）
        launchQianwen();
    }

    private void launchQianwen() {
        if (launched) return;
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK));
            i.setPackage(PKG);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            launched = true;
            Log.i(TAG, "launched qianwen recording");
            // 跳完自毁：从千问返回时直接回桌面，不会重复拉起
            finish();
        } catch (Exception e) {
            Log.e(TAG, "launch failed: " + e.getMessage());
            // 失败则留在本页，用户可点按钮重试
        }
    }
}
