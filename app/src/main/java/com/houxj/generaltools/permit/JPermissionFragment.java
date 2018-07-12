package com.houxj.generaltools.permit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.houxj.generaltools.utils.JLogEx;

import java.util.ArrayList;

/**
 * Created by 侯晓戬 on 2018/7/4.
 * 用来加入到 Activity 中实现授权回调的 Fragment
 */

public class JPermissionFragment extends Fragment {
    private static final String PERMISSION_LIST = "PERMISSION_LIST";
    private static final String REQUEST_CODE ="REQUEST_CODE";

    public static JPermissionFragment newInstant(ArrayList<String> permissions, int requestCode){
        JPermissionFragment fragment = new JPermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PERMISSION_LIST, permissions);
        bundle.putInt(REQUEST_CODE, requestCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        JLogEx.d();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ArrayList<String> list = getArguments().getStringArrayList(PERMISSION_LIST);
            int code = getArguments().getInt(REQUEST_CODE);
            if(null != list){
                String[] permissions = new String[list.size()];
                permissions = list.toArray(permissions);
                requestPermissions(permissions, code);
            }else{
                onRequestPermissionsResult(code, new String[]{""}, new int[]{0});
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JLogEx.d("%d", requestCode);
        JRunTimePermissions.onPermissionResult(requestCode,permissions,grantResults);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    //TODO 加载进入activity中开启检查
    public void requestPermission(FragmentManager manager){
        if(null != manager) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, this.getClass().getName());
            ft.commit();
        }
    }
}
