package com.aligit.base.utils;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;

import com.aligit.base.common.AppContext;
import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音频播放工具
 */
public class MediaPlayerUtil {

    boolean playPrepare = false;
    private MediaPlayer player;
    private ArrayList<PlayProgress> progressesListener;
    int max = 0;

    static MediaPlayerUtil mediaPlayerUtil;
    public MediaPlayerUtil() {
        player = new MediaPlayer();
        progressesListener = new ArrayList<>();
    }

    //获取实例
    public static MediaPlayerUtil getInstance(){
        if (null == mediaPlayerUtil){
            synchronized (MediaPlayerUtil.class){
                if (null == mediaPlayerUtil){
                    mediaPlayerUtil = new MediaPlayerUtil();
                }
            }
        }
        return mediaPlayerUtil;
    }

    //设置播放源
    public void setDataSource(String path,boolean autoPlay){
        if (TextUtils.isEmpty(path))return;
        try {
            stop();
            playPrepare = false;
            if (path.startsWith("assets:")) {
                AssetFileDescriptor fileDescriptor = AppContext.INSTANCE.getAssets().openFd(path.split(":")[1]);
                player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                        fileDescriptor.getStartOffset());
            }else {
                player.setDataSource(path);
            }
            player.prepareAsync();
            player.setOnPreparedListener(mp -> {
                playPrepare = true;
                max = player.getDuration();
                getProgress();
                if (autoPlay)pausePlay();
                for (PlayProgress progress:progressesListener){
                    if (progress != null)progress.onPrepared(max);
                }
            });
            player.setOnErrorListener((mp, what, extra) -> {
                for (PlayProgress progress:progressesListener){
                    if (progress != null)progress.onError();
                }
                return false;
            });
            player.setOnCompletionListener(mp -> {
                for (PlayProgress progress:progressesListener){
                    if (progress != null)progress.onCompletion();
                }
            });
        }catch (Exception e){
            XLog.e("音频播放失败"+e.getLocalizedMessage()+"：路径："+path);
        }
    }

    //播放状态
    public boolean isPlayIng(){
        return player.isPlaying();
    }

    //播放、暂停
    public void pausePlay(){
        if (player == null)return;
        if (!playPrepare)return;
        if (player.isPlaying()) {
            player.pause();
        }else {
            player.start();
        }
    }

    public MediaPlayer getPlayer(){
        return player;
    }

    //跳转
    public void seekTo(int progress){
        if (player == null)return;
        player.seekTo(progress);
    }

    //停止播放
    public void stop(){
        if (player != null) {
            player.stop();
            player.reset();
        }
        if (timer != null)
            timer.cancel();
    }

    //获取音频长度
    public int getMax(){
        return max;
    }

    //增加播放监听
    public void addProgressListener(PlayProgress playProgress){
        if (progressesListener.contains(playProgress))return;
        progressesListener.add(playProgress);
    }

    //移除播放监听
    public void removeProgresListener(PlayProgress playProgress){
        progressesListener.remove(playProgress);
    }

    //变速
    public void changeplayerSpeed(float speed) {
        // this checks on API 23 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (player.isPlaying()) {
                player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            } else {
                player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
                player.pause();
            }
        }
    }

    Timer timer;
    private void getProgress() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int p = player.getCurrentPosition();
                for (PlayProgress progress:progressesListener){
                    if (progress != null)progress.onProgress(p);
                }
            }
        }, 0, 1000);
    }

    public interface PlayProgress{
        void onError();
        void onPrepared(int max);
        void onProgress(int progress);
        void onCompletion();
    }


}
