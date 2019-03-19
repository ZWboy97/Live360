package com.jackchance.live360.vodlist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jackchance.live360.R
import com.jackchance.live360.util.loadUrl
import com.jackchance.live360.vodlist.data.VodData
import kotlinx.android.synthetic.main.fragement_vod_video_adapter_view.view.*

/**
 * Created by lijiachang on 2018/11/24
 */

class VodVideoRecyclerViewAdapter(
        private val vodDataList: List<VodData>,
        private val delegate: VodVideoRecyclerViewAdapterDelegate)
    : RecyclerView.Adapter<VodVideoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    private var position = 0

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as VodData
            delegate.onClick(position,item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragement_vod_video_adapter_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position = position
        val item = vodDataList[position]
        holder.vodVideoTitle.text = item.name
        holder.vodVideoImage.loadUrl(item.imageUrl)
        holder.vodVideoDesctiption.text = item.description
        holder.vodVidoeoKind.text = item.kindTag
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    interface VodVideoRecyclerViewAdapterDelegate{
        fun onClick(position: Int, item: VodData)
    }

    override fun getItemCount(): Int = vodDataList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val vodVideoTitle: TextView = mView.text_vod_video_name
        val vodVideoImage: ImageView = mView.vod_video_bg_image
        val vodVidoeoKind: TextView = mView.vod_video_kind
        val vodVideoDesctiption: TextView = mView.vod_video_desc
    }
}
