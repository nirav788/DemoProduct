package app.demoproduct.com.Utills;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.demoproduct.com.R;


/**
 * Created by EbitW012 on 12/10/2017.
 */

public class Helper {
    private static ConnectionDetector cd;
    private static boolean isInternetPresent;
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String PUSH_NOTIFICATION = "pushNotification";
    static ProgressDialog pDialog;
    private static String TAG = "Helper";

    // Method for Check Internet Connetoion
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else {
            return true;
        }

    }

    public static void showSnackBar(Context context, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void hideKeyboard(Activity activity, AppCompatEditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    /* Show ProgressDialog */
    public static void showProgressBar(Context mContext, String message) {

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /* dismiss ProgressDialog */

    public static void hideProgressBar() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    public static String formatNotification(String date) {
//        2017-12-23 05:19:44
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = "";

        try {
            Date mDate = sdf.parse(date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm, dd/MM");
            strDate = sdf2.format(mDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDate;
    }


    public static void openGmail(Activity activity, String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        activity.startActivity(emailIntent);
    }

    public static File convertBitmapToFile(Bitmap bitmap) {
        String filename = System.currentTimeMillis() + ".png";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Log.e(TAG, "convertBitmapToFile: " + byteArray.length);
        return storeImage(byteArray, filename);
    }

    public static byte[] convertFiletoByte(File file) {
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
           /* for (int i = 0; i < b.length; i++) {
                System.out.print((char)b[i]);
            }*/
        } catch (FileNotFoundException e) {
            b = null;
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            b = null;
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }

    static File storeImage(byte[] imageData, String filename) {
        //get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/lookforsuccess";

        File root = new File(iconsStoragePath);
        if (!root.mkdirs()) {
            Log.e("Test", "This path is already exist: " + root.getAbsolutePath());
        }

        File file = new File(root + filename);

        Log.e(TAG, "storeImage: " + file.getAbsolutePath());
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Log.e("Test", "This file is already exist: " + file.getAbsolutePath());
                }
                try {
                    String filePath = file.getAbsolutePath();
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                    bos.write(imageData, 0, imageData.length);
                    bos.flush();
                    bos.close();

                } catch (FileNotFoundException e) {
                    Log.e("TAG", "Error saving image file: " + e.getMessage());
                    file = null;
                    ///  return false;
                } catch (IOException e) {
                    Log.e("TAG", "Error saving image file: " + e.getMessage());
                    file = null;
                    //return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        } catch (Exception e) {

        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap Base64ToBitmap(String myImageData) {
        byte[] imageAsBytes = Base64.decode(myImageData.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static void setGlide(String url, ImageView img, Context context) {

        try {
            Glide.with(context).load(url)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.placeholder(R.drawable.imgprofile)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  public static void setGlide(String url, ImageView img, Context context) {

        try {
            Glide.with(context).load(url)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.placeholder(R.drawable.imgprofile)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* public static void setGlideBlur(final Context context, String url, final ImageView img) {

        Glide.with(context).load(url)
                .asBitmap().fitCenter()
                .error(R.drawable.imgprofile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Blurry.with(context).radius(30).sampling(1).from(resource).into(img);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        img.setImageDrawable(context.getResources().getDrawable(R.drawable.imgprofile));
                    }
                });
    }*/


    public static String getDateFromMillis(long time) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat;
        if (time < (currentTime - TimeUnit.DAYS.toMillis(100))) {
            dateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        } else if (!DateUtils.isToday(time)) {
            dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        } else {
            dateFormat = new SimpleDateFormat("hh:mm a");
            // return "Today";
        }
        return dateFormat.format(new Date(time));
    }

    public static String getsDateFromMillis(long timestamp) {
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MMM dd, yyyy @ HH:mm");
        return dateFormat.format(new Date(timestamp));
    }
}
