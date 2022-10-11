package com.sozmi.dispetcher.model;

import static com.yandex.runtime.Runtime.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
    private static boolean isInit = false;
    private static MapView mapView;
    private static Map map;
    // --Commented out by Inspection (10.10.2022 22:01):private static Point coordinateBuilding;
    private static Point coordinateUser;
    private static PlacemarkMapObject mapObject;


    public static Point getTargetCamera() {
        return map.getCameraPosition().getTarget();
    }

    public static void setApi() {
        if (!isInit) {
            MapKitFactory.setApiKey(BuildConfig.API_KEY);
            isInit = true;
        }
    }

    public static Point getCoordinateUser() {
        return coordinateUser;
    }

    @SuppressLint("MissingPermission")
    public static void SetUserLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
        }

        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            setCoordinateUser(new Point(location.getLatitude(),location.getLongitude()));
        }

    }


    public static void setCoordinateUser(Point coordinate) {
        coordinateUser = coordinate;
    }

    public static void setMapView(View view){
        mapView = view.findViewById(R.id.mapview);
        map=mapView.getMap();
    }

    public static void init(View view){
            setMapView(view);
            MapKitFactory.initialize(view.getContext());
            map.getLogo().setAlignment(new Alignment(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM));
    }


    public static void camMoveTo(Point coordinate){
        map.move(
                new CameraPosition(coordinate, 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }


    private static ImageProvider getImageProvider(TypeBuilding type){
        int id=0;
        switch (type){
            case hospital:
                id =R.drawable.ic_hospital;
                break;
            case police:
                id =R.drawable.ic_police;
                break;
            case fire_station:
                id =R.drawable.ic_firemen;
                break;
            case none:
                id=R.drawable.ic_map_marker;
                break;
        }
        var btm = getBitmapFromVectorDrawable(getApplicationContext(),id);
        return ImageProvider.fromBitmap(btm);
    }

    public static void addMarker(Point coordinate, TypeBuilding type){
        ImageProvider[] imageProviders =new ImageProvider[TypeBuilding.values().length];

        if(imageProviders[type.ordinal()]==null){
            imageProviders[type.ordinal()]= getImageProvider(type);
        }

        var pm =map.getMapObjects().addPlacemark(coordinate);
        pm.setIcon(imageProviders[type.ordinal()]);
        if(type==TypeBuilding.none)
            pm.setDraggable(true);
        setMapObject(pm);
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

// --Commented out by Inspection START (10.10.2022 22:00):
//    public static Point getCoordinateNewBuilding() {
//        return coordinateBuilding;
//    }
// --Commented out by Inspection STOP (10.10.2022 22:00)

// --Commented out by Inspection START (10.10.2022 22:00):
//    public static void setCoordinateNewBuilding(Point coordinateBuilding) {
//        MyMap.coordinateBuilding = coordinateBuilding;
//    }
// --Commented out by Inspection STOP (10.10.2022 22:00)

    public static PlacemarkMapObject getMapObject() {
        return mapObject;
    }

    public static void setMapObject(PlacemarkMapObject mapObject) {
        MyMap.mapObject = mapObject;
    }
}
