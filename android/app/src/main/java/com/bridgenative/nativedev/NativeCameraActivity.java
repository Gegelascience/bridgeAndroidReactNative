package com.bridgenative.nativedev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bridgenative.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NativeCameraActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private static final int CAMERA_REQUEST = 100;

    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_camera);
        previewView = findViewById(R.id.viewFinder);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
                Button buttonPhoto = findViewById(R.id.photoBtn);
                buttonPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Executor takePhotoExe = Executors.newSingleThreadExecutor();
                        imageCapture.takePicture(takePhotoExe,
                                new ImageCapture.OnImageCapturedCallback() {
                                    @Override
                                    @androidx.camera.core.ExperimentalGetImage
                                    public void onCaptureSuccess(ImageProxy imageProxy) {
                                        // insert your code here.
                                        Image mediaImage = imageProxy.getImage();

                                        if (mediaImage != null) {
                                            Bitmap refBitmap = toBitmap(mediaImage);

                                            if(refBitmap != null){
                                                Log.w(" photo ok","photo ok");
                                            }
                                            imageProxy.close();
                                        }
                                    }
                                    @Override
                                    public void onError(ImageCaptureException error) {
                                        // insert your code here.
                                        Log.e(" photo error",error.getMessage());
                                    }
                                }
                        );
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                Log.e(" camera error",e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "CameraX permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "CameraX permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageCapture, preview);

    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        if (planes.length == 1 && image.getFormat() == 256) {
            try {
                ByteBuffer planeBuff = planes[0].getBuffer();
                byte[] bytes = new byte[planeBuff.remaining()];
                planeBuff.get(bytes);
                return BitmapFactory.decodeByteArray(bytes, 0,bytes.length, null);

            } catch (Exception ex) {
                Log.e("error",ex.toString());
                return null;
            }

        }
        return null;
    }
}