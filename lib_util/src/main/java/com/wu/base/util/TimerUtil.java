package com.wu.base.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.lang.ref.WeakReference;

public class TimerUtil {
    private long totalTime = -1L;
    private long intervalTime = 0L;
    private long remainTime;
    private long systemAddTotalTime;
    private static final int TIME = 1;
    private TimeListener listener;
    private long curReminTime;
    private boolean isPause = false;
    private MyHandler mHandler = new MyHandler(this);

    public TimerUtil() {
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getIntervalTime() {
        return this.intervalTime;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void start() {
        if (this.totalTime <= 0L && this.intervalTime <= 0L) {
            throw new RuntimeException("you must set the totalTime > 0 or intervalTime >0");
        } else {
            this.systemAddTotalTime = SystemClock.elapsedRealtime() + this.totalTime;
            if (null != this.mHandler) {
                this.mHandler.sendEmptyMessage(1);
            }

        }
    }

    public void cancel() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
        }

    }

    public void release() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeCallbacks((Runnable) null);
            this.mHandler = null;
        }

    }

    public void pause() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.isPause = true;
            this.curReminTime = this.remainTime;
        }

    }

    public void resume() {
        if (this.isPause) {
            this.isPause = false;
            this.totalTime = this.curReminTime;
            this.start();
        }

    }

    private void soloveTime() {
        this.remainTime = this.systemAddTotalTime - SystemClock.elapsedRealtime();
        if (this.remainTime <= 0L) {
            if (this.listener != null) {
                this.listener.onFinish();
                this.cancel();
            }
        } else if (this.remainTime < this.intervalTime) {
            if (null != this.mHandler) {
                this.mHandler.sendEmptyMessageDelayed(1, this.remainTime);
            }
        } else {
            long curSystemTime = SystemClock.elapsedRealtime();
            if (this.listener != null) {
                this.listener.onInterval(this.remainTime);
            }

            long delay;
            for (delay = curSystemTime + this.intervalTime - SystemClock.elapsedRealtime(); delay < 0L; delay += this.intervalTime) {
                ;
            }

            if (null != this.mHandler) {
                this.mHandler.sendEmptyMessageDelayed(1, delay);
            }
        }

    }

    public void setTimerLiener(TimeListener listener) {
        this.listener = listener;
    }

    public interface TimeListener {
        void onFinish();

        void onInterval(long var1);
    }

    private static class MyHandler extends Handler {
        WeakReference<TimerUtil> mActivity;

        MyHandler(TimerUtil activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            TimerUtil theActivity = mActivity == null ? null : this.mActivity.get();

            if (theActivity == null) return;

            switch (msg.what) {
                case 1:
                    if (!theActivity.isPause) {
                        theActivity.soloveTime();
                    }
                    break;
                case 2:
                    theActivity.isPause = true;
            }

        }
    }
}
