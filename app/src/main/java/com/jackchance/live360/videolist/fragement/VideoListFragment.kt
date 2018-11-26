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

/**
 * Created by lijiachang on 2018/11/20
 */
class VideoListFragment : Fragment() {

    // Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_video_list, container, false)

        val iRecyclerView: IRecyclerView = view.findViewById(R.id.list)
        iRecyclerView.setRefreshEnabled(true)
        iRecyclerView.setRecyclerListener {
            VideoListBuilder.getVideoList{
                iRecyclerView.setRefreshing(false)
            }
        }

        // Set the adapter
        if (view is IRecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyVideoRecyclerViewAdapter(VideoListBuilder.getVideoList().toList(), listener)
            }
        }
        return view
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
                VideoListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
