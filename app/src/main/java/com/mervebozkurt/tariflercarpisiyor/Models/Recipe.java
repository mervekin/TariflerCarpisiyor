package com.mervebozkurt.tariflercarpisiyor.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.List;

public class Recipe {
    public String documentId;
    public String useremail;
    public String username;
    public String category;
    public String mealname;
    public String cookingstep;
    public String cookingtime;
    public String mealportion;
    public String downloadUrl;
    public Float mealrating;
    public String Userid;
    public FieldValue date;

    List<String> ingredientlist ,search;

    public Recipe(){

    }
    public Recipe(String Userid,String documentId, String mealname,String downloadUrl) {
        this.Userid=Userid;
        this.category = category;
        this.mealname = mealname;
        this.downloadUrl = downloadUrl;
    }
    public Recipe(String documentId ,String Userid,String useremail, String username, String category, String mealname, String cookingstep, String cookingtime, String mealportion, String downloadUrl, List<String> ingredientlist, FieldValue date ,List<String> search) {
        this.documentId=documentId;
        this.Userid=Userid;
        this.useremail = useremail;
        this.username = username;
        this.category = category;
        this.mealname = mealname;
        this.cookingstep = cookingstep;
        this.cookingtime = cookingtime;
        this.mealportion = mealportion;
        this.downloadUrl = downloadUrl;
        this.ingredientlist = ingredientlist;
        this.date=date;
        this.search=search;
    }
    public Recipe(Float mealrating){
        this.mealrating=mealrating;

    }

    public List<String> getSearch() {
        return search;
    }

    public void setSearch(List<String> search) {
        this.search = search;
    }

    public Float getMealrating() {
        return mealrating;
    }

    public void setMealrating(Float mealrating) {
        this.mealrating = mealrating;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }

    public void setCookingstep(String cookingstep) {
        this.cookingstep = cookingstep;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public void setMealportion(String mealportion) {
        this.mealportion = mealportion;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setDate(FieldValue date) {
        this.date = date;
    }

    public void setIngredientlist(List<String> ingredientlist) {
        this.ingredientlist = ingredientlist;
    }



    public String getUseremail() {
        return useremail;
    }

    public FieldValue getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getCategory() {
        return category;
    }

    public String getMealname() {
        return mealname;
    }

    public String getCookingstep() {
        return cookingstep;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public String getMealportion() {
        return mealportion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public List<String> getIngredientlist() {
        return ingredientlist;
    }

}
