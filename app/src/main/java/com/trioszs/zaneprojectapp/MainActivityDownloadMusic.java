package com.trioszs.zaneprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivityDownloadMusic extends AppCompatActivity {

    private final String salaamitoURL = "https://www.zzonawav.com/assets/audio/salaamitoB.mp3";
    private final String giraffeLanguageURL = "https://www.zzonawav.com/assets/audio/giraffeLanguage.mp3";
    ProgressBar progressOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_download_music);

        progressOne = findViewById(R.id.progressBarOne);

        Button downloadOne = findViewById(R.id.buttonDownloadOne);
        Button downloadTwo = findViewById(R.id.buttonDownloadTwo);

        progressOne.setVisibility(View.GONE);

        downloadOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(salaamitoURL, "salaamitoB.mp3");

            }
        });

        downloadTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(giraffeLanguageURL, "giraffeLanguage.mp3");

            }
        });
    } // onCreate

    public void startDownload(String url, String fileName) {

        DownloadManager.Request mRqRequest = new DownloadManager.Request(Uri.parse(url));
        mRqRequest.setTitle(fileName);
        mRqRequest.setDescription("audio");
        mRqRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        mRqRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(mRqRequest);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));
                if(cursor != null && cursor.moveToNext()) {
                    @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    cursor.close();

                    switch (status) {
                        case DownloadManager.STATUS_RUNNING:
                            MainActivityDownloadMusic.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivityDownloadMusic.this, "Content Downloading...",
                                            Toast.LENGTH_SHORT).show();
                                    progressOne.setVisibility(View.VISIBLE);
                                }
                            });
                            break;

                        case DownloadManager.STATUS_SUCCESSFUL:
                            MainActivityDownloadMusic.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivityDownloadMusic.this, "Download Complete :)",
                                            Toast.LENGTH_SHORT).show();
                                    progressOne.setVisibility(View.GONE);
                                }
                            });
                            timer.cancel();
                            break;

                        case DownloadManager.STATUS_FAILED:

                            MainActivityDownloadMusic.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivityDownloadMusic.this, "Download failed :(",
                                            Toast.LENGTH_SHORT).show();
                                    progressOne.setVisibility(View.GONE);
                                }
                            });

                            timer.cancel();
                            break;
                    } // switch
                } //if

            } // run()

        },1000, 2000);

    } // startDownload
} // class