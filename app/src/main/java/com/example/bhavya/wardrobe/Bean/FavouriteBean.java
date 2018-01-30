package com.example.bhavya.wardrobe.Bean;

import com.orm.SugarRecord;

public class FavouriteBean extends SugarRecord {
    public String getFavTopId() {
        return favTopId;
    }

    public void setFavTopId(String favTopId) {
        this.favTopId = favTopId;
    }

    public String getFavBottomId() {
        return favBottomId;
    }

    public void setFavBottomId(String favBottomId) {
        this.favBottomId = favBottomId;
    }

    public String favTopId;
    public String favBottomId;
}
