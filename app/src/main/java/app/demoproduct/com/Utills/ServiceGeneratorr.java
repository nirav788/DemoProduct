package app.demoproduct.com.Utills;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eduardo on 29/05/2016.
 */
public class ServiceGeneratorr {

    //public static final String API_BASE_URL = "http://terapanth.edumastersoft.com/postdata/";
    public static final String API_BASE_URL = "http://shivamsoftwareservices.com/demo_app/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()));
    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();http://ec2-54-84-69-216.compute-1.amazonaws.com/APIs/uploads/userprofile/tutor/tutor_6123a5f4e4a50c633db0263c3eff3b7fLX98LD.jpg
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);
        Retrofit retrofit = builder.client(httpClient.connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
