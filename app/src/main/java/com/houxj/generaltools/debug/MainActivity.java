package com.houxj.generaltools.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.houxj.generaltools.R;
import com.houxj.generaltools.permit.IPermissionCallBack;
import com.houxj.generaltools.permit.JRunTimePermissions;
import com.houxj.generaltools.utils.JAppCache;
import com.houxj.generaltools.utils.JImageLoader;
import com.houxj.generaltools.utils.JLogEx;
import com.houxj.generaltools.utils.JMd5Tools;
import com.houxj.generaltools.utils.JSharedP;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements IPermissionCallBack {

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JLogEx.setDebugEnable(true);
        JImageLoader.initLoader(this);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.image_test);
        JRunTimePermissions.with(this)
                .check(this);
    }

    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.but_local_pic){
            JImageLoader.createLoader()
                    .setCircleOptions()
                    .displayImage(R.mipmap.mypic,mImageView);
        }else if(id == R.id.but_net_pic){
            loadNetPic();
        }else if(id == R.id.but_cache){
            listAppDir();
            getCacheSize();
            JLogEx.d("清理");
            JAppCache.cleanAppCacheData(this);
            JImageLoader.cleanCache(this);
            getCacheSize();
        }else if(id == R.id.but_perm){
            readUsePerm();
        }else if(id == R.id.but_md5){
            testMd5();
        }else if(id == R.id.but_sp_list){
            testSpList();
        }
    }

    private void testSpList(){
        List<String> lst = new ArrayList<>();
        lst.add("今天的天气不错");
        lst.add("hello world");
        lst.add("明天不会下雨的");
        JLogEx.d(lst.toArray());
        JSharedP.setListValue(getApplicationContext(),"key", lst);
        List<String> values = JSharedP.getListValue(getApplicationContext(),"key",String.class);
        JLogEx.d(values.toArray());

        Stu mystu = new Stu();
        mystu.id = 1;
        mystu.name ="张三";
        mystu.age = 12;
        mystu.grade =98.5f;

        Stu mystu2 = new Stu();
        mystu2.id = 2;
        mystu2.name ="李国友";
        mystu2.age = 14;
        mystu2.grade =88.2f;

        List<Stu> stu_lst = new ArrayList<>();
        stu_lst.add(mystu);
        stu_lst.add(mystu2);
        JLogEx.d(stu_lst.toArray());
        JSharedP.setListValue(getApplicationContext(),"STU", stu_lst);
        List<Stu> val_stu = JSharedP.getListValue(getApplicationContext(),"STU1", Stu.class);
        if(null != val_stu) {
            JLogEx.d(val_stu.toArray());
        }

        JSharedP.setValue(getApplicationContext(),"xxx", mystu2);
        JLogEx.d(mystu2);
        Stu mystu3 = JSharedP.getValue(getApplicationContext(),"xxx1", Stu.class);
        if(null != mystu3) {
            JLogEx.d(mystu3);
        }
    }

    class Stu{
        int id;
        String name;
        int age;
        float grade;
        private int code;
    }

    private void testMd5(){
        String src = "hou13510901281inszxxx";
        JLogEx.d("%s", JMd5Tools.getMd5(src));
        JLogEx.d("测试一下");

        Stu mystu = new Stu();
        mystu.id = 1;
        mystu.name ="张三";
        mystu.age = 12;
        mystu.grade =98.5f;
        JLogEx.d(mystu);
        JLogEx.d(1);
        int xx = 2;
        JLogEx.d(xx);
        JLogEx.d(false);
        JLogEx.d(10.2f);
        JLogEx.d(10.222);
        JLogEx.d('c');
        JLogEx.d((byte)0x02);
    }

    private void readUsePerm(){
        JRunTimePermissions.goSystemPermissionSetting(this);
    }

    private void loadNetPic(){
        String url = "https://jiuye-res.jikexueyuan.com/zhiye/" +
                "showcase/attach-/20161208/0409c25c-77a2-498f-b583-066bd97a8ff7.jpg";
        JImageLoader.createLoader()
//                    .setRoundOptions(10)
                .setFedeInOptions(800)
                .displayImage(url, mImageView);
    }

    private void listAppDir(){
        File[] files = getFilesDir().getParentFile().listFiles();
        for (File file : files){
            JLogEx.d(file.getPath());
        }

    }

    //获取缓存大小
    private void getCacheSize(){
        JLogEx.d(JAppCache.fomatFileSize(JAppCache.getAppCacheSize(this)));
    }

    @Override
    public void onResult(int result) {
        JLogEx.d("%d", result);
    }
}
