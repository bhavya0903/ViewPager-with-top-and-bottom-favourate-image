package com.example.bhavya.wardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bhavya.wardrobe.Bean.FavouriteBean;
import com.example.bhavya.wardrobe.Bean.ImageBean;
import com.example.bhavya.wardrobe.adapter.MyPagerAdapter;
import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.example.bhavya.wardrobe.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_TOP = 1001;
    public static final int REQUEST_CODE_BOTTOM = 1002;
    public static final int TYPE_TOP = 1;
    public static final int TYPE_BOTTOM = 2;
    private static final String TAG = "TAG";
    private static final int CAMERA_REQUEST_TOP = 1888;
    private static final int CAMERA_REQUEST_BOTTOM = 1889;

    ViewPager viewPagerTop, viewPagerBottom;
    ImageBean imageBeantop, imageBeanBottom;
    ImageView plusTop, plusBottom, fav,attachTop,attachBottom,shuffle;
    private static int currentPage, currentPage1 = 0;
    private boolean flag = true;
    public MyPagerAdapter myAdapterTop, myAdapterBottom;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        init();
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    private void init() {


        imageBeantop = new ImageBean();
        imageBeanBottom = new ImageBean();

        myAdapterTop = new MyPagerAdapter(getApplicationContext());
        myAdapterBottom = new MyPagerAdapter(getApplicationContext());


        viewPagerTop = (ViewPager) findViewById(R.id.pager_top);
        viewPagerBottom = (ViewPager) findViewById(R.id.pager_bottom);

        viewPagerTop.setAdapter(myAdapterTop);
        viewPagerBottom.setAdapter(myAdapterBottom);

        plusTop = (ImageView) findViewById(R.id.plus_top);
        plusBottom = (ImageView) findViewById(R.id.plus_bottom);

        attachTop = (ImageView) findViewById(R.id.plus_attachTop);
        attachBottom = (ImageView) findViewById(R.id.plus_attachBottom);

        shuffle =(ImageView) findViewById(R.id.shuffle);

        fav = (ImageView) findViewById(R.id.fav);
        setPageChangeListener();
        try {
            plusTop.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_TOP);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            plusBottom.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_BOTTOM);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            attachTop.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_TOP);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            attachBottom.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_BOTTOM);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                favourateUnfavourate(true);

            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//        ImageBean imageBean = ImageBean.findById(ImageBean.class,1);
            //    Log.e("Image Bean for wardrobe"," Wardrobe "+imageBean);

                List<ImageBean> imageBeanTop = ImageBean.find(ImageBean.class, NamingHelper.toSQLNameDefault("type") + " = ? ", String.valueOf(TYPE_TOP));
                List<ImageBean> imageBeanBottom = ImageBean.find(ImageBean.class, NamingHelper.toSQLNameDefault("type") + " = ? ", String.valueOf(TYPE_BOTTOM));

                myAdapterTop.setImageBeanList(imageBeanTop);
                myAdapterBottom.setImageBeanList(imageBeanBottom);

                int imageBeanShuffleTop = imageBeanTop.get(randomWithRange(0,imageBeanTop.size()-1)).getId().intValue();
                int imageBeanShuffleBottom = imageBeanBottom.get(randomWithRange(0,imageBeanBottom.size()-1)).getId().intValue();


                viewPagerTop.setCurrentItem(imageBeanShuffleTop);
                viewPagerBottom.setCurrentItem(imageBeanShuffleBottom);



            }
        });

        displayImage();

    }
    public void  favourateUnfavourate(boolean toggle)
    {
        String currentTop = myAdapterTop.getImageBeanList().get(viewPagerTop.getCurrentItem()).getId() + "";
        String currentBottom = myAdapterBottom.getImageBeanList().get(viewPagerBottom.getCurrentItem()).getId() + "";
        FavouriteBean isFavouriteBean = isFavourite(currentTop, currentBottom);

        try {

            if (isFavouriteBean != null) {
                if(toggle) {
                    fav.setBackgroundResource(R.drawable.unselected_fav);
                    SugarRecord.delete(isFavouriteBean);
                }
                else
                    fav.setBackgroundResource(R.drawable.fav_icon_select);

            } else {
                if(toggle) {
                    fav.setBackgroundResource(R.drawable.fav_icon_select);
                    FavouriteBean favouriteBean = new FavouriteBean();
                    favouriteBean.setFavTopId(currentTop);
                    favouriteBean.setFavBottomId(currentBottom);
                    favouriteBean.save();
                }
                else
                    fav.setBackgroundResource(R.drawable.unselected_fav);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    private FavouriteBean isFavourite(String currentTop, String currentBottom) {
        Select<FavouriteBean> imageBeanTop = Select.from(FavouriteBean.class).where(Condition.prop(NamingHelper.toSQLNameDefault("favTopId")).eq(String.valueOf(currentTop)), Condition.prop(NamingHelper.toSQLNameDefault("favBottomId")).eq(String.valueOf(currentBottom)));
//        List<FavouriteBean> imageBeanTop = FavouriteBean.find(FavouriteBean.class, NamingHelper.toSQLNameDefault("favTopId") + " = ? and " + NamingHelper.toSQLNameDefault("favBottomId") + " = ?", String.valueOf(currentTop), String.valueOf(currentBottom));
        return imageBeanTop != null && imageBeanTop.count() > 0 ? imageBeanTop.iterator().next() : null;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE_TOP:
                case REQUEST_CODE_BOTTOM:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ImageBean imageBean = new ImageBean();
                            sendImage(bitmap, requestCode == REQUEST_CODE_TOP ? TYPE_TOP : TYPE_BOTTOM);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;


                case CAMERA_REQUEST_TOP:
                case CAMERA_REQUEST_BOTTOM:
                    if (resultCode == Activity.RESULT_OK)
                    {
                        Bitmap bitmap = null;
                        try {
                            bitmap = (Bitmap) data.getExtras().get("data");
                            ImageBean imageBean = new ImageBean();
                            sendImage(bitmap, requestCode == CAMERA_REQUEST_TOP ? TYPE_TOP : TYPE_BOTTOM);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private void setPageChangeListener() {
        viewPagerTop.setOnPageChangeListener(pageChangeListener);
        viewPagerBottom.setOnPageChangeListener(pageChangeListener);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            favourateUnfavourate(false);
            Log.i(TAG, "onPageSelected: "+ position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void sendImage(Bitmap bitmap, int type) {
        ImageBean imageBeantop = new ImageBean();
        imageBeantop.setBitmapString(encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 15));
        imageBeantop.setType(type);
        imageBeantop.save();
        displayImage();
    }

    FavCallback favCallback = new FavCallback() {
        @Override
        public void checkIfFavourite() {
            String currentTop = myAdapterTop.getImageBeanList().get(viewPagerTop.getCurrentItem()).getId() + "";
            String currentBottom = myAdapterBottom.getImageBeanList().get(viewPagerBottom.getCurrentItem()).getId() + "";
            FavouriteBean isFavouriteBean = isFavourite(currentTop, currentBottom);
            if (isFavouriteBean != null) {
                fav.setBackgroundResource(R.drawable.unselected_fav);
            } else {
                fav.setBackgroundResource(R.drawable.fav_icon_select);
            }
        }
    };
    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    private void displayImage() {

        List<ImageBean> imageBeanTop = ImageBean.find(ImageBean.class, NamingHelper.toSQLNameDefault("type") + " = ? ", String.valueOf(TYPE_TOP));
        List<ImageBean> imageBeanBottom = ImageBean.find(ImageBean.class, NamingHelper.toSQLNameDefault("type") + " = ? ", String.valueOf(TYPE_BOTTOM));


//        ImageBean imageBeanShuffleTop = imageBeanTop.get(randomWithRange(0,imageBeanTop.size()));
//        ImageBean imageBeanShuffleBottom = imageBeanBottom.get(randomWithRange(0,imageBeanBottom.size()));



        myAdapterTop.setImageBeanList(imageBeanTop);
        myAdapterBottom.setImageBeanList(imageBeanBottom);


        int indexlastTop = viewPagerTop.getAdapter().getCount()-1;
        int indexlastBottom = viewPagerBottom.getAdapter().getCount()-1;

        viewPagerTop.setCurrentItem(indexlastTop);
        viewPagerBottom.setCurrentItem(indexlastBottom);

        if(imageBeanTop != null  && imageBeanTop.size()>0 && imageBeanBottom!=null  && imageBeanBottom.size()>0  )
        {
            favourateUnfavourate(false);
        }
    }


}

