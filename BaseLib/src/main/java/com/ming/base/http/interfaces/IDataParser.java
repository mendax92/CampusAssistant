package com.ming.base.http.interfaces;

/**
 * Created by ming on 2016/8/12.
 */
public interface IDataParser<Data, T> {

    /**
     * [解析数据]<br/>
     * @param data 需要解析的数据
     * @return Object 解析后的数据
     */
    T parseData(Data data);

}
