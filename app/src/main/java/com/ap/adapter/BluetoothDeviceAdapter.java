package com.ap.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ap.R;
import com.ap.base.SuperAdapter;

/**
 * 类描述：
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class BluetoothDeviceAdapter extends SuperAdapter<BluetoothDevice> {

    public BluetoothDeviceAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mConText).inflate(R.layout.adapter_founddevice, null);
        LinearLayout ll_found = (LinearLayout) convertView.findViewById(R.id.ll_found);
        TextView name = (TextView) convertView.findViewById(R.id.tv_name);

        BluetoothDevice device = data.get(position);
        if (null == device.getName() || "".equals(device.getName())) {
            name.setText(device.getAddress());
        } else {
            name.setText(device.getName());
        }

        return convertView;
    }
}
