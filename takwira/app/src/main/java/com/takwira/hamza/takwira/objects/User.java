package com.takwira.hamza.takwira.objects;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableUser;

import java.util.HashMap;

/**
 * Created by hamza on 01/08/17.
 */

public class User implements Parcelable {
    private String id ;
    private String firstName ;
    private String lastName ;
    private String email ;
    private String birthday ;
    private String location ;
    private String pictureUrl ;
    private String connexionMode ;

    public User() {
    }

    public User(String id , String firstName, String lastName, String email, String birthday, String location, String pictureUrl,String connexionMode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.location = location;
        this.pictureUrl = pictureUrl;
        this.connexionMode = connexionMode ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnexionMode() {
        return connexionMode;
    }

    public void setConnexionMode(String connexionMode) {
        this.connexionMode = connexionMode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getpictureUrl() {
        return pictureUrl;
    }

    public void setpictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        birthday = in.readString();
        location = in.readString();
        connexionMode = in.readString();
        pictureUrl = (String) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(location);
        dest.writeString(connexionMode);
        dest.writeValue(pictureUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void resetUserFrom (HashMap<String,String> list){
        if(list != null) {
            setId(list.get(TableUser.id));
            setConnexionMode(list.get(TableUser.connexion_mode));
            setFirstName(list.get(TableUser.first_name));
            setLastName(list.get(TableUser.last_name));
            setEmail(list.get(TableUser.email));
            setBirthday(list.get(TableUser.birthday));
            setLocation(list.get(TableUser.location));
            setpictureUrl(list.get(TableUser.pictureUrl));
        }
    }
}