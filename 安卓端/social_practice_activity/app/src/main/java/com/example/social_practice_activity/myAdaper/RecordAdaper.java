package com.example.social_practice_activity.myAdaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.social_practice_activity.R;
import com.example.social_practice_activity.bean.myActivityRegister;
import java.util.List;

public class RecordAdaper extends BaseAdapter  implements View.OnClickListener {

    private List<myActivityRegister> list;
    private ListView listview;
    private InnerItemOnclickListener mListener;

    public RecordAdaper(List<myActivityRegister> list) {
        super();
        System.out.println("sssss" + list);
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
                    R.layout.fragment_table, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.stute = (TextView) convertView.findViewById(R.id.tv_stute);
            holder.telephone = (TextView) convertView.findViewById(R.id.tv_telephone);
            holder.join = (Button) convertView.findViewById(R.id.btn_ac);
            holder.delete = (Button) convertView.findViewById(R.id.btn_no);
            holder.sign = (Button) convertView.findViewById(R.id.btn_sign);
            holder.call = (Button) convertView.findViewById(R.id.btn_call);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        myActivityRegister myActivityRegister = list.get(position);
        holder.username.setText(myActivityRegister.username);
        holder.stute.setText(myActivityRegister.stute);
        holder.telephone.setText(myActivityRegister.telephone);
        holder.join.setOnClickListener(this);
        holder.join.setTag(position);
        holder.delete.setOnClickListener(this);
        holder.delete.setTag(position);
        holder.sign.setOnClickListener(this);
        holder.sign.setTag(position);
        holder.call.setOnClickListener(this);
        holder.call.setTag(position);
        return convertView;
    }

    class ViewHolder {
        TextView username, stute, telephone;
        Button join, delete, sign, call;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}