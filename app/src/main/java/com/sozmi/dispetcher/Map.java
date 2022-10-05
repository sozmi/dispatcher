package com.sozmi.dispetcher;
import static com.yandex.runtime.Runtime.getApplicationContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public final class Map {
    private static boolean isInit=false;
    private static MapView mapView;

    public static void setApi(){
        if(!isInit){
            MapKitFactory.setApiKey(BuildConfig.ApiKey);
            isInit=true;
        }
    }

    public static com.yandex.mapkit.map.Map getMap(){
        return mapView.getMap();
    }
    public static MapView getMapView(){
        return mapView;
    }
    public static void setMapView(View view){
        mapView = view.findViewById(R.id.mapview);
    }

    public static void init(Context context){
            MapKitFactory.initialize(context);
    }


    public static void moveTo( Point coordinate){
        getMap().move(
                new CameraPosition(coordinate, 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    public static void addMarker(Point coordinate, TypeBuilding type){
        Bitmap btm = null;
        switch (type){
            case police:
                btm =getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_police);
                break;
            case hospital:
                btm =getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_hospital);
                break;
            case fire_station:
                btm =getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_firemen);
                break;
        }
        var pm =mapView.getMap().getMapObjects().addPlacemark(coordinate);
        pm.setIcon(ImageProvider.fromBitmap(btm));
    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        assert drawable != null;
        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
