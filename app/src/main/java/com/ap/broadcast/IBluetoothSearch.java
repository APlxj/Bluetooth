package com.ap.broadcast;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * 类描述：
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public interface IBluetoothSearch {
    void findDevice(Set<BluetoothDevice> bluetooths);

    void onBltIng(BluetoothDevice device);

    void onBltEnd(BluetoothDevice device);

    void onBltNone(BluetoothDevice device);
}
