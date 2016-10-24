package com.focaloid.focaloid_training.rest;

import com.focaloid.focaloid_training.models.AllProductsResults;
import com.focaloid.focaloid_training.models.Allproducts;
import com.focaloid.focaloid_training.models.SingleProductResults;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by focaloid on 05/10/16.
 */
public interface APIPlug {

    /*
   These methods defines our API endpoints.
   All REST methods such as GET,POST,UPDATE,DELETE can be stated in here.
   */
    //@GET("/get_products")
    //Call<AllProductsResults> getProducts();


    @FormUrlEncoded
    @POST("services/get_products")

    Call<AllProductsResults> getProducts(@Field("usr_fk_id") String usr_fk_id,
                                         @Field("cat_fk_id") String cat_fk_id,
                                         @Field("subcat_fk_id") String pro_cat,
                                         @Field("keyword") String keyword,
                                         @Field("offset") String cart_offset);


    @FormUrlEncoded
    @POST("services/get_product_details")
    Call<JsonObject> getProductDetail(@Field("pro_pk_id") String pro_pk_id);
}
