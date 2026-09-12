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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchQianwen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从千问返回时不再重复拉起，仅首次进入时触发
    }

    private void launchQianwen() {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK));
            i.setPackage(PKG);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            Log.i(TAG, "launched qianwen recording");
        } catch (Exception e) {
            Log.e(TAG, "launch failed: " + e.getMessage());
        }
    }
}
