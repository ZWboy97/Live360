package com.jackchance.live360.videolist.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jackchance.live360.R
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.util.TimeUtil
import com.jackchance.live360.util.loadUrl
import com.jackchance.live360.util.visible
import com.jackchance.live360.videolist.fragement.HomeLiveListFragment.OnListFragmentInteractionListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_video_item.view.*

/**
 * Created by lijiachang on 2018/11/24
 */

class MyVideoRecyclerViewAdapter(
        private val liveDataList: List<LiveRoom>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as LiveRoom
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = liveDataList[position]
        holder.liveTitle.text = item.roomName
        holder.publishMessage.text = item.roomDesc
        holder.liveImage.loadUrl(item.roomCoverImageUrl)
//        holder.publisherName.text = "null"
        holder.publishTime.text = TimeUtil.prettyTime(item.createTime)
//        item.publisher?.avtarUrl?.let {
//            holder.publisherAvtar.loadUrl(it)
//        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = liveDataList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val liveTitle: TextView = mView.text_live_name
        val liveDescription: TextView = mView.text_live_desc
        val liveImage: ImageView = mView.live_bg_image
        val publisherAvtar: CircleImageView = mView.avtar_image
        val publisherName: TextView = mView.publisher_name_text
        val publishTime: TextView = mView.publish_time_text
        val publishMessage: TextView = mView.publisher_message
    }
}
