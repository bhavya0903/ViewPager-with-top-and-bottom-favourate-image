package com.example.bhavya.wardrobe.Bean;

import com.orm.SugarRecord;

/**
 * Created by Bhavya on 23-01-2018.
 */

  public class ImageBean extends SugarRecord {

    public ImageBean() {
    }

    public ImageBean(String bitmapString, int type) {
        this.bitmapString = bitmapString;
        this.type = type;


    }

    public String bitmapString;
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getBitmapString() {
        return bitmapString;
    }

    public void setBitmapString(String bitmapString) {
        this.bitmapString = bitmapString;
    }
}