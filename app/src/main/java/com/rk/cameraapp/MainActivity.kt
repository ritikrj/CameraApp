package com.rk.cameraapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.bumptech.glide.Glide
import com.rk.cameraapp.databinding.ActivityMainBinding
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
        binding.mediaApiButon.setOnClickListener {

            val intent =  Intent();
            intent.type = "image/*";
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0  && resultCode == RESULT_OK) {
            if (data == null) {
                //Display an error
                return
            }
            val Uri = data.data
            val intent = Intent(this, EditorActivity::class.java)
            intent.putExtra("URI", Uri)
            startActivity(intent)
            Glide.with(applicationContext).load(Uri).into(binding.viewer)

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