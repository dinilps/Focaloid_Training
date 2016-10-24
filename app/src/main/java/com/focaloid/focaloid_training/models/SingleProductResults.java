package com.focaloid.focaloid_training.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by focaloid on 06/10/16.
 */
public class SingleProductResults{

    @SerializedName("data")
    @Expose
    private SingleProduct singleproductdata;
    @SerializedName("success")
    @Expose
    private Integer success;

    /**
     *
     * @return
     * The data
     */
    public SingleProduct getData() {
        return singleproductdata;
    }

    /**
     *
     * @param singleproductdata
     * The data
     */
    public void setData(SingleProduct singleproductdata) {
        this.singleproductdata = singleproductdata;
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