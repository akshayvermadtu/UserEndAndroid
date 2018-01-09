package com.homebuddy.homebuddy;

class CategoryModel {
    private String categoryName , imageURL ;

    public CategoryModel(){}

    CategoryModel(String categoryName , String imageURL ){
        this.categoryName = categoryName ;
        this.imageURL = imageURL ;
    }

    String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
