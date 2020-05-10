package com.test.webscrape.DataModel;

public class Products {

    private String imageLogo;
    private String imageProduct;
    private String productDesc;
    private String oldPrice;
    private String newPrice;
    private String urlLink;
    private String discountPercentage;
    private String imgLogoDesc;

    public boolean isImageChanged;

    private Products(){
    }

    public Products(String ProductDes,String priceOld,String imageP,String urlLink,String imageLogo,String PriceNew,String dis,
                    String imgLogoDes){
        imgLogoDesc=imgLogoDes;
        discountPercentage=dis;
        productDesc =ProductDes;
        this.oldPrice =priceOld;
        this.newPrice =PriceNew;
        imageProduct=imageP;
        this.urlLink=urlLink;
        this.imageLogo=imageLogo;
    }

    public Products(String ProductDes,String priceOld,String imageP,String urlLink,String imageLogo,String PriceNew,String dis){
        discountPercentage=dis;
        productDesc =ProductDes;
        this.oldPrice =priceOld;
        this.newPrice =PriceNew;
        imageProduct=imageP;
        this.urlLink=urlLink;
        this.imageLogo=imageLogo;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public String getImgLogoDesc() {
        return imgLogoDesc;
    }

    public boolean isImageChanged() {
        return isImageChanged;
    }

    public void setImageChanged(boolean imageChanged) {
        isImageChanged = imageChanged;
    }
}
