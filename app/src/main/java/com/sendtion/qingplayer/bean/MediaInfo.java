package com.sendtion.qingplayer.bean;

import java.io.Serializable;

/**
 * Created by ShengDecheng on 2017/5/16.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：视频信息
 */

public class MediaInfo implements Serializable {
    private String name;// Media.DISPLAY_NAME 带后缀的名字
    private String path;// DATA 路径
    private long size;// SIZE
    private String title;// TITLE 不带后缀的名字
    private long createTime;// DATE_ADDED 创建时间
    private long updateTime;// DATE_MODIFIED 最后修改时间

    //DLNA用到的
    private String mediaID;//ID
    private String album;//专辑
    private String mimeType;// 类型
    private long duration;// 时长
    private String resolution;// 分辨率
    private String artist;//艺术家

    private String bookMark;//书签
    private String backetName;//空间名
    private String backetID;//空间ID
    private String category;//策略
    private String takeDate;//拍摄日期
    private String description;//描述
    private String language;//语言
    private String latitude;//纬度
    private String longitude;//经度
    private String tags;//标签
    private String miniThumbPath;//小缩略图
    private String isPrivate;//是否私有

    private String thumbPath;// 视频缩略图路径

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
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

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }

    public String getBacketName() {
        return backetName;
    }

    public void setBacketName(String backetName) {
        this.backetName = backetName;
    }

    public String getBacketID() {
        return backetID;
    }

    public void setBacketID(String backetID) {
        this.backetID = backetID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(String takeDate) {
        this.takeDate = takeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMiniThumbPath() {
        return miniThumbPath;
    }

    public void setMiniThumbPath(String miniThumbPath) {
        this.miniThumbPath = miniThumbPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String isPrivate() {
        return isPrivate;
    }

    public void setPrivate(String aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "MediaInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", mediaID='" + mediaID + '\'' +
                ", album='" + album + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", duration=" + duration +
                ", resolution='" + resolution + '\'' +
                ", artist='" + artist + '\'' +
                ", bookMark='" + bookMark + '\'' +
                ", backetName='" + backetName + '\'' +
                ", backetID='" + backetID + '\'' +
                ", category='" + category + '\'' +
                ", takeDate='" + takeDate + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", tags='" + tags + '\'' +
                ", miniThumbPath='" + miniThumbPath + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                '}';
    }
}
