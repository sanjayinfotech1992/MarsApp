package com.example.mars.ui;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mars.R;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class VideoViewActivity extends AppCompatActivity {
    VideoView videoView;
    ImageView btn_back;
   ProgressBar progress_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        videoView=findViewById(R.id.videoView);
        btn_back=findViewById(R.id.btn_back);
        progress_bar=findViewById(R.id.progress_bar);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String youtubeLink=getIntent().getStringExtra("url");
        final String videoUrl="";
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                progress_bar.setVisibility(View.GONE);
                if (ytFiles != null) {
                    int itag = 22;
                    String videoUrl = ytFiles.get(itag).getUrl();
                    videoView.setVideoPath(videoUrl);
                    videoView.start();
                }else{
                    Toast.makeText(VideoViewActivity.this,"Something went wrong!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.extract(youtubeLink, true, true);

    }
}
