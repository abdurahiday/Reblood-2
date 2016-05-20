package com.erd.reblood.model;

/**
 * Created by ILM on 5/19/2016.
 */
public class History {

    private String storeId;
    private String merchantName;
    private String storeName;
    private String costId;
    private String transactDate;


    public History() {
    }

    public History(String storeId, String merchantName, String storeName, String costId, String transactDate) {
        this.storeId = storeId;
        this.merchantName = merchantName;
        this.storeName = storeName;
        this.costId = costId;
        this.transactDate = transactDate;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getTransactDate() {
        return transactDate;
    }

    public void setTransactDate(String transactDate) {
        this.transactDate = transactDate;
    }
}
