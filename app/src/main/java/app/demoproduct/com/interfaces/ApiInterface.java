package app.demoproduct.com.interfaces;

import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.Models.ProductList;
import app.demoproduct.com.Models.ProductResponce;
import app.demoproduct.com.Models.RegistrationResponce;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Developer on 16-04-2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php/APIs")
    Call<LoginResponce> userLogin(
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("signup.php/APIs")
    Call<LoginResponce> userRegistration(
            @Field("fname") String userFname,
            @Field("lname") String userLname,
            @Field("mobile") String userMnumber,
            @Field("email") String userEmail,
            @Field("password") String userPassword);

    @Multipart
    @POST("product_add.php/APIs")
    Call<LoginResponce> addProduct(
            @Part MultipartBody.Part image,
            @Part("p_name") RequestBody p_name,
            @Part("p_description") RequestBody p_description,
            @Part("p_branch") RequestBody p_branch,
            @Part("p_qty") RequestBody p_qty,
            @Part("tdate") RequestBody tdate);

    @POST("product_list.php/APIs")
    Call<ProductList> getAllProduct();


    @FormUrlEncoded
    @POST("get_product.php/APIs")
    Call<ProductResponce> ProductDetails(
            @Field("p_id") String p_id);
}
