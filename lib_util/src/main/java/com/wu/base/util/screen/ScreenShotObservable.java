package com.wu.base.util.screen;


import java.util.Observable;

/**
 * @author wkq
 * @date 2021年08月20日 13:57
 * @des 截图成功的观察者
 */

public class ScreenShotObservable extends Observable {
    static ScreenShotObservable instance;

    public static ScreenShotObservable getInstance() {
        if (instance == null) {
            synchronized (ScreenShotObservable.class) {
                if (instance == null) {
                    instance = new ScreenShotObservable();
                }
            }
        }
        return instance;
    }

    public void updateScreenShot(String screenShotPath) {
        setChanged();
        notifyObservers(screenShotPath);
    }
}
