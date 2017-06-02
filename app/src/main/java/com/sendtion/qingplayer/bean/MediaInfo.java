package com.sendtion.qingplayer.bean;

/**
 * Created by ShengDecheng on 2017/5/16.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：视频信息
 */

public class MediaInfo {
    private String name;// Media.DISPLAY_NAME 带后缀的名字
    private String path;// DATA 路径
    private long size;// SIZE
    private String title;// TITLE 不带后缀的名字
    private long time;// DATE_MODIFIED 最后修改时间

    //DLNA用到的
    private String mediaID;
    private String album;
    private String mimeType;// 类型
    private long duration;// 时长
    private String resolution;// 分辨率
    private String artist;

    private String thumbnailPath;// 视频缩略图路径

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public String toString() {
        return "MediaInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", mediaID='" + mediaID + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", duration=" + duration +
                ", resolution='" + resolution + '\'' +
                ", artist='" + artist + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                '}';
    }
}
