package com.example.groomzoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomCamera extends AppCompatActivity {

    Button button;
    TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;
    String parseKey;
    String pictureMsg = "Take a picture with you face the ";
    String directionKey = "direction";
    String cameraPermissionMsg = "Sorry, camera permission is necessary";
    String frontKey = "frontSelfie";
    String frontMsg = "front";
    String leftMsg = "left";
    String leftKey = "leftSelfie";
    String rightKey = "rightSelfie";
    String configurationFailedMsg = "Configuration Changed";
    String updatedMsg = "Picture Updated!";
    String errorMsg = "error uploading";
    String backgroundThread = "Camera Background";
    String jpgExtension = ".jpg";
    String pngExtension = ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);

        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        button = (Button) findViewById(R.id.button_capture);
        textureView.setSurfaceTextureListener(textureListener);

        Intent intent = getIntent();
        final String direction = intent.getStringExtra(directionKey);
        Toast.makeText(getApplicationContext(), pictureMsg + direction, Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    takePicture(direction);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getApplicationContext(), cameraPermissionMsg , Toast.LENGTH_LONG).show();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        try {
            openCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }
};

private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        cameraDevice = camera;
        try {
            createCameraPreview();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        cameraDevice.close();
    }

    @Override
    public void onError(@NonNull CameraDevice camera, int i) {
        cameraDevice.close();
        cameraDevice = null;

    }
};

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if(cameraDevice == null)
                    return;
                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), configurationFailedMsg, Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if(cameraDevice == null){
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);

    }


    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        cameraId = manager.getCameraIdList()[1];

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CustomCamera.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }
        manager.openCamera(cameraId, stateCallback, null);

    }

    private void takePicture(final String direction) throws CameraAccessException {
        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;

        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

        int width = 640;
        int height = 480;

        if(jpegSizes != null && jpegSizes.length > 0){
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }
        ImageReader reader = ImageReader.newInstance(width,height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE,  CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        file = new File(Environment.getDataDirectory() + "/" + ts + jpgExtension);

        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = null;
                image = imageReader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                try {
                    save(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(image != null) {
                        image.close();
                    }
                }
                final ParseUser currentUser = ParseUser.getCurrentUser();
                String username = currentUser.getUsername();
                ParseFile newSelfie = null;
                if(direction.equals(frontMsg)){
                    newSelfie = new ParseFile(username + frontKey + pngExtension, bytes);
                    parseKey = frontKey;
                }
                else if(direction.equals(leftMsg)){
                    newSelfie = new ParseFile(username + leftKey + pngExtension, bytes);
                    parseKey = leftKey;
                }
                else{
                    newSelfie = new ParseFile(username + rightKey + pngExtension, bytes);
                    parseKey = rightKey;
                }
                final ParseFile finalNewSelfie = newSelfie;
                newSelfie.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            currentUser.put(parseKey, finalNewSelfie);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    return;
                                }
                            });
                            Toast.makeText(getApplicationContext(), updatedMsg, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
        final CameraCaptureSession.CaptureCallback  captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, CaptureRequest request,  TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                try {
                    createCameraPreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }
        };
        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

            }
        }, mBackgroundHandler);
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startBackgroundThread();
        if(textureView.isAvailable()){
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread(backgroundThread);
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

    }

    @Override
    protected void onPause() {
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;


    }
}