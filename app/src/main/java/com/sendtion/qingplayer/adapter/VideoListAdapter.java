package com.sendtion.qingplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.bean.MediaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengDecheng on 2017/5/16.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：
 */

public class VideoListAdapter extends BaseAdapter {
    private List<MediaInfo> videoInfoList;

    public VideoListAdapter(){
        videoInfoList = new ArrayList<>();
    }

    public void setVideoInfoList(List<MediaInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    @Override
    public int getCount() {
        if (videoInfoList != null && videoInfoList.size() > 0){
            return videoInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_video_thumb = (ImageView) view.findViewById(R.id.iv_video_thumb);
            viewHolder.tv_video_name = (TextView) view.findViewById(R.id.tv_video_name);
            viewHolder.tv_video_time = (TextView) view.findViewById(R.id.tv_video_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MediaInfo videoInfo = videoInfoList.get(position);
        Glide.with(parent.getContext()).load(videoInfo.getThumbnailPath()).into(viewHolder.iv_video_thumb);
        viewHolder.tv_video_name.setText(videoInfo.getName());
        viewHolder.tv_video_time.setText(generateTime(videoInfo.getDuration()));

        return view;
    }

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    class ViewHolder{
        ImageView iv_video_thumb;
        TextView tv_video_name;
        TextView tv_video_time;
    }
}
