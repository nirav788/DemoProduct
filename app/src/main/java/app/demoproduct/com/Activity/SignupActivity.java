package app.demoproduct.com.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;
import app.demoproduct.com.Utills.Helper;
import app.demoproduct.com.Utills.ServiceGeneratorr;
import app.demoproduct.com.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    AppPreference mAppPreference;
    TextView signUpForFreeTv;
    EditText edt_fname, edt_lname, edt_mnumber, edt_email, edt_password;
    String UserFname, UserLname, UserMnumber, UserEmail, UserPassword;
    LinearLayout ll_SubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        initView();
        onClickHandle();
    }

    private void init() {

        mAppPreference = new AppPreference(SignupActivity.this);
    }

    private void initView() {
        signUpForFreeTv = (TextView) findViewById(R.id.signUpForFreeTv);

        edt_fname = (EditText) findViewById(R.id.edt_fname);
        edt_lname = (EditText) findViewById(R.id.edt_lname);
        edt_mnumber = (EditText) findViewById(R.id.edt_mnumber);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);

        ll_SubmitButton = (LinearLayout) findViewById(R.id.ll_SubmitButton);

    }

    private void onClickHandle() {
        signUpForFreeTv.setOnClickListener(this);
        ll_SubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_SubmitButton:
                verify();
                break;
            case R.id.signUpForFreeTv:
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void verify() {

        boolean flag = true;
        UserFname = edt_fname.getText().toString();
        UserLname = edt_lname.getText().toString();
        UserMnumber = edt_mnumber.getText().toString();
        UserEmail = edt_email.getText().toString();
        UserPassword = edt_password.getText().toString();

        if (UserFname.isEmpty()) {
            flag = false;
            showErrorMessage("Enter First Name");
        } else if (UserLname.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Last Name");
        } else if (UserMnumber.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Mobile Number");
        } else if (UserEmail.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Email Address");
        } else if (UserPassword.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Password");
        } else if (UserMnumber.length() < 10) {
            flag = false;
            Toast.makeText(getApplicationContext(), "You Must have 10 Digits in Your Mobile No.", Toast.LENGTH_SHORT).show();
        } else if (UserPassword.length() < 4) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Password Must be Between 4 to 20 Characters.", Toast.LENGTH_SHORT).show();
        }


        if (flag) {

            RegisterUser(UserFname, UserLname, UserMnumber, UserEmail, UserPassword);
        }
    }

    private void RegisterUser(String userFname, String userLname, String userMnumber, String userEmail, String userPassword) {
        Helper.showProgressBar(SignupActivity.this, getResources().getString(R.string.pleasewait));

        ApiInterface restInterface = ServiceGeneratorr.createService(ApiInterface.class);


        Call<LoginResponce> call = restInterface.userRegistration(userFname, userLname, userMnumber, userEmail, userPassword);

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
                        Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignupActivity.this, MainProductScreenActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_connection_problem), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                Log.e("Login_fail ", t.toString());
                Helper.hideProgressBar();

            }
        });

    }

    public void showErrorMessage(String msg) {
        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
