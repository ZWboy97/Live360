package com.jackchance.live360.vod.fragement

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aspsine.irecyclerview.IRecyclerView
import com.jackchance.live360.R
import com.jackchance.live360.util.toLiveActivity
import com.jackchance.live360.vod.adapter.VodVideoRecyclerViewAdapter
import com.jackchance.live360.vod.data.VodData
import com.jackchance.live360.vod.data.VodListBuilder
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Created by lijiachang on 2018/11/20
 */
class VodVideoListFragment : Fragment() {

    // Customize parameters
    private var columnCount = 1

    private lateinit var iRecyclerView: IRecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout

    private var liveDataList: MutableList<VodData> = ArrayList()
    private lateinit var myAdapter: VodVideoRecyclerViewAdapter
    private val delegate: VodVideoRecyclerViewAdapter.VodVideoRecyclerViewAdapterDelegate

    init {
        delegate = object : VodVideoRecyclerViewAdapter.VodVideoRecyclerViewAdapterDelegate {
            override fun onClick(position: Int, item: VodData) {
                activity?.toLiveActivity(item.resourceUrl, item.isVr)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragement_video_list_for_tab, container, false)

        iRecyclerView = view.findViewById(R.id.list)
        refreshLayout = view.findViewById(R.id.refresh_layout)
        refreshLayout.setOnRefreshListener {
            liveDataList.clear()
            loadLiveData(0)
        }
        refreshLayout.setOnLoadmoreListener {
            loadLiveData(0)
        }

        // Set the adapter
        with(iRecyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            myAdapter = VodVideoRecyclerViewAdapter(liveDataList, delegate)
            adapter = myAdapter
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liveDataList.clear()
        loadLiveData(0)
    }

    private fun loadLiveData(index: Int) {
        val appendItem: MutableList<VodData>?
        appendItem = VodListBuilder.getVodList {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadmore()
        }
        liveDataList.addAll(appendItem)
        myAdapter.notifyDataSetChanged()
    }

    companion object {

        const val ARG_COLUMN_COUNT = "video_column-count"

        @JvmStatic
        fun newInstance(columnCount: Int = 1) =
                VodVideoListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
