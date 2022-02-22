package com.rk.cameraapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.bumptech.glide.Glide
import com.rk.cameraapp.Transformations.CropTransformation
import com.rk.cameraapp.Transformations.RotateTransformations
import com.rk.cameraapp.databinding.ActivityEditorBinding
import java.io.File
import java.io.FileOutputStream


class EditorActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditorBinding
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
         uri = intent.extras?.get("URI") as Uri
        Glide.with(applicationContext).load(uri).into(binding.image)
        binding.rotate.setOnClickListener {
            rotate()
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.crop.setOnClickListener {
            cropImage()
        }
        binding.save.setOnClickListener {
            saveImage()
        }

    }
    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage()
            } else {
                Toast.makeText(
                    this,
                    "Please provide the External storage permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun saveImage(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED){
        var bitmap = (binding.image.drawable as (BitmapDrawable)).bitmap
        val photoFile = File(externalMediaDirs.firstOrNull(),"Kapp"+ System.currentTimeMillis()+".jpg")
        val out =  FileOutputStream(photoFile)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,out)
        out.flush()
        out.close()
        }
        else{
            askPermission()
        }

    }




    fun cropImage(){
        var bitmap = (binding.image.drawable as (BitmapDrawable)).bitmap

        Glide.with(applicationContext).load(bitmap).transform(CropTransformation()).into(binding.image)
    }


    fun rotate(){
        var bitmap = (binding.image.drawable as (BitmapDrawable)).bitmap
        bitmap = RotateTransformations().rotateBitmap(bitmap,90f)
        Glide.with(applicationContext).load(bitmap).into(
            binding.image
        )
    }
}