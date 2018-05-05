package com.takwira.hamza.takwira.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by hamza on 04/08/17.
 */


public class BitmapDownloader extends AsyncTask<String, Void , Bitmap> {

    ProgressDialog progressBar ;
    int progressBarStatus ;
    int fileSize;

    public BitmapDownloader(Context context) {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        fileSize = 0 ;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
//        while (progressBarStatus < 100) {
//            progressBarStatus = downloadFile();
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        if (progressBarStatus >= 100) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            progressBar.dismiss();
//        }
    }

    protected Bitmap doInBackground(String... url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url[0]);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;

    }

    protected void onPostExecute(Bitmap feed) {
        progressBar.dismiss();
    }
//
//    private int downloadFile() {
//
//        while (fileSize <= 1000000) {
//            fileSize++;
//
//            if (fileSize == 100000) {
//                return 10;
//            }else if (fileSize == 200000) {
//                return 20;
//            }else if (fileSize == 300000) {
//                return 30;
//            }else if (fileSize == 400000) {
//                return 40;
//            }else if (fileSize == 500000) {
//                return 50;
//            }else if (fileSize == 700000) {
//                return 70;
//            }else if (fileSize == 800000) {
//                return 80;
//            }
//        }
//        return 100;
//    }
}





