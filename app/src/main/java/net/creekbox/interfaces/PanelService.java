package net.creekbox.interfaces;

import net.creekbox.models.StreamResponse;
import net.creekbox.models.VODCategoryResponse;
import net.creekbox.models.VODResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by opnchaudhary on 6/7/16.
 */
public interface PanelService {
    @GET("vodcategories.php")
    Call<VODCategoryResponse> getVODCategories(@Query("user") String user, @Query("pass")String pass);

    @GET("vodplaylist.php")
    Call<VODResponse> getVideosByVODCategory(@Query("user")String user,@Query("pass")String pass,@Query("id")String id);

    @GET("allstreams_android_tv.php")
    Call<VODResponse> getStreams(@Query("user")String user, @Query("pass")String pass,@Query("page")String page);
}
