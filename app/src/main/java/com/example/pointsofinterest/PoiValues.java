package com.example.pointsofinterest;

public class PoiValues {

    private String name;
    private String type;
    private String desc;

    private double longitude;
    private double latitude;

    public PoiValues(String poiName, String poiType, String poiDesc, double poiLongitude, double poiLatitude)
    {
        name = poiName;
        type = poiType;
        desc = poiDesc;

        longitude = poiLongitude;
        latitude = poiLatitude;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getDesc()
    {
        return desc;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }
}
