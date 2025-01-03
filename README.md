VERSION 1.3.0

__Step 1.__ Add the JitPack repository to your build file

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    

__Step 2.__ Add the dependency    
    
    dependencies {
        implementation 'com.github.kisoojo:ksbaseapp:1.0'
    }


__Step 3.__ Add the below code at AndroidManifest.xml

    <application
        android:name={APPLICATION_NAME}
        ...
        tools:replace="android:name"

        ...

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities={PACKAGE_NAME}
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>

    </application>


__How to use__

    1. ImageUtils

    ImageUtils.getInstance().setOnCameraCompleteListener(OnCompleteParamListener)
    ImageUtils.getInstance().setOnGalleryCompleteListener(OnCompleteParamListener)
    ImageUtils.getInstance().setOnFileCompleteListener(OnCompleteParamListener)
  
    showImageFileDialog(Activity activity, View.OnClickListener cameraListener, View.OnClickListener galleryListener)
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageUtils.CALL_CAMERA) {
                ImageUtils.getInstance().setImageFile(this, ImageUtils.CALL_CAMERA, "", "");
            } else if (requestCode == ImageUtils.CALL_GALLERY) {
                if (data != null) {
                    if (data.getClipData() != null) {
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            ClipData.Item item = data.getClipData().getItemAt(i);
                            ImageUtils.getInstance().makeGalleryFile(this, item.getUri());
                        }
                    } else if (data.getData() != null) {
                        ImageUtils.getInstance().makeGalleryFile(this, data.getData());
                    }
                }
            }
        }
    }
    
    
    2. WebView
    
    ContentValues params = new ContentValues();
    params.put("title", "타이틀");
    params.put("url", "http://kisoojo.com");
    params.put("btn_text", "버튼");
    startActivity(WebViewActivity.class, params);
    
    
    3. DialogUtils
    
    showAlertDialog(Activity activity, String text)
    showConfirmDialog(Activity activity, String text, @NonNull View.OnClickListener confirmListener)