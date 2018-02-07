package com.delhijal.survey.Beans;

import com.delhijal.survey.NewSurvey.PersonDetailsActivity;

/**
 * Created by dell on 2/7/2018.
 */

public class PersonDetailsBean {
    String provideby;
    String name;
    String father;
    String mobile;
    String email;
    String propertyimg;
    public PersonDetailsBean(String addprovidedby, String addname, String addfather, String addmobile, String addemail, String addpropertyimg){
        this.provideby = addprovidedby;
        this.name = addname;
        this.father=addfather;
        this.mobile = addmobile;
        this.email = addemail;
        this.propertyimg = addpropertyimg;
    }
    public String getProvideby() {
        return provideby;
    }

    public void setProvideby(String provideby) {
        this.provideby = provideby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPropertyimg() {
        return propertyimg;
    }

    public void setPropertyimg(String propertyimg) {
        this.propertyimg = propertyimg;
    }
}
