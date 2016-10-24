package com.focaloid.focaloid_training.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by focaloid on 05/10/16.
 */
public class CartAllproducts {

    @SerializedName("crm_pk_id")
    @Expose
    private Object crmPkId;
    @SerializedName("cart_count")
    @Expose
    private String cartCount;

    /**
     *
     * @return
     * The crmPkId
     */
    public Object getCrmPkId() {
        return crmPkId;
    }

    /**
     *
     * @param crmPkId
     * The crm_pk_id
     */
    public void setCrmPkId(Object crmPkId) {
        this.crmPkId = crmPkId;
    }

    /**
     *
     * @return
     * The cartCount
     */
    public String getCartCount() {
        return cartCount;
    }

    /**
     *
     * @param cartCount
     * The cart_count
     */
    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

}