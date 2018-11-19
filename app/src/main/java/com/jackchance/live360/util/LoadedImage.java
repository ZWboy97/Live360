package com.jackchance.live360.util;

import android.graphics.Bitmap;

public class LoadedImage {
    Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
