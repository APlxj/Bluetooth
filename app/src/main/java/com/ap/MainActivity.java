package com.ap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ap.adapter.BluetoothDeviceAdapter;
import com.ap.base.BaseActivity;
import com.ap.broadcast.BluetoothBroadCast;
import com.ap.broadcast.IBluetoothSearch;
import com.ap.broadcast.IBluetoothStatus;
import com.ap.utils.ToastUtil;
import com.ap.view.MeasureListView;
import com.ap.view.SendMsgDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //蓝牙开关
    private CheckBox cb_status;
    //适配器
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    //蓝牙状态监听器
    private BluetoothBroadCast searchBroadCast;
    //已绑定蓝牙显示
    private MeasureListView list_bond;
    private BluetoothDeviceAdapter bondDeviceAdapter;
    //其他蓝牙显示
    private MeasureListView list_found;
    private BluetoothDeviceAdapter foundDeviceAdapter;
    //蓝牙操作界面
    private LinearLayout ll_data;

    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private ProgressBar progressBar;

    BluetoothSocket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void initView() {
        ll_data = (LinearLayout) findViewById(R.id.ll_data);
        ll_data.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.myprobar);

        TextView localname = (TextView) findViewById(R.id.tv_localname);
        localname.setText(mAdapter.getName() /*+ ";" + mAdapter.getAddress()*/);

        TextView tv_visible = (TextView) findViewById(R.id.tv_visible);
        tv_visible.setText("现在可被为 “" + mAdapter.getName() + "”");

        cb_status = (CheckBox) findViewById(R.id.cb_status);
        findViewById(R.id.ll_search).setOnClickListener(this);

        list_bond = (MeasureListView) findViewById(R.id.list_bond);
        bondDeviceAdapter = new BluetoothDeviceAdapter(this);
        list_bond.setAdapter(bondDeviceAdapter);

        list_found = (MeasureListView) findViewById(R.id.list_found);
        foundDeviceAdapter = new BluetoothDeviceAdapter(this);
        list_found.setAdapter(foundDeviceAdapter);
    }

    private void init() {
        if (null == mAdapter) {
            ToastUtil.showToast("此系统无蓝牙设备");
            return;
        }

        /*//启动修改蓝牙可见性的Intent
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //设置蓝牙可见性的时间，方法本身规定最多可见300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);*/

        //搜索设备：蓝牙开启状态下
        searchDevice();

        //获取已绑定设备
        if (mAdapter.isEnabled()) {
            Set<BluetoothDevice> bondDevice = mAdapter.getBondedDevices();
            List<BluetoothDevice> devices = new ArrayList<>();
            if (null != bondDevice)
                for (BluetoothDevice device : bondDevice) {
                    devices.add(device);
                }
            bondDeviceAdapter.putNewData(devices);
        }
        //开关蓝牙操作
        cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mAdapter.isEnabled()) {
                        ToastUtil.showToast("蓝牙已打开");
                    } else {
                        mAdapter.enable();
                    }
                } else {
                    if (!mAdapter.isEnabled()) {
                        ToastUtil.showToast("蓝牙已关闭");
                    } else {
                        mAdapter.disable();
                    }
                }
            }
        });

        list_bond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bondDeviceAdapter.getItem(position);
                connect(device);
            }
        });

        list_found.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = foundDeviceAdapter.getItem(position);
                device.createBond();
                //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
                //Method method = BluetoothDevice.class.getMethod("createBond");
                //Log.e(getPackageName(), "开始配对");
                //method.invoke(device);
            }
        });

        //注册蓝牙状态监听
        searchBroadCast = new BluetoothBroadCast(new IBluetoothStatus() {
            @Override
            public void off() {
                ll_data.setVisibility(View.GONE);
                ToastUtil.showToast("蓝牙已关闭");
            }

            @Override
            public void runOff() {

            }

            @Override
            public void on() {
                ll_data.setVisibility(View.VISIBLE);
                searchDevice();
            }

            @Override
            public void runOn() {

            }
        }, new IBluetoothSearch() {
            @Override
            public void findDevice(Set<BluetoothDevice> bluetooths) {
                List<BluetoothDevice> devices = new ArrayList<>();
                if (null != bluetooths)
                    for (BluetoothDevice device : bluetooths) {
                        devices.add(device);
                    }
                foundDeviceAdapter.putNewData(devices);
            }

            @Override
            public void onBltIng(BluetoothDevice device) {
                //正在配对
            }


            @Override
            public void onBltEnd(BluetoothDevice device) {
                //完成配对
                searchDevice();
            }

            @Override
            public void onBltNone(BluetoothDevice device) {
                //取消配对
            }
        });
        IntentFilter searchfilter = new IntentFilter();
        searchfilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索设备
        searchfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//开关
        searchfilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        searchfilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        registerReceiver(searchBroadCast, searchfilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //搜索设备
            case R.id.ll_search:
                searchDevice();
                break;
        }
    }

    private void searchDevice() {
        /*progressBar.setVisibility(View.VISIBLE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                mAdapter.cancelDiscovery();
                timer.cancel();
            }
        }, 5000, 5000);*/
        /*bondDeviceAdapter.clearData();
        foundDeviceAdapter.clearData();*/
        if (mAdapter == null) {
            // 设备不支持蓝牙
            return;
        }
        if (!mAdapter.isEnabled()) return;

        //获取已绑定设备
        Set<BluetoothDevice> bondDevice = mAdapter.getBondedDevices();
        List<BluetoothDevice> devices = new ArrayList<>();
        if (null != bondDevice)
            for (BluetoothDevice device : bondDevice) {
                devices.add(device);
            }
        bondDeviceAdapter.putNewData(devices);

        ll_data.setVisibility(View.VISIBLE);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        while (!mAdapter.startDiscovery()) {
            Log.e("BlueTooth", "打开蓝牙失败");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect(BluetoothDevice device) {
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast("socket 打开失败");
        }
        //如果在搜索状态，则关闭搜索
        if (mAdapter.isDiscovering()) mAdapter.cancelDiscovery();
        try {
            if (socket != null && !socket.isConnected()) {
                socket.connect();
                showSengMsgDialog();
                ToastUtil.showToast("socket 连接成功");
            } else {
                showSengMsgDialog();
            }
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast("socket 连接失败");
        }
    }

    void showSengMsgDialog() {
        new SendMsgDialog(this, new SendMsgDialog.ISendMsgListener() {
            @Override
            public void sendMsg(String msg) {
                sengMsg(msg);
            }

            @Override
            public void cancel() {
                if (null != socket) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).show();
    }

    private void sengMsg(String message) {
        if (socket == null || !socket.isConnected()) {
            ToastUtil.showToast("请先连接蓝牙");
            return;
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchBroadCast);
    }
}
