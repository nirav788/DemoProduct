package app.demoproduct.com.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.demoproduct.com.Adapters.ProductAdapter;
import app.demoproduct.com.Models.ProductList;
import app.demoproduct.com.Models.ProductList.Products;
import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;
import app.demoproduct.com.Utills.Helper;
import app.demoproduct.com.Utills.ServiceGeneratorr;
import app.demoproduct.com.interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllProductActivity extends AppCompatActivity {

    private ArrayList<Products> mProducts = new ArrayList<>();
    RecyclerView rv_product_list;
    AppPreference mAppPreference;
    ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_product);

        init();
        initView();
        onClickHandle();
        getAllProducts();


    }

    private void getAllProducts() {
        ApiInterface restInterface = ServiceGeneratorr.createService(ApiInterface.class);
        Call<ProductList> call = restInterface.getAllProduct();
        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                Helper.hideProgressBar();

                if (response.body() != null) {
                    boolean successCode = response.body().getStatus();
                    if (successCode) {
                        mProducts = response.body().getmProducts();
                        SetAdapter();
                    } else {
                        showErrorMessage(response.body().getMessage());
                    }
                } else {
                    showErrorMessage(getResources().getString(R.string.server_connection_problem));
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Log.e("error", t.toString());
                Helper.hideProgressBar();
            }
        });
    }

    private void init() {
        mAppPreference = new AppPreference(ViewAllProductActivity.this);
    }

    private void initView() {

        rv_product_list = (RecyclerView) findViewById(R.id.rv_product_list);
    }

    private void onClickHandle() {
    }

    private void SetAdapter() {
        rv_product_list.setHasFixedSize(true);
        LinearLayoutManager llmm = new LinearLayoutManager(ViewAllProductActivity.this);
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_product_list.setLayoutManager(llmm);
        mAdapter = new ProductAdapter(ViewAllProductActivity.this, mProducts);
        rv_product_list.setAdapter(mAdapter);
    }

    public void showErrorMessage(String msg) {
        Toast.makeText(ViewAllProductActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
