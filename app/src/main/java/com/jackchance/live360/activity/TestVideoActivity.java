package com.jackchance.live360.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.jackchance.live360.util.Settings;
import com.jackchance.live360.R;
import com.jackchance.live360.model.Strings;

import java.io.IOException;

public class TestVideoActivity extends Activity {

    private static final String TAG = "LiveVideoActivity";

    public static final int UPDATE_SEEKBAR = 1;

    private SharedPreferences settings;
    private String chooseview;
    private String choosedecode;
    private Boolean mPause = false;
    private String path;


    private ImageView vr_start;
    private SeekBar vrplayer_seekbar;
    private TextView vrplayer_time;
    private Handler mHandler;


    boolean useHwCodec = false;

    private static final SparseArray<String> sDisplayMode = new SparseArray<>();
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    private static final SparseArray<String> sProjectionMode = new SparseArray<>();
    private MDVRLibrary mVRLibrary;

    static {
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_NORMAL,"NORMAL");
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_GLASS,"GLASS");

        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION,"MOTION");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH,"TOUCH");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH,"M & T");

        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_SPHERE,"SPHERE");
    }

    private static final int STATUS_IDLE = 0;
    private static final int STATUS_PREPARING = 1;
    private static final int STATUS_PREPARED = 2;
    private static final int STATUS_STARTED = 3;
    private static final int STATUS_PAUSED = 4;
    private static final int STATUS_STOPPED = 5;
    private int mStatus = STATUS_IDLE;

    protected KSYMediaPlayer mPlayer;
    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(IMediaPlayer mp) {
            cancelBusy();
            setVideoProgress(0);
            mStatus = STATUS_PREPARED;
            mPlayer.start();

        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            long duration = mPlayer.getDuration();
            long progress = duration * percent/100;
            vrplayer_seekbar.setSecondaryProgress((int)progress);
        }
    };
    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener(){

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG, "Buffering Start.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG, "Buffering End.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Toast.makeText(getApplicationContext(), "Audio Rendering Start", Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Toast.makeText(getApplicationContext(), "Video Rendering Start", Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                    // Player find a new stream(video or audio), and we could reload the video.
                    if(mPlayer != null)
                        mPlayer.reload(path, false);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                    Toast.makeText(getApplicationContext(), "Succeed to reload video.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Succeed to reload video.");
                    return false;
            }
            return false;
        }
    };

    private int mVideoProgress = 0;
    private SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b) {
                mVideoProgress = i;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPlayer.seekTo(mVideoProgress);
            setVideoProgress(mVideoProgress);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        settings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        choosedecode = settings.getString("choose_decode","undefind");
        chooseview = settings.getString("choose_view","undefind");

        // set content view
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case UPDATE_SEEKBAR:
                        setVideoProgress(0);
                        break;
                }
            }
        };

        if(chooseview.equals(Settings.USESUFACE)){
            setContentView(R.layout.activity_md_using_surface_view);
        }else{
            setContentView(R.layout.activity_md_using_texture_view);
        }

        useHwCodec = true;

        vr_start = (ImageView)findViewById(R.id.vr_start);
        vrplayer_seekbar = (SeekBar)findViewById(R.id.vrplayer_seekbar);
        vrplayer_time = (TextView)findViewById(R.id.vrplayer_time);

        vrplayer_seekbar.setOnSeekBarChangeListener(mSeekBarListener);
        vr_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause = !mPause;
                if(mPause) {
                    vr_start.setBackgroundResource(R.drawable.qyvideo_pause_btn);
                    mPlayer.pause();
                }else {
                    vr_start.setBackgroundResource(R.drawable.qyvideo_start_btn);
                    mPlayer.start();
                }
            }
        });


        // init VR Library
        mVRLibrary = createVRLibrary();
        mPlayer = new KSYMediaPlayer.Builder(getApplicationContext()).build();

        if (useHwCodec) {
            //硬解264&265
            mPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        }

        initMedia();

        SpinnerHelper.with(this)
                .setData(sDisplayMode)
                .setDefault(mVRLibrary.getDisplayMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchDisplayMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_display);

        SpinnerHelper.with(this)
                .setData(sInteractiveMode)
                .setDefault(mVRLibrary.getInteractiveMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchInteractiveMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_interactive);

        SpinnerHelper.with(this)
                .setData(sProjectionMode)
                .setDefault(mVRLibrary.getProjectionMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchProjectionMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_projection);
        mVRLibrary.switchInteractiveMode(TestVideoActivity.this,
                MDVRLibrary.INTERACTIVE_MODE_TOUCH); //初始模式采用VR模式
    }

    private void initMedia() {

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        mPlayer.setOnPreparedListener(mPreparedListener);
        mPlayer.setOnInfoListener(mInfoListener);
        mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                try{
                    String error = String.format("Play Error what=%d extra=%d",what,extra);
                    Toast.makeText(TestVideoActivity.this, error, Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    e.printStackTrace();
                }

                return true;
            }
        });

        mPlayer.setScreenOnWhilePlaying(true);
        mPlayer.setBufferTimeMax(5);
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mVRLibrary.handleTouchEvent(event) || super.onTouchEvent(event);
    }

    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mPlayer.setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(TestVideoActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .directorFactory(new DirectorFactory())
                .motionDelay(SensorManager.SENSOR_DELAY_GAME)
                .sensorCallback(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                })
                .pinchEnabled(true)
                .gesture(new MDVRLibrary.IGestureListener() {
                    @Override
                    public void onClick(MotionEvent e) {
                        Toast.makeText(TestVideoActivity.this, "onClick!", Toast.LENGTH_SHORT).show();
                    }
                })
                .build(R.id.gl_view);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
        mPlayer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);

        mPlayer.pause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mVRLibrary.onDestroy();
    }


    public void cancelBusy(){
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    private static class DirectorFactory extends MD360DirectorFactory {
        @Override
        public MD360Director createDirector(int index) {
            switch (index) {
                // setAngle: angle to rotate in degrees
                case 1:
                    return MD360Director.builder().setAngle(20).setEyeX(-2.0f).setLookX(-2.0f).build();
                default:
                    return MD360Director.builder().setAngle(20).build();
            }
        }
    }


    public int setVideoProgress(int currentProgress) {

        if(mPlayer == null)
            return -1;

        long time = currentProgress > 0 ? currentProgress : mPlayer.getCurrentPosition();
        long length = mPlayer.getDuration();

        // Update all view elements
        vrplayer_seekbar.setMax((int)length);
        vrplayer_seekbar.setProgress((int)time);

        if(time >= 0)
        {
            String progress = Strings.millisToString(time) + "/" + Strings.millisToString(length);
            vrplayer_time.setText(progress);
        }

        Message msg = new Message();
        msg.what = UPDATE_SEEKBAR;

        if(mHandler != null)
            mHandler.sendMessageDelayed(msg, 1000);
        return (int)time;
    }


}
