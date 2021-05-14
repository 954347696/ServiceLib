package com.keepfun.imageselector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.keepfun.aiservice.R;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.imageselector.utils.ImageSelector;
import com.keepfun.imageselector.utils.ImageUtil;
import com.keepfun.imageselector.utils.StringUtils;
import com.keepfun.imageselector.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片裁剪页面<p>
 *
 * @author zixuefei
 * @since 2019/7/31 17:51
 */
public class ClipImageActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
    private FrameLayout btnConfirm;
    private FrameLayout btnBack;
    private CropImageView cropImageView;
    private int mRequestCode;
    private boolean isCameraImage;
    private boolean isCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_clip_image);

        initView();
    }

    /**
     * 修改状态栏颜色
     */
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#373c3d"));
        }
    }

    private void initView() {
        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra("requestCode", 0);
        isCircle = intent.getBooleanExtra(ImageSelector.IS_CIRCLE, false);
        setStatusBarColor();
        ImageSelectorActivity.openActivity(this, mRequestCode, true,
                intent.getBooleanExtra(ImageSelector.IS_VIEW_IMAGE, true),
                intent.getBooleanExtra(ImageSelector.USE_CAMERA, true), 0,
                intent.getStringArrayListExtra(ImageSelector.SELECTED));

        cropImageView = findViewById(R.id.process_img);
        btnConfirm = (FrameLayout) findViewById(R.id.btn_confirm);
        btnBack = (FrameLayout) findViewById(R.id.btn_back);

        cropImageView.setFocusStyle(isCircle ? CropImageView.Style.CIRCLE : CropImageView.Style.RECTANGLE);
        btnConfirm.setOnClickListener(new CheckClickListener(this));
        btnBack.setOnClickListener(new CheckClickListener(this));

        cropImageView.setOnBitmapSaveCompleteListener(new CropImageView.OnBitmapSaveCompleteListener() {
            @Override
            public void onBitmapSaveSuccess(File file) {
                if (file == null) {
                    Log.e("image", "file save failed");
                    return;
                }
                Log.d("image", "path:" + file.getAbsolutePath());
                confirm(file.getAbsolutePath());
            }

            @Override
            public void onBitmapSaveError(File file) {
                Log.e("image", "------file save failed-----");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == mRequestCode) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            if (images == null || images.size() == 0) {
                finish();
                return;
            }
            Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(images.get(0), 720, 1280);
            if (bitmap != null) {
                cropImageView.setImageBitmap(bitmap);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void confirm(String imagePath) {
//        String imagePath = null;
//        if (bitmap != null) {
//            imagePath = ImageUtil.saveImage(bitmap, getCacheDir().getPath() + File.separator + "image_select");
//            bitmap.recycle();
//            bitmap = null;
//        }

        if (StringUtils.isNotEmptyString(imagePath)) {
            ArrayList<String> selectImages = new ArrayList<>();
            selectImages.add(imagePath);
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ImageSelector.SELECT_RESULT, selectImages);
            intent.putExtra(ImageSelector.IS_CAMERA_IMAGE, isCameraImage);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public static void openActivity(Activity context, int requestCode, boolean isViewImage,
                                    boolean useCamera, boolean isCircle, ArrayList<String> selected) {
        Intent intent = new Intent(context, ClipImageActivity.class);
        intent.putExtras(dataPackages(requestCode, isViewImage, useCamera, isCircle, selected));
        context.startActivityForResult(intent, requestCode);
    }

    public static void openActivity(Fragment fragment, int requestCode, boolean isViewImage,
                                    boolean useCamera, boolean isCircle, ArrayList<String> selected) {
        Intent intent = new Intent(fragment.getContext(), ClipImageActivity.class);
        intent.putExtras(dataPackages(requestCode, isViewImage, useCamera, isCircle, selected));
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void openActivity(android.app.Fragment fragment, int requestCode, boolean isViewImage,
                                    boolean useCamera, boolean isCircle, ArrayList<String> selected) {
        Intent intent = new Intent(fragment.getActivity(), ClipImageActivity.class);
        intent.putExtras(dataPackages(requestCode, isViewImage, useCamera, isCircle, selected));
        fragment.startActivityForResult(intent, requestCode);
    }

    public static Bundle dataPackages(int requestCode, boolean isViewImage, boolean useCamera, boolean isCircle,
                                      ArrayList<String> selected) {
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", requestCode);
        bundle.putBoolean(ImageSelector.IS_VIEW_IMAGE, isViewImage);
        bundle.putBoolean(ImageSelector.USE_CAMERA, useCamera);
        bundle.putBoolean(ImageSelector.IS_CIRCLE, isCircle);
        bundle.putStringArrayList(ImageSelector.SELECTED, selected);
        return bundle;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            if (cropImageView.getDrawable() != null) {
                btnConfirm.setEnabled(false);
                File dir = new File(SAVE_PATH + getPackageName() + "/");
                cropImageView.saveBitmapToFile(dir, !isCircle);
            }
        }
        if (v.getId() == R.id.btn_back) {
            finish();
        }
    }
}
