package com.zanelove.ViewHtmlTextDemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zanelove.ViewHtmlTextDemo.Entity.DetailEntity;
import com.zanelove.ViewHtmlTextDemo.Entity.URLImageParser;
import com.zanelove.ViewHtmlTextDemo.Utils.GsonUtil;

public class MyActivity extends Activity {
    //TODO
    private String url = "url接口";
    private DetailEntity entity;
    private String infos;
    private TextView tv_infos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
    }

    private void initView() {
        tv_infos = (TextView) findViewById(R.id.tv_infos);
    }

    private void initData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                entity = GsonUtil.json2Bean(MyActivity.this, responseInfo.result, DetailEntity.class);
                fillData();
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void fillData() {
        infos = entity.body;
        setViewHtmlText();
    }

    private void setViewHtmlText() {
        if (infos != null && !infos.equals("")) {
            final URLImageParser p = new URLImageParser(tv_infos, this,tv_infos.getMeasuredWidth());
            final Spanned text = Html.fromHtml(infos, p, null);
            p.setOnLoadOkListener(new URLImageParser.OnLoadOkListener() {

                @Override
                public void invalidate() {
                    setViewHtmlText();
                }
            });
            try {
                tv_infos.setText(text);
                tv_infos.setMovementMethod(LinkMovementMethod.getInstance()); //添加连接方式
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
