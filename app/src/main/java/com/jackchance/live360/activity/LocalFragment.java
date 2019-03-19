package com.jackchance.live360.activity;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jackchance.live360.R;
import com.jackchance.live360.util.Settings;
import com.jackchance.live360.livelist.data.Video;
import com.jackchance.live360.model.GetList;
import java.util.ArrayList;

public class LocalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private ListView listView;
    private JieVideoListViewAdapter madapter;
    private SharedPreferences settings;
    private String choosevr;

    private ArrayList<Video> showlistVideos;
    private SwipeRefreshLayout swipeLayout;
    public static Handler mHandler;
    private GetList getList;
    private boolean updateok = false;

    public LocalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        choosevr = settings.getString("choose_vr","信息为空");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        settings = getActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        choosevr = settings.getString("choose_vr","信息为空");

        showlistVideos = new ArrayList<Video>();
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        listView = (ListView)view.findViewById(R.id.list_local_frag);
        madapter = new JieVideoListViewAdapter(getActivity(),showlistVideos);
        swipeLayout.setOnRefreshListener(this);
        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video v = showlistVideos.get(position);
                Log.e("adasdasd",v.getPath());

                if (choosevr.equals(Settings.VRON)){
                    Intent intent  = new Intent(getActivity(),TestVideoActivity.class);
                    intent.putExtra("path",v.getPath());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
                    intent.putExtra("path", v.getPath());
                    startActivity(intent);
                }

            }
        });

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getList.startThread(showlistVideos, Environment.getExternalStorageDirectory());

        return view;
    }

    public ArrayList<Video> getList(Context context) {
        ArrayList<Video> sysVideoList = new ArrayList<>();
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};
        // 视频其他信息的查询条件
        String[] mediaColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media
                        .EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor == null) {
            return sysVideoList;
        }
        if (cursor.moveToFirst()) {
            do {
                Video info = new Video();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = context.getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setPath(thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media
                        .DATA)));
                info.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video
                        .Media.DURATION)));
                sysVideoList.add(info);
            } while (cursor.moveToNext());
        }
        return sysVideoList;
    }

    public void updatelist(){
        madapter = new JieVideoListViewAdapter(getActivity(),showlistVideos);
        listView.setAdapter(madapter);
    }


    @Override
    public void onRefresh() {
//        Message msg = new Message();
//        msg.what = UPDATE;
//        mHandler.sendMessageDelayed(msg,3000);
        showlistVideos.addAll(getList(getActivity().getApplicationContext()));
        madapter = new JieVideoListViewAdapter(getActivity(),showlistVideos);
        listView.setAdapter(madapter);
    }
}

