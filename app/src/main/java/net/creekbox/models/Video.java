package net.creekbox.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by opnchaudhary on 6/7/16.
 */
public class Video implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("video_filename")
    @Expose
    private String videoFilename;
    @SerializedName("image_1")
    @Expose
    private String image1;
    @SerializedName("image_2")
    @Expose
    private String image2;
    @SerializedName("image_3")
    @Expose
    private String image3;
    @SerializedName("rtmp_stream")
    @Expose
    private String rtmpStream;
    @SerializedName("hls_stream")
    @Expose
    private String hlsStream;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The length
     */
    public String getLength() {
        return length;
    }

    /**
     *
     * @param length
     * The length
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     *
     * @return
     * The videoFilename
     */
    public String getVideoFilename() {
        return videoFilename;
    }

    /**
     *
     * @param videoFilename
     * The video_filename
     */
    public void setVideoFilename(String videoFilename) {
        this.videoFilename = videoFilename;
    }

    /**
     *
     * @return
     * The image1
     */
    public String getImage1() {
        return image1;
    }

    /**
     *
     * @param image1
     * The image_1
     */
    public void setImage1(String image1) {
        this.image1 = image1;
    }

    /**
     *
     * @return
     * The image2
     */
    public String getImage2() {
        return image2;
    }

    /**
     *
     * @param image2
     * The image_2
     */
    public void setImage2(String image2) {
        this.image2 = image2;
    }

    /**
     *
     * @return
     * The image3
     */
    public String getImage3() {
        return image3;
    }

    /**
     *
     * @param image3
     * The image_3
     */
    public void setImage3(String image3) {
        this.image3 = image3;
    }

    /**
     *
     * @return
     * The rtmpStream
     */
    public String getRtmpStream() {
        return rtmpStream;
    }

    /**
     *
     * @param rtmpStream
     * The rtmp_stream
     */
    public void setRtmpStream(String rtmpStream) {
        this.rtmpStream = rtmpStream;
    }

    /**
     *
     * @return
     * The hlsStream
     */
    public String getHlsStream() {
        return hlsStream;
    }

    /**
     *
     * @param hlsStream
     * The hls_stream
     */
    public void setHlsStream(String hlsStream) {
        this.hlsStream = hlsStream;
    }
}
