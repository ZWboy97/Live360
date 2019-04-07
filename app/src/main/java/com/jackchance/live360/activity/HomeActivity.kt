package com.jackchance.live360.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.CheckedTextView
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.jackchance.live360.R
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.util.toLiveActivity
import com.jackchance.live360.util.viewById
import com.jackchance.live360.util.visible
import com.jackchance.live360.videolist.data.LiveData
import com.jackchance.live360.videolist.fragement.HomeLiveListFragment
import com.jackchance.live360.vod.VodListFragment

class HomeActivity : BaseActivity(), View.OnClickListener, HomeLiveListFragment.OnListFragmentInteractionListener {

    private val homeLiveListButton: CheckedTextView by viewById(R.id.home_live_list_button)
    private val homeVodListButton: CheckedTextView by viewById(R.id.home_kind_button)
    private val homeMiscButton: CheckedTextView by viewById(R.id.home_misc_button)

    private val homeLiveListButtonLayout: LinearLayout by viewById(R.id.home_live_button_layout)
    private val homeVodListButtonLayout: LinearLayout by viewById(R.id.home_kind_button_layout)
    private val homeMiscButtonLayout: LinearLayout by viewById(R.id.home_misc_button_layout)

    private val homeLiveFrameLayout: FrameLayout by viewById(R.id.live_list_fragment)
    private val homeVodFrameLayout: FrameLayout by viewById(R.id.vod_list_fragment)
    private val homeMiscFrameLayout: FrameLayout by viewById(R.id.my_misc_fragment)

    private var currentSelected: Int = -1
    private val homeLiveListFragment = HomeLiveListFragment.newInstance(1)
    private val homeVodListFragment = VodListFragment.newInstance("", "")
    private val homeMiscFragment = HomeLiveListFragment.newInstance(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        homeLiveListButton.setOnClickListener(this)
        homeVodListButton.setOnClickListener(this)
        homeMiscButton.setOnClickListener(this)
        homeLiveListButtonLayout.setOnClickListener(this)
        homeVodListButtonLayout.setOnClickListener(this)
        homeMiscButtonLayout.setOnClickListener(this)

        setSelectedFragment(HOME_LIVE)
    }

    private fun setSelectedFragment(selectIndex: Int) {
        if (selectIndex == currentSelected) {
            return
        }
        unCheckedButton()
        currentSelected = selectIndex
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (selectIndex) {
            HOME_LIVE -> {
                homeLiveListButton.isChecked = true
                transaction.replace(R.id.live_list_fragment, homeLiveListFragment)
                transaction.commit()
            }
            HOME_VOD -> {
                homeVodListButton.isChecked = true
                transaction.replace(R.id.vod_list_fragment, homeVodListFragment)
                transaction.commit()
            }
            HOME_MISC -> {
                homeMiscButton.isChecked = true
                transaction.replace(R.id.live_list_fragment, homeMiscFragment)
                transaction.commit()
            }
        }
        updateFragementVisiable(selectIndex)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_live_list_button,
            R.id.home_live_button_layout -> {
                setSelectedFragment(HOME_LIVE)
            }
            R.id.home_kind_button_layout,
            R.id.home_kind_button -> {
                setSelectedFragment(HOME_VOD)
            }
            R.id.home_misc_button_layout,
            R.id.home_misc_button -> {
                setSelectedFragment(HOME_MISC)
            }
        }
    }

    private fun unCheckedButton() {
        homeLiveListButton.isChecked = false
        homeVodListButton.isChecked = false
        homeMiscButton.isChecked = false
    }

    fun updateFragementVisiable(index: Int) {
        invisiableAllFragment()
        when (index) {
            HOME_LIVE -> {
                homeLiveFrameLayout.visible = true
            }
            HOME_VOD -> {
                homeVodFrameLayout.visible = true
            }
            HOME_MISC -> {
                homeLiveFrameLayout.visible = true
            }
            else -> {
                homeLiveFrameLayout.visible = true
            }
        }
    }

    private fun invisiableAllFragment() {
        homeLiveFrameLayout.visible = false
        homeVodFrameLayout.visible = false
        homeMiscFrameLayout.visible = false
    }

    override fun onListFragmentInteraction(item: LiveRoom) {
        val intent = Intent(this, LiveDetailActivity::class.java)
        intent.putExtra("liveroom", item.writeJson())
        startActivity(intent)
    }

    companion object {
        private const val HOME_LIVE = 0
        private const val HOME_VOD = 1
        private const val HOME_MISC = 2
    }

}
