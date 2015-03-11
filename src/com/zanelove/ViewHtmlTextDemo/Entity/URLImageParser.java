package com.zanelove.ViewHtmlTextDemo.Entity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by ZaneLove on 2015/3/9.
 */
public class URLImageParser implements Html.ImageGetter {
    private static final String TAG = "URLImageParser";
    Activity c;
    View container;
    int disHeight;
    private String basePath;

    private int containerWidth;
    private OnLoadOkListener onLoadOkListener;

    public void setOnLoadOkListener(OnLoadOkListener onLoadOkListener) {
        this.onLoadOkListener = onLoadOkListener;
    }

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the
     * container
     *
     * @param t
     * @param c
     */
    public URLImageParser(View t, Activity c, int width) {
        init(t, c);
        containerWidth = width;
    }

    public URLImageParser(View t, Activity c) {
        init(t, c);
    }

    private void init(View t, Activity c) {
        this.c = c;
        this.container = t;
        basePath = FileTool.SDPATH + "/image/";
    }

    public Drawable getDrawable(String source) {
        String path = basePath + Tool.changeUrl2Path(source) + "img";
        File file = new File(path);
        // 不管图片是否真实可获取，只要路径存在就认为有图片
        if (file.exists()) {
            Drawable drawable = null;
            try {
                drawable = BitmapUtil.getDrawable(path);
                setDrawableBound(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(Tool.replaceSpace(source));

        // return reference to URLDrawable where I will change with actual image
        // from
        // the src tag
        return urlDrawable;
    }

    // 设置drawable 大小
    private void setDrawableBound(Drawable drawable) {
        int height = (int) (containerWidth * drawable.getIntrinsicHeight() * 1.0 / drawable.getIntrinsicWidth());
        disHeight += height - drawable.getIntrinsicHeight();

        if (containerWidth != 0)
            drawable.setBounds(0, 0, containerWidth, height);
        else
            drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;
        private String source;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());
            // urlDrawable.setBounds(0, disHeight, 0 + containerWidth, disHeight
            // + result.getIntrinsicHeight());

            String path = basePath + Tool.changeUrl2Path(source) + "img";
            File file = new File(path);
            if (file.exists() && onLoadOkListener != null) {
                onLoadOkListener.invalidate();// 监听来刷新，如果没监听，就直接从网络获取，只是如果图片多的话会出现叠加的现象
                return;
            }

            urlDrawable.drawable = result;
            container.invalidate();

        }

        /***
         * Get the Drawable from URL
         *
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                String path = basePath + Tool.changeUrl2Path(urlString) + "img";
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");
                setDrawableBound(drawable);
                try {
                    BitmapUtil.saveDrawable(drawable, path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is.close();
                return drawable;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }

    public interface OnLoadOkListener {
        void invalidate();
    }

    public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}