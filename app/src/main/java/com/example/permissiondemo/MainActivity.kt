package com.example.permissiondemo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    /**
        ActivityResultLauncher(Средство запуска результатов деятельности) - A launcher
            for a previously-prepared call to start the process of
            executing an ActivityResultContract

        ActivityResultContracts - A collection of some standard
            activity call contracts, as provided by android.
    */

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted ->
                if(isGranted){
                    Toast.makeText(this,
                        "Permission granted for camera.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,
                        "Permission denied for camera.", Toast.LENGTH_SHORT).show()
                }
        }

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
                permissions.entries.forEach{
                    val permissionName = it.key
                    val isGranted = it.value
                    if(isGranted){
                        if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                            Toast.makeText(
                                this,
                                "Permission granted for fine location.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                            Toast.makeText(
                                this,
                                "Permission granted for coarse location.",
                                Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(
                                this,
                                "Permission granted for camera.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }else{
                        if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                            Toast.makeText(
                                this,
                                "Permission denied for fine location.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                            Toast.makeText(
                                this,
                                "Permission denied for coarse location.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }else{
                            Toast.makeText(
                                this,
                                "Permission denied for camera.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCameraPermission: Button = findViewById(R.id.btn_camera_permission)
        btnCameraPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                // Check if the permission exist(if it hasn't been requested before)
                (
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            ) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    showRationalDialog(
                        "Permission Demo requires fine location access",
                        "Fine Location can't be used because access is denied"
                    )
                }else {
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    showRationalDialog(
                        "Permission Demo requires coarse location access",
                        "Coarse location can't be used because access is denied"
                    )
                }

                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showRationalDialog(
                        "Permission Demo requires camera access",
                        "Camera can't be used because access is denied"
                    )
                }
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

    private fun showRationalDialog(
        title: String,
        message: String
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _->
                dialog.dismiss()}
        builder.create().show()
    }
}