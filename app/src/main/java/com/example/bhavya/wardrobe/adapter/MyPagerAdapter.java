package com.example.bhavya.wardrobe.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bhavya.wardrobe.Bean.ImageBean;
import com.example.bhavya.wardrobe.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.bhavya.wardrobe.MainActivity.decodeBase64;


public class MyPagerAdapter extends PagerAdapter {

    private List<ImageBean> imageBeanList;
    private LayoutInflater inflater;
    private Context context;

    public MyPagerAdapter(Context context, List<ImageBean> imageBeanList) {
        this.context = context;
        this.imageBeanList = imageBeanList;
        inflater = LayoutInflater.from(context);
    }

    public List<ImageBean> getImageBeanList() {
        return imageBeanList;
    }

    public void setImageBeanList(List<ImageBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
        notifyDataSetChanged();
    }

    public MyPagerAdapter(Context context) {
        this.context = context;
        imageBeanList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (imageBeanList == null)
            imageBeanList = new ArrayList<ImageBean>();
        return imageBeanList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
//        myImage.setImageResource(imageBeanArrayList.get(position).getBitmapString();

        myImage.setImageBitmap(decodeBase64(imageBeanList.get(position).getBitmapString()));
        view.addView(myImageLayout, 0);

//        FavouriteBean favouriteBean = new FavouriteBean();
//        favouriteBean.setFavTopId((long) myImage.getId());
//        favouriteBean.save();

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
