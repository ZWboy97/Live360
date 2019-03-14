package com.jackchance.live360.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.CheckedTextView
import android.widget.FrameLayout
import com.jackchance.live360.R
import com.jackchance.live360.fragment.LivePublishFragment
import com.jackchance.live360.util.toLiveActivity
import com.jackchance.live360.util.viewById
import com.jackchance.live360.util.visible
import com.jackchance.live360.videolist.data.LiveData
import com.jackchance.live360.videolist.fragement.HomeLiveListFragment
import com.jackchance.live360.vod.VodListFragment

class HomeActivity : BaseActivity(), View.OnClickListener, HomeLiveListFragment.OnListFragmentInteractionListener {

    private val homeLiveListButton: CheckedTextView by viewById(R.id.home_live_list_button)
    private val homeKindListButton: CheckedTextView by viewById(R.id.home_kind_button)
    private val homePublishButton: CheckedTextView by viewById(R.id.home_publish_button)
    private val homeSettingButton: CheckedTextView by viewById(R.id.home_setting_button)
    private val homeMiscButton: CheckedTextView by viewById(R.id.home_misc_button)
    private val homeLiveFragment: FrameLayout by viewById(R.id.live_list_fragment)
    private val homeKindFragment: FrameLayout by viewById(R.id.vod_list_fragment)
    private val homeSettingFragment: FrameLayout by viewById(R.id.my_misc_fragment)

    private var currentSelected: Int = -1
    private val liveListFragment = HomeLiveListFragment.newInstance(1)
    private val vodListFrameLayout = VodListFragment.newInstance("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        homeLiveListButton.setOnClickListener(this)
        homeKindListButton.setOnClickListener(this)
        homePublishButton.setOnClickListener(this)
        homeSettingButton.setOnClickListener(this)

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
        val fragment: Fragment
        when (selectIndex) {
            HOME_LIVE -> {
                homeLiveListButton.isChecked = true
                transaction.replace(R.id.live_list_fragment, liveListFragment)
                transaction.commit()
            }
            HOME_KIND -> {
                homeKindListButton.isChecked = true
                transaction.replace(R.id.vod_list_fragment, vodListFrameLayout)
                transaction.commit()
            }
            HOME_PUBLISH -> {
                homePublishButton.isChecked = true
                fragment = LivePublishFragment.newInstance("","")
                transaction.replace(R.id.live_publish_fragment, fragment)
                transaction.commit()
            }
            HOME_SETTING -> {
                homeSettingButton.isChecked = true
                fragment = HomeLiveListFragment.newInstance(1)
                transaction.replace(R.id.my_misc_fragment, fragment)
                transaction.commit()
            }
            else -> {
                homeLiveListButton.isChecked = true
                fragment = HomeLiveListFragment.newInstance(1)
                transaction.replace(R.id.live_list_fragment, fragment)
                transaction.commit()
            }
        }
        updateFragementVisiable(selectIndex)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_live_list_button -> {
                setSelectedFragment(HOME_LIVE)
            }
            R.id.home_kind_button -> {
                setSelectedFragment(HOME_KIND)
            }
            R.id.home_publish_button -> {
                setSelectedFragment(HOME_PUBLISH)
            }
            R.id.home_setting_button -> {
                setSelectedFragment(HOME_SETTING)
            }
        }
    }

    fun unCheckedButton() {
        homeLiveListButton.isChecked = false
        homeKindListButton.isChecked = false
        homePublishButton.isChecked = false
        homeSettingButton.isChecked = false
    }

    fun updateFragementVisiable(index: Int) {
        when (index) {
            HOME_LIVE -> {
                homeLiveFragment.visible = true
                homeKindFragment.visible = false
                homeSettingFragment.visible = false
            }
            HOME_KIND -> {
                homeLiveFragment.visible = false
                homeKindFragment.visible = true
                homeSettingFragment.visible = false
            }
            HOME_PUBLISH -> {
                homeLiveFragment.visible = true
                homeKindFragment.visible = false
                homeSettingFragment.visible = false
            }
            HOME_SETTING -> {
                homeLiveFragment.visible = true
                homeKindFragment.visible = false
                homeSettingFragment.visible = false
            }
            else -> {
                homeLiveFragment.visible = true
                homeKindFragment.visible = false
                homeSettingFragment.visible = false
            }
        }
    }

    override fun onListFragmentInteraction(item: LiveData) {
        if (item.rtmpUrl.isEmpty()) {
            return
        }
        this.toLiveActivity(item.rtmpUrl, item.isVr)
    }

    companion object {
        private const val HOME_LIVE = 0
        private const val HOME_KIND = 1
        private const val HOME_PUBLISH = 2
        private const val HOME_SETTING = 3
    }

}
