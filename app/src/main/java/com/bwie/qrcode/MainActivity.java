package com.bwie.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions//生成工具类
public class MainActivity extends AppCompatActivity implements QRCodeView.Delegate {

    @BindView(R.id.hello)
    TextView hello;

    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hello.setText("调用相机");


        init();
    }

    private void init() {
        mQRCodeView.setDelegate(this);
    }

    /**
     * 申请权限
     */
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityPermissionsDispatcher.showCameraWithCheck(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQRCodeView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.hello)
    void click(View view) {//调用

    }

    /**
     * 权限使用说明
     *
     * @param request
     */
    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }


    /**
     * 拒绝访问
     */
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    /**
     * 不再询问
     */
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(this, "不再询问", Toast.LENGTH_SHORT).show();
    }


    /**
     * 覆盖实现
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        hello.setText(result);
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
