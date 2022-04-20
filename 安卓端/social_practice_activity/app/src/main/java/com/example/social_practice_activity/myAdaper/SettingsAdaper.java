package com.example.social_practice_activity.myAdaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.mySettings;

import java.util.List;

public class SettingsAdaper extends BaseAdapter {

    private List<mySettings> list;
    private ListView listview;

    public SettingsAdaper(List<mySettings> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (listview == null) {
            listview = (ListView) parent;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.settings_item_list, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mySettings mySettings = list.get(position);
        holder.name.setText(mySettings.name);
        holder.content.setText(mySettings.content);
        return convertView;
    }

    class ViewHolder {
        TextView name, content;
    }
}