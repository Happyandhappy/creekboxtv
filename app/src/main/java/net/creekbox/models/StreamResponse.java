package net.creekbox.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by opnchaudhary on 6/7/16.
 */
public class StreamResponse implements Serializable{
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("next")
    @Expose
    private Boolean next;
    @SerializedName("allstreams")
    @Expose
    private List<Allstream> allstreams = new ArrayList<Allstream>();
    @SerializedName("status")
    @Expose
    private Boolean status;

    /**
     *
     * @return
     * The count
     */
    public String getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The next
     */
    public Boolean getNext() {
        return next;
    }

    /**
     *
     * @param next
     * The next
     */
    public void setNext(Boolean next) {
        this.next = next;
    }

    /**
     *
     * @return
     * The allstreams
     */
    public List<Allstream> getAllstreams() {
        return allstreams;
    }

    /**
     *
     * @param allstreams
     * The allstreams
     */
    public void setAllstreams(List<Allstream> allstreams) {
        this.allstreams = allstreams;
    }

    /**
     *
     * @return
     * The status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

}
