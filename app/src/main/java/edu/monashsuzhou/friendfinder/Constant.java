package edu.monashsuzhou.friendfinder;

public interface Constant {
//    String SEVER_HOST = "192.168.1.100";
    String SEVER_HOST = "47.102.97.239";
    String SEVER_PORT = "8080";
    String SEVER_NAME = "5183_A1";

    String WEATHER_HOST = "http://api.openweathermap.org/data/2.5/forecast/daily";
    String APPID = "79badf94102e008963c2d50b6cfa43f2";
    String UNITS = "metric";
    Integer N_DAYS = 1;

    String MOVIE_HOST = "http://www.omdbapi.com/?apikey=a98c3027&t=";

    int SUCCESS_RESULT = 0;
    int FAILURE_RESULT = 1;
    String PACKAGE_NAME = "com.zhp.googlemaptutorial.MapsActivity";
    String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    String LOCATION_NAME_EXTRA = PACKAGE_NAME +
            ".LOCATION_NAME_EXTRA";

}
