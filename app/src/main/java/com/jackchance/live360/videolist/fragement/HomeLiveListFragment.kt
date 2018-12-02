package com.jackchance.live360.videolist.fragement

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
import com.jackchance.live360.videolist.data.LiveData
import com.jackchance.live360.videolist.data.VideoListBuilder
import com.jackchance.live360.videolist.ui.MyVideoRecyclerViewAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Created by lijiachang on 2018/11/20
 */
class HomeLiveListFragment : Fragment() {

    // Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var iRecyclerView: IRecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout

    private var liveDataList: MutableList<LiveData> = ArrayList()
    private lateinit var myAdapter: MyVideoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_video_list, container, false)

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
            myAdapter = MyVideoRecyclerViewAdapter(liveDataList,listener)
            adapter = myAdapter
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadLiveData(0)
    }

    private fun loadLiveData(index: Int){
        val appendItem: MutableList<LiveData>?
        appendItem = VideoListBuilder.getVideoList{
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadmore()
        }
        liveDataList.addAll(appendItem)
        myAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " 没有实现OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {

        fun onListFragmentInteraction(item: LiveData)

    }

    companion object {

        const val ARG_COLUMN_COUNT = "video_column-count"

        @JvmStatic
        fun newInstance(columnCount: Int = 1) =
                HomeLiveListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
