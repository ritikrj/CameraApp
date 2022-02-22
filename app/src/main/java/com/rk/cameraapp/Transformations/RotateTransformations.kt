package com.rk.cameraapp.Transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class RotateTransformations() :
    BitmapTransformation() {
    private val rotate = 90f
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap,
        outWidth: Int, outHeight: Int
    ): Bitmap {
        return rotateBitmap(toTransform, rotate)
    }



    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


}