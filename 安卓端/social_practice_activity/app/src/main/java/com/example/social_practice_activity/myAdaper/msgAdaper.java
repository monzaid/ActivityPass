package com.example.social_practice_activity.myAdaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myMsg;

import java.util.List;

public class msgAdaper extends BaseAdapter {

    private List<myMsg> list;
    private ListView listview;

    public msgAdaper(List<myMsg> list) {
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
                    R.layout.inform_item_list, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        myMsg myMsg = list.get(position);
        holder.title.setText(myMsg.title);
        holder.time.setText(myMsg.getTime());
        holder.content.setText(myMsg.content);
        return convertView;
    }

    class ViewHolder {
        TextView time, title, content;
    }
}