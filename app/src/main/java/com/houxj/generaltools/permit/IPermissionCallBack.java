package com.houxj.generaltools.permit;

/**
 * Created by 侯晓戬 on 2018/7/4.
 * 运行时权限结果回调
 */

public interface IPermissionCallBack {
    public final static int PERMISSION_RELUST_OK = 0;
    public final static int PERMISSION_RELUST_FAIL = -1;
    public void onResult(int result);
}
