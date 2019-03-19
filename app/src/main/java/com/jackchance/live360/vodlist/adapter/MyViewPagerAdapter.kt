package com.jackchance.live360.vodlist.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *  Created by lijiachang on 2018/12/1
 */
class MyViewPagerAdapter : FragmentPagerAdapter {

    constructor(fm: FragmentManager?) : super(fm)

    constructor(fm: FragmentManager?, titles: MutableList<String>, fragments: MutableList<Fragment>) : super(fm) {
        this.titles = titles
        this.fragments = fragments
    }

    private var fragments: MutableList<Fragment> = ArrayList()
    private var titles: MutableList<String> = ArrayList()

    override fun getItem(index: Int): Fragment {
        return fragments.get(index)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

}