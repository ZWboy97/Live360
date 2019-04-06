package com.jackchance.live360.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.zhihu.matisse.engine.impl.PicassoEngine

class MyImageEngine() : PicassoEngine() {

    override fun loadAnimatedGifThumbnail(context: Context?, resize: Int, placeholder: Drawable?, imageView: ImageView?, uri: Uri?) {
        super.loadAnimatedGifThumbnail(context, resize, placeholder, imageView, uri)

    }

    override fun loadImage(context: Context?, resizeX: Int, resizeY: Int, imageView: ImageView?, uri: Uri?) {
        Picasso.get().load(uri).resize(resizeX, resizeY).priority(Picasso.Priority.HIGH)
                .centerInside().into(imageView)
    }

    override fun loadAnimatedGifImage(context: Context?, resizeX: Int, resizeY: Int, imageView: ImageView?, uri: Uri?) {
        super.loadAnimatedGifImage(context, resizeX, resizeY, imageView, uri)
    }

    override fun supportAnimatedGif(): Boolean {
        return super.supportAnimatedGif()
    }

    override fun loadThumbnail(context: Context?, resize: Int, placeholder: Drawable?, imageView: ImageView?, uri: Uri?) {
        Picasso.get().load(uri)
                .resize(resize, resize)
                .centerCrop()
                .into(imageView)
    }
}