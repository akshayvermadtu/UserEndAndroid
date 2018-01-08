package com.homebuddy.homebuddy;

public class ItemListModel {
    private String itemName , itemPrice , itemBrand , itemImage ;

    public ItemListModel(){}

    ItemListModel(String itemName , String itemPrice , String itemBrand , String itemImage ){
        this.itemName = itemName ;
        this.itemPrice = itemPrice ;
        this.itemBrand = itemBrand ;
        this.itemImage = itemImage ;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

}
