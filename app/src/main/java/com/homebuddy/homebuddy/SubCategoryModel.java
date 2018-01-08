package com.homebuddy.homebuddy;

class SubCategoryModel  {
    private String subCategoryName;

    public SubCategoryModel(){}

    SubCategoryModel(String subCategoryName ){
        this.subCategoryName = subCategoryName ;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

}
