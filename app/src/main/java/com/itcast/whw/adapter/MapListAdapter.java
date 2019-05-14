package com.itcast.whw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.itcast.whw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近信息的适配器
 */
public class MapListAdapter extends BaseAdapter {
    private List<PoiInfo> poiInfoList = new ArrayList<>();
    private Context mContext;
    public MapListAdapter(List<PoiInfo> poiInfoList, Context context) {
        this.poiInfoList = poiInfoList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return poiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.map_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_address = view.findViewById(R.id.tv_address);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        PoiInfo poiInfo = poiInfoList.get(position);
        viewHolder.tv_name.setText(poiInfo.name);
        viewHolder.tv_address.setText(poiInfo.address);
        return view;
    }

    class ViewHolder {
        private TextView tv_name;
        private TextView tv_address;
    }
}
