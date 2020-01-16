package com.blood.emirateslifedonation.HolderClass;

public class reciver_holder {

    private String Image_downloadurl, blood_group;
    private String hospital_name, location;
    private String message, mobilenumber;
    private String patentname, userid;
    private String gender, age;



    public reciver_holder(){

    }


    public reciver_holder(String image_downloadurl, String blood_group, String hospital_name, String location, String message, String mobilenumber, String patentname, String userid, String gender, String age) {
        Image_downloadurl = image_downloadurl;
        this.blood_group = blood_group;
        this.hospital_name = hospital_name;
        this.location = location;
        this.message = message;
        this.mobilenumber = mobilenumber;
        this.patentname = patentname;
        this.userid = userid;
        this.gender = gender;
        this.age = age;
    }

    public String getImage_downloadurl() {
        return Image_downloadurl;
    }

    public void setImage_downloadurl(String image_downloadurl) {
        Image_downloadurl = image_downloadurl;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPatentname() {
        return patentname;
    }

    public void setPatentname(String patentname) {
        this.patentname = patentname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
