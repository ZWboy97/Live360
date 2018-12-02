package com.jackchance.live360.live

import android.os.Bundle
import android.view.View
import com.jackchance.live360.R
import com.jackchance.live360.activity.BaseActivity
/**
 *  Created by lijiachang on 2018/12/1
 */
class VRLiveActivity : BaseActivity(), View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)

    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }
}