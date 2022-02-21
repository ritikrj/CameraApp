package com.rk.cameraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.rk.cameraapp.databinding.ActivityMainBinding
import android.Manifest
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var  imageCapture:ImageCapture
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED){
           startCamera()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.CAMERA), 1 )
        }

        binding.button.setOnClickListener {
            takePhoto()
        }

    }

    fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
             imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(this,cameraSelector,preview, imageCapture )
            preview.setSurfaceProvider(binding.cameraView.createSurfaceProvider(camera?.cameraInfo))
        },ContextCompat.getMainExecutor(this))
    }

    fun takePhoto(){
        val photoFile = File(externalMediaDirs.firstOrNull(),"Kapp"+ System.currentTimeMillis()+".jpg")
        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(output, ContextCompat.getMainExecutor(this),  object :
            ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(applicationContext,"Image saved",Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: ImageCaptureException) {

            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startCamera()
    }

}