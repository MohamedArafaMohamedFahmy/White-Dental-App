package com.arafa.mohamed.whitedental.Model;

public class ImagesData {

    private String imgURl,currentDate,idImage;

    public ImagesData(){

    }

    public ImagesData(String imgURL,String currentDate,String idImage){
        this.imgURl=imgURL;
        this.currentDate=currentDate;
        this.idImage=idImage;

    }

    public String getImgURl() {
        return imgURl;
    }

    public void setImgURl(String imgURl) {
        this.imgURl = imgURl;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }
}
