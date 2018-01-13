package com.homebuddy.homebuddy;


public class MyOrderModel {
    private String itemList , bill , status , payment , date;

    public MyOrderModel(){}

    MyOrderModel(String itemList , String bill , String status , String payment , String date){
        this.itemList = itemList ;
        this.bill = bill ;
        this.status = status ;
        this.payment = payment ;
        this.date = date;
    }

    String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    String getBill() {
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

    String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
