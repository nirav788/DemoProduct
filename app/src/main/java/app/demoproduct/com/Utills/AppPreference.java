package app.demoproduct.com.Utills;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EbiM10 on 6/17/2016.
 */
public class AppPreference {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREFERENCE_NAME = "DemoApp";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String IS_USER_LOGIN_FIRST = "is_user_login_first";
    private String UpdateType = "updatetype";


    public AppPreference(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void setUserFname(String Fname) {
        mEditor.putString(KEY_FNAME, Fname).commit();
    }

    public String getUserFname() {
        return mSharedPreferences.getString(KEY_FNAME, null);
    }

    public void setUserLname(String Lname) {
        mEditor.putString(KEY_LNAME, Lname).commit();
    }


    public boolean isUserLogin() {
        return mSharedPreferences.getBoolean(IS_USER_LOGIN_FIRST, false);
    }

    public void setUserLogin(boolean useLogin) {
        mEditor.putBoolean(IS_USER_LOGIN_FIRST, useLogin).commit();
    }

    public void setPermission(String KEY_PERMISSION, boolean login) {
        mEditor.putBoolean(KEY_PERMISSION, login).commit();
    }

    public boolean getPermission(String KEY_PERMISSION) {
        return mSharedPreferences.getBoolean(KEY_PERMISSION, false);

    }

    public void setIsUpdateType(boolean IsUpdateType) {
        mEditor.putBoolean(UpdateType, IsUpdateType).commit();
    }

    public boolean getIsUpdateType() {
        return mSharedPreferences.getBoolean(UpdateType, true);
    }

    public void ClearSharedpreference() {
        this.mEditor.clear();
        this.mEditor.commit();
    }


}
