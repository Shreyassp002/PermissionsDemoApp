package com.rey.permissionsdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.icu.text.CaseMap.Title
import android.os.Message
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
            if (isGranted){
                Toast.makeText(this, "Permission Granted For Camera", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Permission Denied For Camera", Toast.LENGTH_LONG).show()
            }
        }

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if(isGranted){
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission Granted for Precise Location", Toast.LENGTH_LONG).show()
                    }else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission Granted for Approximate Location", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Permission Granted for Camera", Toast.LENGTH_LONG).show()
                    }
                }else{
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission Denied for Fine Location", Toast.LENGTH_LONG).show()
                    }else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission Denied for Approximate Location", Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCameraPermission: Button = findViewById(R.id.btnCameraPermission)
        btnCameraPermission.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationaleDialog("Permission Demo requires Camera Access","Camera cannot be used because Camera access denied")
            }else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _-> dialog.dismiss()
            }
        builder.create().show()
    }
}