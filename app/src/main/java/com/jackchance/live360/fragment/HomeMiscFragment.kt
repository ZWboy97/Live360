package com.jackchance.live360.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

import com.jackchance.live360.R
import com.jackchance.live360.activity.APPInfoActivity
import com.jackchance.live360.activity.CaptureActivity
import com.jackchance.live360.activity.LivePublishActivity

private const val ARG_PARAM1 = "param1"

class HomeMiscFragment : Fragment() {
    private var param1: String? = null

    private lateinit var liveManagerButton: LinearLayout
    private lateinit var appInfoButton: LinearLayout
    private lateinit var displaySettingButton: LinearLayout
    private lateinit var publishLiveButton: LinearLayout
    private lateinit var qrScanButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_misc, container, false)
        liveManagerButton = view.findViewById(R.id.bt_live_list_info)
        appInfoButton = view.findViewById(R.id.bt_container_info)
        displaySettingButton = view.findViewById(R.id.bt_container_setting)
        publishLiveButton = view.findViewById(R.id.bt_container_publish)
        qrScanButton = view.findViewById(R.id.bt_qr_scan)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        publishLiveButton.setOnClickListener {
            val intent = Intent(activity, LivePublishActivity::class.java)
            context?.startActivity(intent)
        }
        liveManagerButton.setOnClickListener {
            Toast.makeText(context, "开发中,敬请期待", Toast.LENGTH_SHORT).show()
        }
        appInfoButton.setOnClickListener {
            val intent = Intent(context,APPInfoActivity::class.java)
            startActivity(intent)
        }
        displaySettingButton.setOnClickListener {
            Toast.makeText(context, "开发中,敬请期待", Toast.LENGTH_SHORT).show()
        }
        qrScanButton.setOnClickListener {
            val intent = Intent(activity, CaptureActivity::class.java)
            context?.startActivity(intent)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                HomeMiscFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
