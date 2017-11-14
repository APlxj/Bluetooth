package com.ap.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ap.utils.ToastUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：蓝牙搜索监听器
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class BluetoothBroadCast extends BroadcastReceiver {

    private IBluetoothSearch iBluetoothSearch;
    private IBluetoothStatus iBluetoothStatus;
    private Set<BluetoothDevice> bluetooths = new HashSet<>();

    public BluetoothBroadCast(IBluetoothStatus iBluetoothStatus, IBluetoothSearch iBluetoothSearch) {
        this.iBluetoothSearch = iBluetoothSearch;
        this.iBluetoothStatus = iBluetoothStatus;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device;
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // TODO: 发现设备
            // 从Intent中获取设备对象
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 将设备名称和地址放入array adapter，以便在ListView中显示
            bluetooths.add(device);
            if (null != iBluetoothSearch) iBluetoothSearch.findDevice(bluetooths);
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            // TODO: 配对状态
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING:
                    ToastUtil.showToast("正在配对......");
                    if (null != iBluetoothSearch) iBluetoothSearch.onBltIng(device);
                    break;
                case BluetoothDevice.BOND_BONDED:
                    ToastUtil.showToast("完成配对");
                    if (null != iBluetoothSearch) iBluetoothSearch.onBltEnd(device);
                    break;
                case BluetoothDevice.BOND_NONE:
                    ToastUtil.showToast("取消配对");
                    if (null != iBluetoothSearch) iBluetoothSearch.onBltNone(device);
                default:
                    break;
            }
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            // TODO: 开关状态
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
                    if (null != iBluetoothStatus) iBluetoothStatus.off();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                    if (null != iBluetoothStatus) iBluetoothStatus.runOff();
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d("aaa", "STATE_ON 手机蓝牙开启");
                    if (null != iBluetoothStatus) iBluetoothStatus.on();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                    if (null != iBluetoothStatus) iBluetoothStatus.runOn();
                    break;
            }
        }
    }
}
