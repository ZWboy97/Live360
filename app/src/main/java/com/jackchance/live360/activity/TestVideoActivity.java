package com.jackchance.live360.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.jackchance.live360.R;
import com.jackchance.live360.model.Strings;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * VR播放器
 */
public class TestVideoActivity extends Activity {

    private static final String TAG = "VRLiveVideoActivity";

    public static final int UPDATE_SEEKBAR = 1;
    public static final int DISPLAY_CONTROL = 2;
    public static final int DISMISS_CONTROL = 3;
    public static final int WAIT_TIME_OUT = 4;
    public static final int DISPLAY_TIP = 5;

    public static final int VIDEO_QUALITY_HIGH = 1;
    public static final int VIDEO_QUALITY_NORMAL = 2;
    private boolean hasDisplayedTip = false;

    private Boolean mPause = false;
    private String path;
    private String pathHD;//标清地址
    private boolean isLiving = false;
    private String coverImageUrl;
    private String title;
    boolean useHwCodec = false;//硬件加速
    private static final SparseArray<String> sDisplayMode = new SparseArray<>();
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    private static final SparseArray<String> sProjectionMode = new SparseArray<>();
    private static final SparseArray<String> screenOrientionMode = new SparseArray<>();
    private static final SparseArray<String> videoQualityMode = new SparseArray<>();

    static {
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_NORMAL, "全景");
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_GLASS, "双眼");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION, "感应");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH, "滑动");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH, "滑动+感应");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_SPHERE, "球体");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_FULL, "展开");
        screenOrientionMode.put(Configuration.ORIENTATION_LANDSCAPE, "横屏(推荐)");
        screenOrientionMode.put(Configuration.ORIENTATION_PORTRAIT, "竖屏");
        videoQualityMode.put(VIDEO_QUALITY_HIGH, "高清");
        videoQualityMode.put(VIDEO_QUALITY_NORMAL, "普通");
    }

    private static final int STATUS_IDLE = 0;
    private static final int STATUS_PREPARING = 1;
    private static final int STATUS_PREPARED = 2;
    private static final int STATUS_STARTED = 3;
    private static final int STATUS_PAUSED = 4;
    private static final int STATUS_STOPPED = 5;
    private int mStatus = STATUS_IDLE;

    private LinearLayout controlContainer;
    private ImageView startButton;
    private SeekBar progressSeekbar;
    private TextView playTimeText;
    private TextView videoTitleText;
    private ImageView coverImageView;
    private TextView messageTextView;
    private ConstraintLayout waitingLayout;
    private ConstraintLayout tipsLayout;
    private TextView positiveButton;
    private TextView cancelButton;
    private Spinner displaySpinner;
    private Spinner interactiveSpinner;
    private Spinner projectionSpinner;
    private Spinner screenOrientionSpinner;
    private Spinner videoQualitySpinner;

    protected KSYMediaPlayer mPlayer;
    private MDVRLibrary mVRLibrary;
    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {

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
            long progress = duration * percent / 100;
            progressSeekbar.setSecondaryProgress((int) progress);
        }
    };
    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {

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
                    //Toast.makeText(getApplicationContext(), "Audio Rendering Start", Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    mHandler.sendEmptyMessage(DISPLAY_CONTROL);
                    waitingLayout.setVisibility(View.GONE);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                    if (mPlayer != null)
                        mPlayer.reload(path, false);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                    cancelBusy();
                    Log.d(TAG, "Succeed to reload video.");
                    return false;
            }
            return false;
        }
    };

    private int mVideoProgress = 0;
    private SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_SEEKBAR:
                    setVideoProgress(0);
                    break;
                case DISPLAY_CONTROL:
                    setControlVisible(true);
                    mHandler.removeMessages(DISMISS_CONTROL);
                    mHandler.sendEmptyMessageDelayed(DISMISS_CONTROL, 3000);
                    mHandler.sendEmptyMessageDelayed(DISPLAY_TIP, 3000);
                    break;
                case DISMISS_CONTROL:
                    setControlVisible(false);
                    break;
                case WAIT_TIME_OUT:
                    messageTextView.setText("直播还未开始");
                    break;
                case DISPLAY_TIP:
                    if (!hasDisplayedTip) {
                        hasDisplayedTip = true;
                        int orientation = TestVideoActivity.this.getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            tipsLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_md_using_surface_view);
        initData();
        initView();
        initVRMediaPlayer();
        initControlView();
    }

    private void initData() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        pathHD = intent.getStringExtra("path_hd");
        isLiving = intent.getBooleanExtra("is_living", false);
        coverImageUrl = intent.getStringExtra("cover_image_url");
        title = intent.getStringExtra("title");
    }

    private void initView() {
        controlContainer = findViewById(R.id.control_container);
        startButton = findViewById(R.id.vr_start);
        progressSeekbar = findViewById(R.id.vrplayer_seekbar);
        playTimeText = findViewById(R.id.vrplayer_time);
        coverImageView = findViewById(R.id.cover_image);
        videoTitleText = findViewById(R.id.video_title_text);
        messageTextView = findViewById(R.id.message_text);
        waitingLayout = findViewById(R.id.waiting_layout);
        tipsLayout = findViewById(R.id.tip_container);
        positiveButton = findViewById(R.id.positive_button);
        cancelButton = findViewById(R.id.cancel_button);
        setControlVisible(false);
        if (isLiving) {
            waitingLayout.setVisibility(View.VISIBLE);
            videoTitleText.setText(title);
            if (coverImageUrl == null || coverImageUrl.isEmpty()) {
                return;
            }
            Picasso.get()
                    .load(coverImageUrl)
                    .placeholder(R.drawable.ic_empty_zhihu)
                    .error(R.drawable.ic_empty_zhihu)
                    .into(coverImageView);
        } else {
            videoTitleText.setText("");
            messageTextView.setText("加载中");
        }
        mHandler.sendEmptyMessageDelayed(WAIT_TIME_OUT, 5000);
    }

    private void initControlView() {
        progressSeekbar.setOnSeekBarChangeListener(mSeekBarListener);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause = !mPause;
                if (mPause) {
                    startButton.setBackgroundResource(R.drawable.qyvideo_pause_btn);
                    mPlayer.pause();
                } else {
                    startButton.setBackgroundResource(R.drawable.qyvideo_start_btn);
                    mPlayer.start();
                }
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsLayout.setVisibility(View.GONE);
                setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE);
                screenOrientionSpinner.setSelection(1);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsLayout.setVisibility(View.GONE);
            }
        });
        displaySpinner = SpinnerHelper.with(this)
                .setData(sDisplayMode)
                .setDefault(mVRLibrary.getDisplayMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        if (key == MDVRLibrary.DISPLAY_MODE_GLASS) {
                            setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE);
                            screenOrientionSpinner.setSelection(1);
                        }
                        mVRLibrary.switchDisplayMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_display);

        interactiveSpinner = SpinnerHelper.with(this)
                .setData(sInteractiveMode)
                .setDefault(mVRLibrary.getInteractiveMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        if (key == MDVRLibrary.INTERACTIVE_MODE_MOTION ||
                                key == MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH) {
                            setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE);
                            screenOrientionSpinner.setSelection(1);
                        }
                        mVRLibrary.switchInteractiveMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_interactive);

        projectionSpinner = SpinnerHelper.with(this)
                .setData(sProjectionMode)
                .setDefault(mVRLibrary.getProjectionMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchProjectionMode(TestVideoActivity.this, key);
                    }
                })
                .init(R.id.spinner_projection);

        screenOrientionSpinner = SpinnerHelper.with(this)
                .setData(screenOrientionMode)
                .setDefault(Configuration.ORIENTATION_PORTRAIT)
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        if (key == Configuration.ORIENTATION_PORTRAIT) {
                            if (mVRLibrary.getInteractiveMode() != MDVRLibrary.INTERACTIVE_MODE_TOUCH
                                    || mVRLibrary.getDisplayMode() == MDVRLibrary.DISPLAY_MODE_GLASS) {
                                Toast.makeText(getApplicationContext(), "滑动屏幕切换视角", Toast.LENGTH_SHORT).show();
                                mVRLibrary.switchInteractiveMode(TestVideoActivity.this, MDVRLibrary.INTERACTIVE_MODE_TOUCH);
                            }
                            setScreenOrientation(Configuration.ORIENTATION_PORTRAIT);
                        } else {
                            setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE);
                        }
                    }
                })
                .init(R.id.spinner_orientation);

        videoQualitySpinner = SpinnerHelper.with(this)
                .setData(videoQualityMode)
                .setDefault(VIDEO_QUALITY_HIGH)
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        if (key == VIDEO_QUALITY_HIGH) {
                            if (mPlayer != null)
                                mPlayer.reload(path, false);
                        } else {
                            if (mPlayer != null)
                                mPlayer.reload(path, false);
                        }
                    }
                })
                .init(R.id.spinner_quality);
    }

    private void setControlVisible(boolean visible) {
        if (visible) {
            controlContainer.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.VISIBLE);
            progressSeekbar.setVisibility(View.VISIBLE);
            playTimeText.setVisibility(View.VISIBLE);
        } else {
            controlContainer.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            progressSeekbar.setVisibility(View.GONE);
            playTimeText.setVisibility(View.GONE);
        }

    }

    private void setScreenOrientation(int mode) {
        int orientation = TestVideoActivity.this.getResources().getConfiguration().orientation;
        switch (mode) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
            break;
            case Configuration.ORIENTATION_PORTRAIT: {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
            break;
            default:
                break;
        }
    }

    private void initVRMediaPlayer() {
        mVRLibrary = createVRLibrary();
        mPlayer = new KSYMediaPlayer.Builder(getApplicationContext()).build();
        useHwCodec = true; //硬件编解码
        if (useHwCodec) {
            mPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        }
        mPlayer.setOnPreparedListener(mPreparedListener);
        mPlayer.setOnInfoListener(mInfoListener);
        mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                try {
                    if (waitingLayout.getVisibility() == View.VISIBLE) {
                        if (isLiving) {
                            mHandler.removeMessages(WAIT_TIME_OUT);
                            mHandler.sendEmptyMessage(WAIT_TIME_OUT);
                        } else {
                            messageTextView.setText("加载失败");
                        }
                    } else {
                        String error = String.format("播放失败：what=%d extra=%d", what, extra);
                        messageTextView.setText(error);
                    }
                } catch (Exception e) {
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
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_TOUCH)
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
                        if (waitingLayout.getVisibility() == View.VISIBLE) {
                            return;
                        }
                        setControlVisible(true);
                        mHandler.removeMessages(DISMISS_CONTROL);
                        mHandler.sendEmptyMessageDelayed(DISMISS_CONTROL, 3000);
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


    public void cancelBusy() {
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

        if (mPlayer == null)
            return -1;

        long time = currentProgress > 0 ? currentProgress : mPlayer.getCurrentPosition();
        long length = mPlayer.getDuration();

        // Update all view elements
        progressSeekbar.setMax((int) length);
        progressSeekbar.setProgress((int) time);

        if (time >= 0) {
            String progress = "";
            if (isLiving) {
                progress = "直播中 " + Strings.millisToString(time);
            } else {
                progress = Strings.millisToString(time) + "/" + Strings.millisToString(length);
            }
            playTimeText.setText(progress);
        }

        Message msg = new Message();
        msg.what = UPDATE_SEEKBAR;

        if (mHandler != null)
            mHandler.sendMessageDelayed(msg, 1000);
        return (int) time;
    }


}
