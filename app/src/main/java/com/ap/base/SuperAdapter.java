package com.ap.base;

import android.app.Activity;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 类描述：adapter父类
 * 创建人：evan.yang
 * 创建时间：2016/5/13 9:25
 * Email: william.wang@kemai.cn
 * 修改备注：
 */
public abstract class SuperAdapter<T> extends BaseAdapter {

    protected Activity mConText;
    protected ArrayList<T> data = new ArrayList<T>();

    public SuperAdapter(Activity activity) {
        this.mConText = activity;
    }

    /***
     * 加载更多数据
     *
     * @param data
     */
    public void putMoreData(ArrayList<T> data) {
        if (null != data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    /***
     * 加载更多数据
     *
     * @param data
     */
    public void putMoreData(List<T> data) {
        if (null != data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public ArrayList<T> getData() {
        return data;
    }

    /***
     * 加载更多数据
     *
     * @param data
     */
    public void putMoreData(T data) {
        if (null != data) {
            this.data.add(data);
            notifyDataSetChanged();
        }
    }

    /***
     * 更新数据
     *
     * @param data
     */
    public void putNewData(List<T> data) {
        if (null != data) {
            this.data.clear();
            this.data.addAll(data);
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    /***
     * 更新数据
     *
     * @param bean
     */
    public void rePeat(T bean, int position) {
        if (null != bean && data.size() > position) {
            this.data.set(position, bean);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void delData(int position) {
        if (null != data && data.size() > position) {
            this.data.remove(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (null == data || position >= data.size() || position < 0) {
            return null;
        } else {
            return data.get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
