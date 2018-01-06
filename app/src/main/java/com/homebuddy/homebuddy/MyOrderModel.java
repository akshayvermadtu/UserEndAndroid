package com.homebuddy.homebuddy;


public class MyOrderModel {
    private String itemList , bill , status , payment ;

    public MyOrderModel(){}

    MyOrderModel(String itemList , String bill , String status , String payment ){
        this.itemList = itemList ;
        this.bill = bill ;
        this.status = status ;
        this.payment = payment ;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

}
