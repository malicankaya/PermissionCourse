package com.malicankaya.permission

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
    private var btnRequestPermission: Button? = null
    private var btnRequestMultiplePermission: Button? = null

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission accepted for camera.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied for camera.", Toast.LENGTH_SHORT).show()
            }
        }
    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permission = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permission == Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Permission accepted for camera.", Toast.LENGTH_SHORT)
                            .show()
                    } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Permission accepted for fine location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Permission accepted for coarse location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (permission == Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Permission denied for camera.", Toast.LENGTH_SHORT)
                            .show()
                    } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Permission denied for fine location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Permission denied for coarse location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRequestPermission = findViewById(R.id.btnRequestPermission)
        btnRequestMultiplePermission = findViewById(R.id.btnRequestMultiplePermission)

        btnRequestPermission?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            ) {
                showRationaleDialog(
                    "Permission demo requires camera access",
                    "Camera cannot be used because Camera access is denied."
                )
            } else {
                cameraResultLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        btnRequestMultiplePermission?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    showRationaleDialog(
                        "Permission demo requires camera access",
                        "Camera cannot be used because Camera access is denied."
                    )
                }
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    showRationaleDialog(
                        "Permission demo requires fine location access",
                        "Fine location cannot be used because fine location access is denied."
                    )
                }
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    showRationaleDialog(
                        "Permission demo requires coarse location access",
                        "Coarse location cannot be used because coarse location access is denied."
                    )
                }

            } else {
                cameraAndLocationResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}