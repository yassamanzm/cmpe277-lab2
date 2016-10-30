package com.sjsu.cmpe277.campusmap.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Information {

    public static final String KING= "King Library";
    public static final String SU= "Student Union";
    public static final String BBC= "Boccardo Business";
    public static final String YUH= "Yoshihiro Uchida Hall";
    public static final String SPG= "South Parking Garage";
    public static final String ENG= "Engineering Building";


    public static final Map<String, Building> BUILDING_MAP;
    public static final Map<String, String> SHORT_NAME_MAP;

    static {
        Map<String, Building> aMap = new HashMap<>();
        aMap.put("king library",
                new Building("King Library",
                        "King",
                        "Dr. Martin Luther King, Jr. Library, 150 East San Fernando Street, San Jose, CA 95112",
                        37.3358043,
                        -121.8860251,
                        50, 180, 130, 280));
        aMap.put("engineering building",
                new Building("Engineering Building",
                        "ENG",
                        "San José State University Charles W. Davidson College of Engineering, 1 Washington Square, San Jose, CA 95112",
                        37.337656,
                        -121.8822646,
                        340, 190, 440, 300));
        aMap.put("boccardo business complex",
                new Building("Boccardo Business Complex",
                        "BBC",
                        "Boccardo Business Complex, San Jose, CA 95112",
                        37.3369032,
                        -121.8782262,
                        520, 350, 600, 410));
        aMap.put("yoshihiro uchida hall",
                new Building("Yoshihiro Uchida Hall",
                        "YUH",
                        "Yoshihiro Uchida Hall, San Jose, CA 95112",
                        37.3333767,
                        -121.88422,
                        60, 410, 100, 480));
        aMap.put("student union",
                new Building("Student Union",
                        "SU",
                        "SJSU Student Union, South 9th Street, San Jose, CA 95112",
                        37.3343414,
                        -121.8806146,
                        335, 305, 450, 355));
        aMap.put("south parking garage",
                new Building("South Parking Garage",
                        "SPG",
                        "San Jose State University South Garage, 330 South 7th Street, San Jose, CA 95112",
                        37.3327995,
                        -121.8801411,
                        190, 570, 330, 650));
        BUILDING_MAP = Collections.unmodifiableMap(aMap);
    }

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("king", "King Library");
        aMap.put("eng", "Engineering Building");
        aMap.put("bbc", "Boccardo Business Complex");
        aMap.put("yuh", "Yoshihiro Uchida Hall");
        aMap.put("su", "Student Union");
        aMap.put("spg", "South Parking Garage");
        SHORT_NAME_MAP = Collections.unmodifiableMap(aMap);
    }
}
