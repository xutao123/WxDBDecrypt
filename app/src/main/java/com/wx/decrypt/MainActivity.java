package com.wx.decrypt;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.wx.decrypt.request.WxRequestManager;
import com.wx.decrypt.threadpool.ThreadPoolManager;
import com.wx.decrypt.util.SPHelper;
import com.wx.decrypt.util.UrlUtil;

import static com.wx.decrypt.util.UrlUtil.ONLINE_HOST;
import static com.wx.decrypt.util.UrlUtil.TEST_HOST;

/**
 *  微信语音消息，图片，视频，收藏的语音，图片视频都是存放在本地，对应而本地的路径规则是
 *  Environment.getExternalStorageDirectory().getPath() + "/"+tencent/MicroMsg/+ Md5Utils.md5Encode("mm" + uid)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button mSearchDataBtn;
    Spinner mSpinner;
    TextView mEnv;

    private static final int ONLINE_DOMAIN_POS = 0;
    private static final int TEST_DOMAIN_POS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchDataBtn = findViewById(R.id.btn);
        mSearchDataBtn.setOnClickListener(this);

        mEnv = findViewById(R.id.env);

        mSpinner = findViewById(R.id.spinner);
        final String[] citys = new String[]{"线上环境", "测试环境"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,citys);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        setSelection();

        Looper.prepare();
        Looper.loop();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        this.finish();
    }

    private void setSelection() {
        String domainAddress = SPHelper.getInstance().getRootDomainPath();
        mEnv.setText("当前使用的域名地址是：" + domainAddress);
        if (domainAddress.equals(UrlUtil.ONLINE_HOST)) {
            mSpinner.setSelection(0);
        } else if (domainAddress.equals(UrlUtil.TEST_HOST)) {
            mSpinner.setSelection(1);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn:
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        WxRequestManager.getInstance().uploadWxInfo();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == ONLINE_DOMAIN_POS) {
            SPHelper.getInstance().setRootDomainPath(ONLINE_HOST);
            mEnv.setText("当前使用的域名地址是：" + ONLINE_HOST);
        } else if (position == TEST_DOMAIN_POS) {
            SPHelper.getInstance().setRootDomainPath(TEST_HOST);
            mEnv.setText("当前使用的域名地址是：" + TEST_HOST);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
