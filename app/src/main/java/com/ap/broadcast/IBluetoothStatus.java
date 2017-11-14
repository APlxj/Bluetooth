package com.ap.broadcast;

/**
 * 类描述：蓝牙开关状态数据返回接口
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public interface IBluetoothStatus {
    void off();

    void runOff();

    void on();

    void runOn();
}
