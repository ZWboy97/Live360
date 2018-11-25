package com.jackchance.live360.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.jackchance.live360.R
import com.jackchance.live360.util.toLiveActivity
import com.jackchance.live360.util.toMainActivity
import com.jackchance.live360.util.viewById
import com.jackchance.live360.videolist.data.LiveData
import com.jackchance.live360.videolist.fragement.VideoListFragment

class HomeActivity : BaseActivity(), View.OnClickListener ,VideoListFragment.OnListFragmentInteractionListener{

    private val myMiscButton: Button by viewById(R.id.my_misc_buttom)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        myMiscButton.setOnClickListener(this)

        setSelectedFragment()
    }

    private fun setSelectedFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment: Fragment = VideoListFragment.newInstance(1)
        transaction.replace(R.id.live_list_container, fragment)
        transaction.commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.my_misc_buttom -> {
                toMainActivity()
            }
        }
    }

    override fun onListFragmentInteraction(item: LiveData) {
        if (item.rtmpUrl.isEmpty()) {
            return
        }
        this.toLiveActivity(item.rtmpUrl, false)
    }
}
