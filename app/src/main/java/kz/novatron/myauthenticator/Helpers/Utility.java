package kz.novatron.myauthenticator.Helpers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by SMustafa on 28.07.2017.
 */

public class Utility {
    public static final int PERMISSIONS_REQUEST_CAMERA = 200;
    public static final int INVALID_QR_CODE = 1;
    public static final int INVALID_SECRET_IN_QR_CODE = 2;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionCamera(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
