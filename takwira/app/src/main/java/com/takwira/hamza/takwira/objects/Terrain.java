package com.takwira.hamza.takwira.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hamza on 10/08/17.
 */

public class Terrain implements Parcelable
{
    private String table_name ;

    private String name;
    private String type  ;
    private String address ;
    private String city;
    private String phone  ;
    private Double latitude  ;
    private Double longitude ;
    private Float note ;
    private String pictureUrl ;

    public Terrain() {
        this.table_name = "";
        this.name = "";
        this.type = "";
        this.address = "";
        this.city ="";
        this.phone = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.note = 0f;
        this.pictureUrl = "";
    }

    public Terrain(String table_name, String name, String type, String address, String city, String phone, Double latitude, Double longitude, Float note, String pictureUrl) {
        this.table_name = table_name;
        this.name = name;
        this.type = type;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
        this.pictureUrl = pictureUrl;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    protected Terrain(Parcel in) {
        table_name = in.readString();
        name = in.readString();
        type = in.readString();
        address = in.readString();
        city = in.readString();
        phone = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        note = in.readFloat();
        pictureUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(table_name);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(note);
        dest.writeString(pictureUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Terrain> CREATOR = new Parcelable.Creator<Terrain>() {
        @Override
        public Terrain createFromParcel(Parcel in) {
            return new Terrain(in);
        }

        @Override
        public Terrain[] newArray(int size) {
            return new Terrain[size];
        }
    };
}