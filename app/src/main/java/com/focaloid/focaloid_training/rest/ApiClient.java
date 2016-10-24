package com.focaloid.focaloid_training.rest;

import com.focaloid.focaloid_training.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by focaloid on 05/10/16.
 */
/*public class ApiClient {

    private static APIPlug REST_CLIENT;
    private static final String API_URL = Constant.Base_Url; //Change according to your API path.

    static {
        setupRestClient();
    }

    private ApiClient() {}

    public static APIPlug get() {
        return REST_CLIENT;
    }

    private static Retrofit setupRestClient() {

        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //Uncomment these lines below to start logging each request.

        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

    private static Retrofit getRetrofitInstance() {

            return new Retrofit.Builder()
                    .baseUrl(Constant.Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        public static APIPlug getApiPlug() {
            return getRetrofitInstance().create(APIPlug.class);
        }
    }
*/

public class ApiClient {

    /********
     * URLS
     *******/
    private static final String ROOT_URL = Constant.Base_Url;


    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static APIPlug getApiService() {
        return getRetrofitInstance().create(APIPlug.class);
    }
}