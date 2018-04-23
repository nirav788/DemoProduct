package app.demoproduct.com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.demoproduct.com.Activity.ProductDetailsActivity;
import app.demoproduct.com.Models.ProductList.Products;
import app.demoproduct.com.R;

/**
 * Created by EbitM6 on 12-01-2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    public static final String TAG = "PortfolioImageAdapter";
    private final LayoutInflater inflater;
    private final Context mContext;
    ArrayList<Products> mProducts;


    public ProductAdapter(Context mContext, ArrayList<Products> products) {
        inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mProducts = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_product_list, parent, false);
        MyViewHolder holer = new MyViewHolder(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Products product = mProducts.get(position);

        if (product.getP_qty() != null || product.getP_qty().length() > 0) {
            holder.tv_qty.setText(product.getP_qty());
        } else {
            holder.tv_qty.setText("");
        }
        if (product.getP_branch() != null || product.getP_branch().length() > 0) {
            holder.tv_branch.setText(product.getP_branch());
        } else {
            holder.tv_branch.setText("");
        }
        if (product.getP_description() != null || product.getP_description().length() > 0) {
            holder.tv_product_desc.setText(product.getP_description());
        } else {
            holder.tv_product_desc.setText("");
        }
        if (product.getP_name() != null || product.getP_name().length() > 0) {
            holder.tv_product_name.setText(product.getP_name());
        } else {
            holder.tv_product_name.setText("");
        }


        if (product.getP_image().toString().length() > 0) {

            Glide.with(mContext).load("http://shivamsoftwareservices.com/demo_app/upload/" + product.getP_image().toString()).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(holder.iv_product);
        } else {
            holder.iv_product.setBackgroundResource(R.mipmap.ic_launcher);
        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("getP_id", mProducts.get(position).getP_id());
                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
            }
        });

        // holder.iv_thumbnail.setImageResource(R.drawable.imgprofile);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_product;
        private final TextView tv_qty, tv_branch, tv_product_desc, tv_product_name;
        LinearLayout ll_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            tv_branch = (TextView) itemView.findViewById(R.id.tv_branch);
            tv_product_desc = (TextView) itemView.findViewById(R.id.tv_product_desc);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            ll_main = (LinearLayout) itemView.findViewById(R.id.ll_main);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}