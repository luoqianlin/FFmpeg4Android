package com.mabeijianxi.jianxiffmpegcmd;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("fdk-aac");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("swresample");
        System.loadLibrary("avfilter");
        System.loadLibrary("jxffmpegrun");
    }

    private ProgressBar pb;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar) findViewById(R.id.pb);
        surfaceView=findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        play(holder.getSurface());
                    }
                }).start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }
public void getConfig(View v){
        System.out.println(getFFmpegConfig());
}

public void goplay(View v){

}
    public void onClick(View v){
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String basePath = Environment.getExternalStorageDirectory().getPath();
                String cmd_transcoding = String.format("ffmpeg -i %s -c:v libx264 %s  -c:a libfdk_aac %s",
                        basePath+"/"+"17_组合模式_上.mp4",
                        "-crf 40",
                        basePath+"/"+"my_girl.mp4");
                int i = jxFFmpegCMDRun(cmd_transcoding);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"ok了",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    /**
     * 命令运行
     * @param cmd
     * @return
     */
    public  int jxFFmpegCMDRun(String cmd){
        String regulation="[ \\t]+";
        final String[] split = cmd.split(regulation);

        return ffmpegRun(split);
    }
    public native int ffmpegRun(String[] cmd);

    /**
     * 获取ffmpeg编译信息
     * @return
     */
    public native String getFFmpegConfig();
    public native int play(Object surface);
}



