package app.demoproduct.com.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;

public class MainProductScreenActivity extends AppCompatActivity implements View.OnClickListener {

    AppPreference mAppPreference;
    LinearLayout ll_add_product, ll_view_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_product_screen);

        init();
        initView();
        onClickHandle();
    }

    private void init() {
        mAppPreference = new AppPreference(MainProductScreenActivity.this);
    }

    private void initView() {

        ll_add_product = (LinearLayout) findViewById(R.id.ll_add_product);
        ll_view_product = (LinearLayout) findViewById(R.id.ll_view_product);
    }

    private void onClickHandle() {
        ll_add_product.setOnClickListener(this);
        ll_view_product.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.ll_add_product:
                intent = new Intent(MainProductScreenActivity.this, AddNewItemActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_view_product:
                intent = new Intent(MainProductScreenActivity.this, ViewAllProductActivity.class);
                startActivity(intent);

                break;
        }
    }
}
