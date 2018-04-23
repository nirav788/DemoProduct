package app.demoproduct.com.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.demoproduct.com.Models.ProductResponce;
import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;
import app.demoproduct.com.Utills.Helper;
import app.demoproduct.com.Utills.ServiceGeneratorr;
import app.demoproduct.com.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailsActivity";
    String QuestionID;
    AppPreference mAppPreference;
    ImageView iv_viewPic;
    TextView tv_product_name, tv_branch, tv_qty, tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        if (intent.hasExtra("getP_id")) {
            QuestionID = intent.getExtras().getString("getP_id");
            Log.e(TAG, "getP_id: " + QuestionID);
        } else {
            QuestionID = "";
        }
        init();
        initView();
        onClickHandle();
        getProductDetails(QuestionID);
    }

    private void init() {
        mAppPreference = new AppPreference(ProductDetailsActivity.this);
    }

    private void initView() {

        iv_viewPic = (ImageView) findViewById(R.id.iv_viewPic);

        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_branch = (TextView) findViewById(R.id.tv_branch);
        tv_qty = (TextView) findViewById(R.id.tv_qty);
        tv_description = (TextView) findViewById(R.id.tv_description);


    }

    private void onClickHandle() {
    }

    private void getProductDetails(String questionID) {
        Helper.showProgressBar(ProductDetailsActivity.this, getResources().getString(R.string.pleasewait));

        ApiInterface restInterface = ServiceGeneratorr.createService(ApiInterface.class);


        Call<ProductResponce> call = restInterface.ProductDetails(questionID);

        call.enqueue(new Callback<ProductResponce>() {
            @Override
            public void onResponse(Call<ProductResponce> call, Response<ProductResponce> response) {
                Log.e(TAG, "onResponse: " + response.code());

                Helper.hideProgressBar();


                if (response.body() != null) {
                    boolean responseStatus = response.body().getStatus();
                    if (responseStatus) {
                        ProductResponce.ProductDetails productDetails = response.body().getmProductDetails();
                        if (productDetails.getP_image().toString().length() > 0) {

                            Glide.with(ProductDetailsActivity.this).load(productDetails.getP_image().toString()).placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher).into(iv_viewPic);
                        } else {
                            iv_viewPic.setBackgroundResource(R.mipmap.ic_launcher);
                        }
                        if (productDetails.getP_description() != null || productDetails.getP_description().length() > 0) {
                            tv_description.setText(productDetails.getP_description());
                        } else {
                            tv_description.setText("");
                        }
                        if (productDetails.getP_name() != null || productDetails.getP_name().length() > 0) {
                            tv_product_name.setText(productDetails.getP_name());
                        } else {
                            tv_product_name.setText("");
                        }
                        if (productDetails.getP_branch() != null || productDetails.getP_branch().length() > 0) {
                            tv_branch.setText(productDetails.getP_branch());
                        } else {
                            tv_branch.setText("");
                        }
                        if (productDetails.getP_qty() != null || productDetails.getP_qty().length() > 0) {
                            tv_qty.setText(productDetails.getP_qty());
                        } else {
                            tv_qty.setText("");
                        }

                    } else {
                        showErrorMessage(response.body().getMessage());
                        Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showErrorMessage(getResources().getString(R.string.server_connection_problem));

                }

            }

            @Override
            public void onFailure(Call<ProductResponce> call, Throwable t) {
                Log.e("Login_fail ", t.toString());
                Helper.hideProgressBar();

            }
        });

    }

    public void showErrorMessage(String msg) {
        Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
