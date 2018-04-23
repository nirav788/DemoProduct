package app.demoproduct.com;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Developer on 15-04-2018.
 */

public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    public static Typeface pb_typeface, pm_typeface, pr_typeface, psb_typeface;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        pb_typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/pb.ttf");
        pm_typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/pm.ttf");
        pr_typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/pr.ttf");
        psb_typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/psb.ttf");
        mContext = getApplicationContext();
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyApplication(Context context)
    {
        mContext = context;
    }

    public MyApplication()
    {
    }

    public static  Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
