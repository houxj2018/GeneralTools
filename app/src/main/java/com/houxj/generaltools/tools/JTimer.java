package com.houxj.generaltools.tools;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 侯晓戬 on 2018/7/25.
 * 定时器类
 */

public class JTimer {
    private Timer mTimer = null;
    private TimerTask mTask = null;
    private long mTimeInterval = 0;
    private long mTimeDelay = 0;
    private Object mParam = null;
    private ITimeCallBack mTimeCallBack = null;

    public JTimer(){
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                if(null != mTimeCallBack){
                    mTimeCallBack.onTime(mParam);
                }
            }
        };
    };

    //TODO 设置定时回调
    public JTimer setTimeCallBack(ITimeCallBack callBack){
        mTimeCallBack = callBack;
        return this;
    }

    //TODO 设置定时间隔（如果延时和间隔时间一样可以不设置延时）
    public JTimer setInterval(int intervalSec){
        mTimeInterval = intervalSec * 1000; //转毫秒
        return this;
    }

    //TODO 设置延时长度（设置了延时，会在延时到了执行一次）
    public JTimer setDelay(int dalaySec){
        mTimeDelay = dalaySec*1000;//转毫秒
        return this;
    }

    //TODO 设置回调参数
    public JTimer setParam(Object param){
        mParam = param;
        return this;
    }

    //TODO 开始定时
    public JTimer start(){
        if(mTimeInterval > 0){
            mTimeDelay = mTimeDelay> 0? mTimeDelay:mTimeInterval;
            mTimer.schedule(mTask, mTimeDelay, mTimeInterval);
        }else{
            mTimer.schedule(mTask, mTimeDelay>0?mTimeDelay:1);
        }
        return this;
    }

    //TODO 取消定时
    public void cancel(){
        mTimer.cancel();
        mTask = null;
    }

    //TODO 定时器定时回调
    public interface ITimeCallBack{
        void onTime(Object param);
    }
}
