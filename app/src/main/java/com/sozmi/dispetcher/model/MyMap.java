package com.sozmi.dispetcher.model;
import static com.yandex.runtime.Runtime.getApplicationContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sozmi.dispetcher.BuildConfig;
import com.sozmi.dispetcher.R;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.logo.Alignment;
import com.yandex.mapkit.logo.HorizontalAlignment;
import com.yandex.mapkit.logo.VerticalAlignment;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public final class MyMap {
    private static boolean isInit=false;
    private static MapView mapView;
    private static Map map;

    public static Point getTargetCamera(){
        return map.getCameraPosition().getTarget();
    }
    public static void setApi(){
        if(!isInit){
            MapKitFactory.setApiKey(BuildConfig.API_KEY);
            isInit=true;
        }
    }

    public static void setMapView(View view){
        mapView = view.findViewById(R.id.mapview);
        map=mapView.getMap();
    }
    public static void setLogoPosition(){
        map.getLogo().setAlignment(new Alignment(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM));
    }

    public static void init(Context context){
            MapKitFactory.initialize(context);
    }


    public static void moveTo( Point coordinate){
        map.move(
                new CameraPosition(coordinate, 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

// --Commented out by Inspection START (09.10.2022 19:21):
//    @SuppressWarnings("EmptyMethod")
//    public static void LoadResource(){
//        //TODO: для оптимизации необходимо реализовать передачу готового
//        // ImageProvider в метод создания маркера.
//        // Иначе каждый раз происходит формирование нового btm файла
//
//    }
// --Commented out by Inspection STOP (09.10.2022 19:21)

    public static PlacemarkMapObject addMarker(Point coordinate, TypeBuilding type){
        int id=0;
        switch (type){
            case police:
                id =R.drawable.ic_police;
                break;
            case hospital:
                id =R.drawable.ic_hospital;
                break;
            case fire_station:
                id =R.drawable.ic_firemen;
                break;
            case none:
                id=R.drawable.ic_map_marker;
                break;
        }
        var btm = getBitmapFromVectorDrawable(getApplicationContext(),id);
        var pm =map.getMapObjects().addPlacemark(coordinate);
        pm.setIcon(ImageProvider.fromBitmap(btm));
        if(type==TypeBuilding.none)
            pm.setDraggable(true);
        return pm;
    }
    public static void delMarker(PlacemarkMapObject mapObject){
        if(mapObject==null)
            return;
       mapObject.getParent().remove(mapObject);
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

    public static void OnStart(){
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    public static void OnStop(){
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}
