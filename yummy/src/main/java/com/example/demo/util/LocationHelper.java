package com.example.demo.util;

import com.example.demo.entity.Location;

public class LocationHelper {
    public static boolean checkTwoLocationEqual(Location location1, Location location2){
        if(location1.getId()==location2.getId()){
            return true;
        }
        if(location1.getLatitude()==location2.getLatitude()&&location1.getLongitude()==location2.getLongitude()){
            return true;
        }
        boolean provinceEquals = location1.getProvince().equals(location2.getProvince());
        boolean cityEquals = location1.getCity().equals(location2.getCity());
        boolean districtEquals = location1.getDistrict().equals(location2.getDistrict());
        boolean streetEquals = location1.getStreet().equals(location2.getStreet());
        boolean streetNumberEquals = location1.getStreetNumber().equals(location2.getStreetNumber());
        return provinceEquals && cityEquals && districtEquals && streetEquals && streetNumberEquals;
    }
}
