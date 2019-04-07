package com.jackchance.live360.videolist.fragement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.aspsine.irecyclerview.IRecyclerView
import com.jackchance.live360.Api.DataApi
import com.jackchance.live360.R
import com.jackchance.live360.activity.LivePublishActivity
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.videolist.data.VideoListBuilder
import com.jackchance.live360.videolist.ui.MyVideoRecyclerViewAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by lijiachang on 2018/11/20
 */
class HomeLiveListFragment : Fragment() {

    // Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var iRecyclerView: IRecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var livePublishButton: TextView

    private var liveDataList: MutableList<LiveRoom> = ArrayList()
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
        livePublishButton = view.findViewById(R.id.live_publish_button)
        refreshLayout.setOnRefreshListener {
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
            myAdapter = MyVideoRecyclerViewAdapter(liveDataList, listener)
            adapter = myAdapter
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadLiveData(0)
        livePublishButton.setOnClickListener {
            val intent = Intent(context, LivePublishActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadLiveData(index: Int) {
        liveDataList.clear()
        DataApi.getLiveRooms().enqueue(object : Callback<List<LiveRoom>> {
            override fun onFailure(call: Call<List<LiveRoom>>, t: Throwable) {
                Toast.makeText(context, "更新直播列表失败，请稍后重试！", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<LiveRoom>>, response: Response<List<LiveRoom>>) {
                response.body()?.let {
                    liveDataList.addAll(it)
                }
                myAdapter.notifyDataSetChanged()
            }
        })
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

        fun onListFragmentInteraction(item: LiveRoom)

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
