package com.lixue.app.common.logic;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.lixue.app.R;
import com.lixue.app.common.constants.FilePathConstants;
import com.lixue.app.common.constants.RequestConstantCode;
import com.lixue.app.library.util.BitmapUtil;
import com.lixue.app.library.util.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

import eu.janmuller.android.simplecropimage.CropImage;


/**
 * 图片选择、拍摄、裁剪类
 * onCorpCompleteListener  为裁剪回调
 */
public class PhotoCorp implements RequestConstantCode {

    private Activity mActivity;
    private Uri imageUri;
    private onCorpCompleteListener mCorpCompleteListener;
    private Flag mFlag = Flag.ADD;
    private int aspectX;
    private int aspectY;

    private int out_putx = 640;
    private int out_puty = 640;

    public enum Flag {
        UPDATE, ADD, FRONT_IMG, BACK_IMG, HOLD_IMG
    }

    public interface onCorpCompleteListener {
        void onCorpComplete(Flag flag, byte[] imageByte);
    }

    public void addCorpCompleteListener(onCorpCompleteListener listener) {
        mCorpCompleteListener = listener;
    }

    public void setOutX(int out_putx) {
        this.out_putx = out_putx;
    }

    public void setOutY(int out_puty) {
        this.out_puty = out_puty;
    }

    public PhotoCorp(Activity activity) {
        mActivity = activity;
    }

    public void start(Flag flag) {
        mFlag = flag;
        imageUri = Uri.parse("file://"
                + FilePathConstants.getAvatarPhotosPath(mActivity)
                + File.separator + String.valueOf(System.currentTimeMillis())
                + ".jpg");
        Log.d("enlong", "tempFilePath" + imageUri.getPath());
        openChoosePhotoDialog(mActivity, imageUri, 1, 1);
    }

    private void openChoosePhotoDialog(Activity mActivity, Uri imageUri, int x, int y) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
        builder.setTitle("选择图片");
        builder.setItems(new String[]{"相机拍摄", "相册选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0: {
                        if (PermissionUtil.checkPermissions(mActivity, Manifest.permission.CAMERA)) {
                            try {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                mActivity.startActivityForResult(intent,
                                        PICK_CAMERA_RESULT_X_Y);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(mActivity, R.string.choose_photo_not_find_device, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mActivity);
                            builder1.setTitle("权限提示");
                            builder1.setMessage("使用该功能需要摄像头权限, 请在应用详情中允许该权限");
                            builder1.setNegativeButton("取消", null);

                            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PermissionUtil.startAppSettings(mActivity);
                                }
                            });

                            builder1.show();
                        }
                    }
                    break;

                    case 1:
                        try {
                            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
                            if (isKitKat) {
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                mActivity.startActivityForResult(intent,
                                        PICK_GALLERY_RESULT_KitKat_X_Y);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                                        null);
                                intent.setType("image/*");
                                intent.putExtra("crop", "true");
                                intent.putExtra("aspectX", aspectX);
                                intent.putExtra("aspectY", aspectY);
                                intent.putExtra("outputX", out_putx);
                                intent.putExtra(
                                        "outputY",
                                        (int) ((out_puty * (float) ((float) aspectY / (float) aspectX))));
                                intent.putExtra("scale", true);
                                intent.putExtra("return-data", false);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                intent.putExtra("outputFormat",
                                        Bitmap.CompressFormat.JPEG.toString());
                                intent.putExtra("noFaceDetection", true);
                                mActivity.startActivityForResult(intent,
                                        PICK_GALLERY_RESULT);
                            }
                        } catch (ActivityNotFoundException e) {

                            Toast.makeText(mActivity, R.string.choose_photo_no_album, Toast.LENGTH_SHORT).show();
                        }
                        break;

                }

            }
        });


        builder.show();

    }


    /**
     *  选择图片
     * @param flag
     */
    public void picture(Flag flag, int aspectX, int aspectY) {
        mFlag = flag;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        out_puty = (int) (out_putx * (float) ((float) aspectY / (float) aspectX));


        imageUri = Uri.parse("file://"
                + FilePathConstants.getAvatarPhotosPath(mActivity)
                + File.separator + String.valueOf(System.currentTimeMillis())
                + ".jpg");
        Log.d("enlong", "tempFilePath" + imageUri.getPath());
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            if (isKitKat) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mActivity.startActivityForResult(intent,
                        PICK_GALLERY_RESULT_KitKat_X_Y);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", out_putx);
                intent.putExtra(
                        "outputY",
                        out_puty);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                mActivity.startActivityForResult(intent,
                        PICK_GALLERY_RESULT);
            }
        } catch (ActivityNotFoundException e) {

            Toast.makeText(mActivity, R.string.choose_photo_no_album, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 拍摄图片
     * @param flag
     * @param aspectX
     * @param aspectY
     */
    public void camera(Flag flag, int aspectX, int aspectY) {
        mFlag = flag;
        out_puty = (int) (out_putx * (float) ((float) aspectY / (float) aspectX));
        this.aspectX = aspectX;
        this.aspectY = aspectY;

        imageUri = Uri.parse("file://"
                + FilePathConstants.getAvatarPhotosPath(mActivity)
                + File.separator + String.valueOf(System.currentTimeMillis())
                + ".jpg");
        CLog.d("enlong", "tempFilePath" + imageUri.getPath());
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(intent,
                    PICK_CAMERA_RESULT_X_Y);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, R.string.choose_photo_not_find_device, Toast.LENGTH_LONG).show();
        }
    }


    /**
     *
     * @param flag
     * @param aspectX
     * @param aspectY
     */
    public void start(Flag flag, int aspectX, int aspectY) {
        mFlag = flag;
        this.aspectX = aspectX;
        this.aspectY = aspectY;

        out_puty = (int) (out_putx * (float) ((float) aspectY / (float) aspectX));


        imageUri = Uri.parse("file://"
                + FilePathConstants.getAvatarPhotosPath(mActivity)
                + File.separator + String.valueOf(System.currentTimeMillis())
                + ".jpg");

        openChoosePhotoDialog(mActivity, imageUri,
                aspectX, aspectY);
    }

    public Uri getImageUri() {
        return imageUri;
    }

    // 调用系统剪切图片程序
    private void cropImageUri(Uri uri, int requestCode) {
        startCropImage(uri, requestCode);
    }


    private void cropImageUri(Uri uri, int requestCode, int aspectX, int aspectY) {
        Intent intent = new Intent(mActivity, CropImage.class);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(CropImage.IMAGE_PATH, uri.getPath());
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", out_putx);
        intent.putExtra(
                "outputY", out_puty);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        mActivity.startActivityForResult(intent, requestCode);

    }

    private void startCropImage(Uri uri, int requestCode) {

        Intent intent = new Intent(mActivity, CropImage.class);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(CropImage.IMAGE_PATH, uri.getPath());
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", out_putx);
        intent.putExtra("outputY", out_puty);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("enlong", "action.CROP result:" + resultCode);

        if (resultCode != Activity.RESULT_OK) {// result is not correct

            return;
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        switch (requestCode) {
            case PICK_GALLERY_RESULT:

                try {
                    if (imageUri != null) {
                        CLog.i("zeng", "imageUri:" + imageUri);
                        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(mActivity,
                                imageUri);
                        if (bitmap == null) {
                            return;
                        }
                        byteStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                        byte[] avatarByte = byteStream.toByteArray();
                        if (mCorpCompleteListener != null) {
                            mCorpCompleteListener.onCorpComplete(mFlag, avatarByte);
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            case PICK_GALLERY_RESULT_KitKat:

                try {
                    if (imageUri != null && data != null) {
                        CLog.i("zeng", "imageUri:" + imageUri);
                        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(mActivity, data.getData());
                        // 保存到
                        saveMyBitmap(imageUri.getPath(), bitmap);
                        cropImageUri(imageUri, CROP_PICTURE);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            case PICK_CAMERA_RESULT:
                if (imageUri != null) {
                    cropImageUri(imageUri, CROP_PICTURE);
                } else {
                    CLog.i("zeng", "imageUri==null");
                }
                break;
            case PICK_GALLERY_RESULT_KitKat_X_Y:
                try {
                    if (imageUri != null && data != null) {
                        CLog.i("zeng", "imageUri:" + imageUri);
                        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(mActivity, data.getData());
                        // 保存到
                        saveMyBitmap(imageUri.getPath(), bitmap);
                        cropImageUri(imageUri, CROP_PICTURE, aspectX, aspectY);
                    }
                } catch (Exception e) {
                    CLog.i("zeng", "imageUri:Exception" + e.getMessage());
                }
                break;
            case PICK_CAMERA_RESULT_X_Y:
                if (imageUri != null) {
                    cropImageUri(imageUri, CROP_PICTURE, aspectX,
                            aspectY);
                } else {
                    CLog.i("zeng", "imageUri==null");
                }
                break;
            case CROP_PICTURE:
                if (imageUri != null) {
                    Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(mActivity,
                            imageUri);
                    if (bitmap == null) {
                        return;
                    }
                    byteStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                    byte[] avatarByte = byteStream.toByteArray();
                    if (mCorpCompleteListener != null) {
                        mCorpCompleteListener.onCorpComplete(mFlag, avatarByte);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void saveMyBitmap(String path, Bitmap mBitmap) {
        BitmapUtil.saveImg2File(mBitmap, path);
    }

}
