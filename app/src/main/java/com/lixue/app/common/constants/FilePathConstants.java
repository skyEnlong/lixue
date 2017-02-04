package com.lixue.app.common.constants;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by enlong on 2017/2/4.
 */

public class FilePathConstants {
    /* 获取SD卡目录 */
    private static final String SD_FILE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private static final String Photo_IMG_PATH = SD_FILE_PATH + File.separator
            + "Lixue" + File.separator + "photos";


    /**
     * 头像存储路径
     * @param context
     * @return
     */
    public static String getAvatarPhotosPath(Context context) {
        File path;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = new File(Photo_IMG_PATH + File.separator + "avater");
        } else {
            String appPath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath();
            path = new File(appPath + File.separator + "photos"
                    + File.separator + "avater");
        }
        if (!path.exists()) {
            path.mkdirs();
        }

        return path.getAbsolutePath();
    }

}
