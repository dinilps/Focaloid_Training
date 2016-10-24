package com.focaloid.focaloid_training.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by focaloid on 06/10/16.
 */
public class SingleProduct  {


    @SerializedName("pro_pk_id")
    @Expose
    private Object proPkId;
    @SerializedName("pro_name")
    @Expose
    private Object proName;
    @SerializedName("category_id")
    @Expose
    private Object categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private Object subCategoryId;
    @SerializedName("pro_price")
    @Expose
    private Object proPrice;
    @SerializedName("pro_qty")
    @Expose
    private Object proQty;
    @SerializedName("pro_desc")
    @Expose
    private Object proDesc;
    @SerializedName("pro_code")
    @Expose
    private Object proCode;
    @SerializedName("product_images")
    @Expose
    private Object productImages;

    /**
     *
     * @return
     * The proPkId
     */
    public Object getProPkId() {
        return proPkId;
    }

    /**
     *
     * @param proPkId
     * The pro_pk_id
     */
    public void setProPkId(Object proPkId) {
        this.proPkId = proPkId;
    }

    /**
     *
     * @return
     * The proName
     */
    public Object getProName() {
        return proName;
    }

    /**
     *
     * @param proName
     * The pro_name
     */
    public void setProName(Object proName) {
        this.proName = proName;
    }

    /**
     *
     * @return
     * The categoryId
     */
    public Object getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @param categoryId
     * The category_id
     */
    public void setCategoryId(Object categoryId) {
        this.categoryId = categoryId;
    }

    /**
     *
     * @return
     * The subCategoryId
     */
    public Object getSubCategoryId() {
        return subCategoryId;
    }

    /**
     *
     * @param subCategoryId
     * The sub_category_id
     */
    public void setSubCategoryId(Object subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /**
     *
     * @return
     * The proPrice
     */
    public Object getProPrice() {
        return proPrice;
    }

    /**
     *
     * @param proPrice
     * The pro_price
     */
    public void setProPrice(Object proPrice) {
        this.proPrice = proPrice;
    }

    /**
     *
     * @return
     * The proQty
     */
    public Object getProQty() {
        return proQty;
    }

    /**
     *
     * @param proQty
     * The pro_qty
     */
    public void setProQty(Object proQty) {
        this.proQty = proQty;
    }

    /**
     *
     * @return
     * The proDesc
     */
    public Object getProDesc() {
        return proDesc;
    }

    /**
     *
     * @param proDesc
     * The pro_desc
     */
    public void setProDesc(Object proDesc) {
        this.proDesc = proDesc;
    }

    /**
     *
     * @return
     * The proCode
     */
    public Object getProCode() {
        return proCode;
    }

    /**
     *
     * @param proCode
     * The pro_code
     */
    public void setProCode(Object proCode) {
        this.proCode = proCode;
    }

    /**
     *
     * @return
     * The productImages
     */
    public Object getProductImages() {
        return productImages;
    }

    /**
     *
     * @param productImages
     * The product_images
     */
    public void setProductImages(Object productImages) {
        this.productImages = productImages;
    }

}