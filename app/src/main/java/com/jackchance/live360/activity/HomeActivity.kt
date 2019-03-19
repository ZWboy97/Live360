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
import com.jackchance.live360.mylive.LivePublishFragment
import com.jackchance.live360.util.toLiveActivity
import com.jackchance.live360.util.viewById
import com.jackchance.live360.util.visible
import com.jackchance.live360.livelist.data.LiveData
import com.jackchance.live360.livelist.fragement.HomeLiveListFragment
import com.jackchance.live360.vodlist.VodListFragment

class HomeActivity : BaseActivity(), View.OnClickListener,
    HomeLiveListFragment.OnLiveListInteractionListener {

    private val homeLiveListButton: CheckedTextView by viewById(R.id.home_live_list_button)
    private val homeKindListButton: CheckedTextView by viewById(R.id.home_kind_button)
    private val homePublishButton: CheckedTextView by viewById(R.id.home_publish_button)
    private val homeSettingButton: CheckedTextView by viewById(R.id.home_setting_button)
    private val homeMiscButton: CheckedTextView by viewById(R.id.home_misc_button)
    private val homeLiveFragment: FrameLayout by viewById(R.id.live_list_fragment)
    private val homeKindFragment: FrameLayout by viewById(R.id.vod_list_fragment)
    private val homePublishFragment: FrameLayout by viewById(R.id.live_publish_fragment)
    private val homeSettingFragment: FrameLayout by viewById(R.id.home_setting_fragment)
    private val homeMiscFragment: FrameLayout by viewById(R.id.my_misc_fragment)

    private var currentSelected: Int = -1
    private val liveListFragment = HomeLiveListFragment.newInstance(1)
    private val vodListFrameLayout by lazy {
        VodListFragment.newInstance("", "")
    }
    private val livePublishFragment by lazy {
        LivePublishFragment.newInstance("", "")
    }

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
                transaction.replace(R.id.live_publish_fragment, livePublishFragment)
                transaction.commit()
            }
            HOME_SETTING -> {
                homeSettingButton.isChecked = true
                //todo
                fragment = HomeLiveListFragment.newInstance(1)
                transaction.replace(R.id.home_setting_fragment, fragment)
                transaction.commit()
            }
            else -> {
                homeLiveListButton.isChecked = true
                //todo
                fragment = HomeLiveListFragment.newInstance(1)
                transaction.replace(R.id.my_misc_fragment, fragment)
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
            R.id.home_misc_button -> {
                setSelectedFragment(HOEM_MISC)
            }
        }
    }

    fun unCheckedButton() {
        homeLiveListButton.isChecked = false
        homeKindListButton.isChecked = false
        homePublishButton.isChecked = false
        homeSettingButton.isChecked = false
        homeMiscButton.isChecked = false
    }

    fun updateFragementVisiable(index: Int) {
        homeLiveFragment.visible = false
        homeKindFragment.visible = false
        homePublishFragment.visible = false
        homeSettingFragment.visible = false
        homeMiscFragment.visible = false
        when (index) {
            HOME_LIVE -> {
                homeLiveFragment.visible = true
            }
            HOME_KIND -> {
                homeKindFragment.visible = true

            }
            HOME_PUBLISH -> {
                homePublishFragment.visible = true
            }
            HOME_SETTING -> {
                homeSettingFragment.visible = true
            }
            HOEM_MISC -> {
                homeMiscFragment.visible = true
            }
            else -> {
                homeLiveFragment.visible = true
            }
        }
    }

    /**
     * 处理直播列表item
     */
    override fun onLiveListItemClick(item: LiveData) {
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
        private const val HOEM_MISC = 4
    }

}
