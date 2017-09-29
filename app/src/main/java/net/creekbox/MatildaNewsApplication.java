package net.creekbox;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.creekbox.models.Category;

import java.util.HashMap;
import java.util.List;

/**
 * Created by opnchaudhary on 6/6/16.
 */
public class MatildaNewsApplication extends Application {
    private static String endPoint = "http://tvstartup.biz/mng-channel/vpanel/api/";
    private static String user = "creekboxtv";
    private static String pass = "cdf6461fac0e84afd9336fe385e03d5d";
    private static String imageUri = "http://tvstartup.biz/mng-channel/vpanel/uploads/";

    public static List<Category> categoryList;
    public static HashMap<String, List<Movie>> hmap = new HashMap<String, List<Movie>>();

    public static String getEndPoint() {
        return endPoint;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }

    public static String getImageUri() {
        return imageUri;
    }

    public static List<Category> getCategoryList() {
        return categoryList;
    }

    public static void setCategoryList(List<Category> categoryList) {
        MatildaNewsApplication.categoryList = categoryList;
    }

    public static HashMap<String, List<Movie>> getHmap() {
        return hmap;
    }

    public static void setHmap(HashMap<String, List<Movie>> hmap) {
        MatildaNewsApplication.hmap = hmap;
    }

    public static String getDeviceMacAddress(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return  info.getMacAddress();
    }
    public static boolean hasPermission(final Context context, final String permission) {
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }
    public static Movie buildMovieInfo(String category, String title,
                                        String description, String studio, String videoUrl, String cardImageUrl,
                                        String bgImageUrl) {
        Movie movie = new Movie();
        movie.setId(Movie.getCount());
        Movie.incCount();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCategory(category);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(bgImageUrl);
        movie.setVideoUrl(videoUrl);
        return movie;
    }
}
