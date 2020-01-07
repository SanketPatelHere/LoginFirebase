package com.example.loginfirebase;
import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class User {
    public String name, email;
    String uid, phoneno;
    Uri photourl;
    boolean anonymous;
    long createdtime;
    long lastlogintime;
    public User()
    {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, long createdtime, long lastlogintime) {
        this.name = name;
        this.email = email;
        this.createdtime = createdtime;
        this.lastlogintime = lastlogintime;
    }


    public User(String name, String email, String uid, String phoneno, Uri photourl, boolean anonymous) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.phoneno = phoneno;
        this.photourl = photourl;
        this.anonymous = anonymous;
    }

    public User(String name, String email, String uid, String phoneno, Uri photourl, boolean anonymous, long time) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.phoneno = phoneno;
        this.photourl = photourl;
        this.anonymous = anonymous;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public Uri getPhotourl() {
        return photourl;
    }

    public void setPhotourl(Uri photourl) {
        this.photourl = photourl;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }


    public long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

    public long getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(long lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

}
