package com.lixue.app;

import android.content.Intent;

import com.lixue.app.common.constants.RequestConstantCode;
import com.lixue.app.common.logic.PhotoCorp;
import com.lixue.app.library.base.BaseActivity;

/**
 * Created by enlong on 2017/2/4.
 */
public class MyActivity extends BaseActivity implements RequestConstantCode {
    PhotoCorp mPhotoCrop;


    /**
     * toast dialog to choose photo
     * x : y  just like 4 : 3
     */
    public void showToGetPhoto(int x, int y){
        if(null == mPhotoCrop) mPhotoCrop = new PhotoCorp(this);

        mPhotoCrop.start(PhotoCorp.Flag.UPDATE, x, y);

    }

    /**
     * toast dialog to choose photo
     * x : y  just like 4 : 3
     * out_x  real out bitmap x
     * out_y  real out bitmap y
     */
    public void showToGetPhoto(int x, int y, int out_x, int out_y){
        if(null == mPhotoCrop) mPhotoCrop = new PhotoCorp(this);
        mPhotoCrop.setOutX(out_x);
        mPhotoCrop.setOutY(out_y);
        mPhotoCrop.start(PhotoCorp.Flag.UPDATE, x, y);

    }

    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // take or crop pic
            case PICK_GALLERY_RESULT:
            case PICK_GALLERY_RESULT_KitKat:
            case PICK_CAMERA_RESULT:
            case PICK_GALLERY_RESULT_KitKat_X_Y:
            case PICK_CAMERA_RESULT_X_Y:
            case CROP_PICTURE:
                if (mPhotoCrop != null) {
                    mPhotoCrop.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;

        }

    }
}
