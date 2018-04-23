package app.demoproduct.com.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;
import app.demoproduct.com.Utills.Helper;
import app.demoproduct.com.Utills.ServiceGeneratorr;
import app.demoproduct.com.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";
    AppPreference mAppPreference;
    EditText signin_email, signin_password;
    TextView tv_forgotPWD, signUpForFreeTv;
    LinearLayout loginSubmitButton;
    String UserName, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        onClickHandle();

    }

    private void init() {
        mAppPreference = new AppPreference(MainActivity.this);

        if (mAppPreference != null) {

            if (mAppPreference.isUserLogin()) {
                Intent intent = new Intent(MainActivity.this, MainProductScreenActivity.class);
                startActivity(intent);
                finish();
            } else {

            }
        }
    }

    private void initView() {
        signin_email = (EditText) findViewById(R.id.signin_email);
        signin_password = (EditText) findViewById(R.id.signin_password);

        tv_forgotPWD = (TextView) findViewById(R.id.tv_forgotPWD);
        signUpForFreeTv = (TextView) findViewById(R.id.signUpForFreeTv);

        loginSubmitButton = (LinearLayout) findViewById(R.id.loginSubmitButton);

    }

    private void onClickHandle() {
        loginSubmitButton.setOnClickListener(this);
        tv_forgotPWD.setOnClickListener(this);
        signUpForFreeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginSubmitButton:
                verify();
                break;
            case R.id.tv_forgotPWD:
                Toast.makeText(getApplicationContext(), "Forgot Passowrd..?", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signUpForFreeTv:
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);

                break;

        }
    }

    private void verify() {

        boolean flag = true;
        UserName = signin_email.getText().toString();
        Password = signin_password.getText().toString();

        if (UserName.isEmpty()) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Please enter Email Or Mobile Number", Toast.LENGTH_SHORT).show();

        } else if (Password.isEmpty()) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (UserName.length() <= 9) {
            flag = false;
            Toast.makeText(getApplicationContext(), "You Must have 10 Digits in Your Mobile No.", Toast.LENGTH_SHORT).show();
        }


        if (flag) {

            if (Helper.isNetworkConnected(getApplicationContext())) {

                if (flag) {
                    loginTheUser(UserName, Password);
                }

            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void loginTheUser(String userName, String password) {
        Call<LoginResponce> call;

        String regex = "-?\\d+(\\.\\d+)?";
        boolean flag = true;

        if (userName.matches(regex)) {
            flag = false;
            Log.e("enteredID value", "enterdID is numeric!!!!!!!!!!!^^^");
            Log.e("enteredID value", userName);
        } else {
            flag = true;
            Log.e("enteredID value", "enterdID isn't numeric!!!!!!!!!!!^^^");
            Log.e("enteredID value", userName);
        }

        Helper.showProgressBar(MainActivity.this, getResources().getString(R.string.pleasewait));

        ApiInterface restInterface = ServiceGeneratorr.createService(ApiInterface.class);

        if (flag) {
            call = restInterface.userLogin("", userName, password);
        } else {
            call = restInterface.userLogin(userName, "", password);
        }

        call.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.e(TAG, "onResponse: " + response.code());

                Helper.hideProgressBar();

                if (response.body() != null) {
                    boolean responseStatus = response.body().getStatus();
                    if (responseStatus) {
                        LoginResponce.User userObject = response.body().getmUser();

                        mAppPreference.setUserLogin(true);

                        Intent intent = new Intent(MainActivity.this, MainProductScreenActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.server_connection_problem), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                Log.e("Login_fail ", t.toString());
                Helper.hideProgressBar();
            }
        });

    }
}
