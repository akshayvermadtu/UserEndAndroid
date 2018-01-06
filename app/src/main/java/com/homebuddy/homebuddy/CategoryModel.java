package com.homebuddy.homebuddy;

public class CategoryModel {
    private String categoryName;

    public CategoryModel(){}

    CategoryModel(String categoryName ){
        this.categoryName = categoryName ;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
