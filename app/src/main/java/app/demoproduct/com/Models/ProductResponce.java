package app.demoproduct.com.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 19-04-2018.
 */

public class ProductResponce {

    @SerializedName("data")
    @Expose
    private ProductDetails mProductDetails;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public class ProductDetails {
        @SerializedName("p_id")
        @Expose
        private String p_id;
        @SerializedName("p_name")
        @Expose
        private String p_name;
        @SerializedName("p_description")
        @Expose
        private String p_description;
        @SerializedName("p_branch")
        @Expose
        private String p_branch;
        @SerializedName("p_qty")
        @Expose
        private String p_qty;
        @SerializedName("p_image")
        @Expose
        private String p_image;

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getP_description() {
            return p_description;
        }

        public void setP_description(String p_description) {
            this.p_description = p_description;
        }

        public String getP_branch() {
            return p_branch;
        }

        public void setP_branch(String p_branch) {
            this.p_branch = p_branch;
        }

        public String getP_qty() {
            return p_qty;
        }

        public void setP_qty(String p_qty) {
            this.p_qty = p_qty;
        }

        public String getP_image() {
            return p_image;
        }

        public void setP_image(String p_image) {
            this.p_image = p_image;
        }
    }

    public ProductDetails getmProductDetails() {
        return mProductDetails;
    }

    public void setmProductDetails(ProductDetails mProductDetails) {
        this.mProductDetails = mProductDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
