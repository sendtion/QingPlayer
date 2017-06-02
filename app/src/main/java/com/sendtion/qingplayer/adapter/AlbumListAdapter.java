package com.sendtion.qingplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sendtion.qingplayer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShengDecheng on 2017/5/16.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：专辑列表
 */

public class AlbumListAdapter extends BaseAdapter {
    private Map<String, Integer> albumMap = null;
    private List<String> albumList = null;

    public AlbumListAdapter(){
        albumMap = new HashMap<>();
        albumList = new ArrayList<>();
    }

    public void setAlbumMap(Map<String, Integer> albumMap) {
        this.albumMap = albumMap;
    }

    public void setAlbumList(List<String> albumList) {
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        if (albumList != null && albumList.size() > 0){
            return albumList.size();
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_album_name = (TextView) view.findViewById(R.id.tv_album_name);
            viewHolder.tv_video_count = (TextView) view.findViewById(R.id.tv_video_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String album = albumList.get(position);
        int count = albumMap.get(album);
        viewHolder.tv_album_name.setText(album);
        viewHolder.tv_video_count.setText(count+" item");

        return view;
    }

    class ViewHolder{
        TextView tv_album_name;
        TextView tv_video_count;
    }
}
