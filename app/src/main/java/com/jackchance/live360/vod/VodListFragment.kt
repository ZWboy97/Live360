package com.jackchance.live360.vod

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jackchance.live360.R
import com.jackchance.live360.activity.LocalFragment
import com.jackchance.live360.videolist.fragement.HomeLiveListFragment
import com.jackchance.live360.vod.adapter.MyViewPagerAdapter
import com.jackchance.live360.vod.fragement.VodVideoListFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VodListFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private val titles = arrayOf("户外", "影视", "动画", "生活", "创意")
    private val fragments = arrayListOf(
            VodVideoListFragment.newInstance(2),
            VodVideoListFragment.newInstance(2),
            VodVideoListFragment.newInstance(2),
            VodVideoListFragment.newInstance(2),
            VodVideoListFragment.newInstance(2))

    private lateinit var viewPagerAdapter: MyViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vod_list, container, false)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        initView()
        return view
    }

    private fun initView() {
        tabLayout.tabMode = TabLayout.MODE_FIXED
        for (title in titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title).setIcon(R.drawable.icon_home_360_selseted))
        }
        tabLayout.addOnTabSelectedListener(this)
        viewPagerAdapter = MyViewPagerAdapter(fragmentManager, titles.toMutableList(), fragments.toMutableList())
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 5
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        viewPager.currentItem = p0?.position ?: 0
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                VodListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
