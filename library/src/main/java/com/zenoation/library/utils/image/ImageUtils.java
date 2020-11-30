package com.zenoation.library.utils.image;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.zenoation.library.base.BaseApplication;
import com.zenoation.library.listener.OnCompleteListener;
import com.zenoation.library.utils.Utils;
import com.zenoation.library.utils.permission.PermissionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Created by kisoojo on 2020.02.03
 */

public class ImageUtils {

    final private String PHOTO_TEMP_PATH = BaseApplication.TEMP_PATH;
    final private int PHOTO_SIZE = 1280;
    final private int PHOTO_QUALITY = 99;

    final public static int CALL_FILE = 9997;
    final public static int CALL_GALLERY = 9998;
    final public static int CALL_CAMERA = 9999;

    private Uri mCameraFileUri;
    private String mStrCurrentPhotoPath;//실제 사진 파일 경로
    private String mStrImageCaptureName;//이미지 이름

    private OnCompleteListener mOnCompleteListener;

    //private static class LazyHolder {
    //    private static final ImageUtils INSTANCE = new ImageUtils();
    //}
    //
    //public static ImageUtils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private volatile static ImageUtils uniqueInstance;

    public static ImageUtils getInstance() {
        if (uniqueInstance == null) {
            synchronized (ImageUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ImageUtils();
                }
            }
        }
        return uniqueInstance;
    }

    private ImageUtils() {
    }

    /**
     * 사진 각도 가져오기
     */
    private int getExifOrientation(String filePath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }

        return degree;
    }

    /**
     * 사진 회전
     */
    private Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
                // 메모리 부족에러시, 원본을 반환
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 비트맵 가져오기
     */
    private synchronized Bitmap safeDecodeBitmapFile(String strFilePath) {
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                return null;
            }

            // Max image size
            final int IMAGE_MAX_SIZE = PHOTO_SIZE;
            final BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;

            //BitmapFactory.decodeFile(strFilePath, bfo);
            if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
                bfo.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
            }
            bfo.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                bfo.inPurgeable = true;
            bfo.inDither = false;
            bfo.inScaled = false;
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;

            final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);
            int degree = getExifOrientation(strFilePath);
            return getRotatedBitmap(bitmap, degree);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private String resize(String path) {

        try {
            //저장 될 파일 생성
            String fileName = Utils.getInstance().getDatetimeString2() + ".jpg";
            String filePath = PHOTO_TEMP_PATH + File.separator + fileName;
            File folder = new File(PHOTO_TEMP_PATH);
            File file = new File(filePath);
            Uri fileUri = null;
            try {
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                file.createNewFile();
                fileUri = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap photo = safeDecodeBitmapFile(path);
            if (photo == null) {
                return null;
            }

            int imgWidth = photo.getWidth();
            int imgHeight = photo.getHeight();
            float ratio;
            if (imgWidth > imgHeight)
                ratio = (float) imgWidth / 800;
            else
                ratio = (float) imgHeight / 800;

            //최대값 보다 클 경우만 크기 조정
            photo = Bitmap.createScaledBitmap(photo, (ratio <= 1.0) ? imgWidth : (int) (imgWidth / ratio), (ratio <= 1.0) ? imgHeight : (int) (imgHeight / ratio), true);

            FileOutputStream fos = new FileOutputStream(filePath);
            photo.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, fos);
            fos.flush();
            fos.close();

            return fileUri.getPath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void doGallery(final Activity activity) {
        PermissionUtils.getInstance().requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                () -> getGallery(activity, false), false);
    }

    public void doGallerySelectMultiple(final Activity activity) {
        PermissionUtils.getInstance().requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                () -> getGallery(activity, true), false);
    }

    public void doCamera(final Activity activity) {
        PermissionUtils.getInstance().requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                () -> getCamera(activity), false);
    }

    public void doFile(final Activity activity) {
        PermissionUtils.getInstance().requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                () -> getFile(activity), false);
    }

    public void getGallery(Activity activity, boolean isSelectMultiple) {
        Intent target = new Intent(Intent.ACTION_PICK);
        target.setType(MediaStore.Images.Media.CONTENT_TYPE);
        if (isSelectMultiple) {
            target.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        activity.startActivityForResult(target, CALL_GALLERY);
    }

    public void getCamera(Activity activity) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = makeCameraFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    mCameraFileUri = FileProvider.getUriForFile(activity, activity.getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
                    activity.startActivityForResult(intent, CALL_CAMERA);
                }
            }
        }
    }

    public void getFile(Activity activity) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        Intent intent = Intent.createChooser(chooseFile, "Choose a file");
        activity.startActivityForResult(intent, CALL_FILE);
    }

    public File makeCameraFile() throws IOException {
        File dir = new File(PHOTO_TEMP_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mStrImageCaptureName = Utils.getInstance().getDatetimeString2() + ".jpg";

        // File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/onvitplatform/" + mStrImageCaptureName);
        File storageDir = new File(PHOTO_TEMP_PATH + File.separator + mStrImageCaptureName);
        mStrCurrentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    public void makeGalleryFile(Context context, Uri data) {
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
        };

        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int column_data = cursor.getColumnIndexOrThrow(proj[0]);
            int column_title = cursor.getColumnIndexOrThrow(proj[1]);

            String strPath = cursor.getString(column_data);
            String strTitle = cursor.getString(column_title);

            setImageFile(context, strPath, strTitle);
            cursor.close();
        }
    }

    public void setImageFile(Context context, String path, String name) {
        path = TextUtils.isEmpty(path) ? mStrCurrentPhotoPath : path;
        name = TextUtils.isEmpty(name) ? mStrImageCaptureName : name;
        if (path == null) {
            Utils.getInstance().showToast(context, "파일을 찾을 수 없습니다.");
            return;
        }

        Uri uri = Uri.fromFile(new File(path));
        path = uri.getPath();

        path = resize(path);
        if (path == null) {
            Utils.getInstance().showToast(context, "파일을 찾을 수 없습니다.");
            return;
        }
        name = path.split("/")[path.split("/").length - 1];


    }


    private Uri filePathUri = null;

    public String getPath(final Context context, final Uri uri) {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        filePathUri = uri;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                //return getDataColumn(context, uri, null, null);
                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        FileInputStream input;
        FileOutputStream output;

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

            Utils.getInstance().showToast(context, "파일을 찾을 수 없습니다.");
            return null;

            // File file = new File(context.getCacheDir(), "tmp");
            // String filePath = file.getAbsolutePath();
            // try {
            //     ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(filePathUri, "r");
            //     if (pfd == null)
            //         return null;
            //
            //     FileDescriptor fd = pfd.getFileDescriptor();
            //     input = new FileInputStream(fd);
            //     output = new FileOutputStream(filePath);
            //     int read;
            //     byte[] bytes = new byte[4096];
            //     while ((read = input.read(bytes)) != -1) {
            //         output.write(bytes, 0, read);
            //     }
            //
            //     input.close();
            //     output.close();
            //     return new File(filePath).getAbsolutePath();
            // } catch (IOException ignored) {
            //     ignored.printStackTrace();
            // }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private String guessMimeType(byte[] topOfStream) {

        String mimeType = null;
        Properties magicmimes = new Properties();
        FileInputStream in = null;

        // Read in the magicmimes.properties file (e.g. of file listed below)
        try {
            in = new FileInputStream("magicmimes.properties");
            magicmimes.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // loop over each file signature, if a match is found, return mime type
        for (Enumeration keys = magicmimes.keys(); keys.hasMoreElements(); ) {
            String key = (String) keys.nextElement();
            byte[] sample = new byte[key.length()];
            System.arraycopy(topOfStream, 0, sample, 0, sample.length);
            if (key.equals(new String(sample))) {
                mimeType = magicmimes.getProperty(key);
                System.out.println("Mime Found! " + mimeType);
                break;
            } else {
                System.out.println("trying " + key + " == " + new String(sample));
            }
        }

        return mimeType;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
