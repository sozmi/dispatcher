package com.sozmi.dispatcher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sozmi.dispatcher.model.system.BaseObject;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class Object<T extends BaseType> extends BaseObject {

    private String name;
    private GeoPoint point;
    private T type;
    private int cost;
    private Marker marker;

    public Object() {

    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    protected Object(int id, String name, GeoPoint point, T type, int cost) {
        super(id);
        setCost(cost);
        setName(name);
        setPosition(point);
        setType(type);
    }

    public int getCost() {
        return cost;
    }

    /**
     * Установка название объекта
     *
     * @param name название объекта
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение названия объекта
     *
     * @return название объекта
     */
    public String getName() {
        return name;
    }

    public GeoPoint getPosition() {
        return point;
    }

    public void setPosition(GeoPoint point) {
        this.point = point;
    }

    /**
     * Установка типа объекта
     *
     * @param type тип объекта
     */
    public void setType(T type) {
        this.type = type;
    }

    /**
     * Получение типа объекта
     *
     * @return тип объекта
     */
    public T getType() {
        return type;
    }

    /**
     * Получение id иконки объекта для отображения на карте
     *
     * @return id ресурса
     */
    public int getImage() {
        return type.toImageId();
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
