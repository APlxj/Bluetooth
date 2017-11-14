package com.ap.model;

import android.bluetooth.BluetoothDevice;

/**
 * 类描述：
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class Bluetooth {

    private BluetoothDevice device;
    private String name;
    private String address;

    private Bluetooth(Builder builder) {
        device = builder.device;
        name = builder.name;
        address = builder.address;
    }

    public static class Builder {
        private String name;
        private String address;
        private BluetoothDevice device;

        public Builder Device(BluetoothDevice device) {
            this.device = device;
            return this;
        }

        public Builder Name(String name) {
            this.name = name;
            return this;
        }

        public Builder Address(String address) {
            this.address = address;
            return this;
        }

        public Bluetooth build() {
            return new Bluetooth(this);
        }
    }
}
