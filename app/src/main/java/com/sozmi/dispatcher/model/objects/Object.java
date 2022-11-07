package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.model.system.ServerMod;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class Object<T extends BaseType> extends ServerMod {

    private String name;
    private GeoPoint point;
    private T type;
    private int cost;
    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        if(this.marker==null || marker==null) this.marker = marker;
    }

    protected Object(int id, String name, GeoPoint point, T type, int cost) {
        super(id);
        setCost(cost);
        setName(name);
        setPosition(point);
        setType(type);
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
