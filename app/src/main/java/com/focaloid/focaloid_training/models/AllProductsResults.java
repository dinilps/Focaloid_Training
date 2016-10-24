package com.focaloid.focaloid_training.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by focaloid on 05/10/16.
 */
public class AllProductsResults {

    @SerializedName("data")
    @Expose
    private List<Allproducts> data = new ArrayList<Allproducts>();
    @SerializedName("total_products")
    @Expose
    private Integer totalProducts;
    @SerializedName("cart_data")
    @Expose
    private List<CartAllproducts> cartData = new ArrayList<CartAllproducts>();
    @SerializedName("success")
    @Expose
    private Integer success;

    /**
     *
     * @return
     * The data
     */
    public List<Allproducts> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Allproducts> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The totalProducts
     */
    public Integer getTotalProducts() {
        return totalProducts;
    }

    /**
     *
     * @param totalProducts
     * The total_products
     */
    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

    /**
     *
     * @return
     * The cartData
     */
    public List<CartAllproducts> getCartData() {
        return cartData;
    }

    /**
     *
     * @param cartData
     * The cart_data
     */
    public void setCartData(List<CartAllproducts> cartData) {
        this.cartData = cartData;
    }

    /**
     *
     * @return
     * The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

}

