package com.wu.base.update;

import java.util.Observable;

/**
 * 作者: 吴奎庆
 * <p>
 * 时间: 2019/8/23
 * <p>
 * 简介:  版本更新状态的 观察者
 */
public class UpdateStateObservable extends Observable {

    static UpdateStateObservable instance;

    public static UpdateStateObservable getInstance() {
        if (instance == null) {
            synchronized (UpdateStateObservable.class) {
                if (instance == null) {
                    instance = new UpdateStateObservable();
                }
            }
        }
        return instance;
    }

    /**
     *  版本更新的状态
     * @param state 1 开始更新  2 更新异常 3 强更新逻辑
     */
    public void updateChangeState(int state) {
        setChanged();
        notifyObservers(state);
    }

}
