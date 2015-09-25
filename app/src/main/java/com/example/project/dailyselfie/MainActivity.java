package com.example.project.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    public static String ALBUM_NAME = "daily_selfie";
    ImageView mImageView;
    String mCurrentPhotoPath;
    ListView listView;
    ArrayList<File> filesList;
    File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.listView);
        Intent intent = new Intent(MainActivity.this, DailySelfieBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                DailySelfieBroadcast.REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(am.RTC_WAKEUP, System.currentTimeMillis(), 2000 , pendingIntent);
        //Log.d("Image", mCurrentPhotoPath);
        File storageDir = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME);
        Log.d("Dir", storageDir.getAbsolutePath());
        if(isExternalStorageWritable()){
            Log.d("SD", "error");
        }

//        File f = new File(path);
        File file[] = storageDir.listFiles();
        filesList = new ArrayList<File>();
        SelfieAdapter adapter = new SelfieAdapter(getApplicationContext(), filesList);
        if(file.length > 0) {
            Log.d("Files", "Size: " + file.length);
            for (int i = 0; i < file.length; i++) {
                Log.d("Files", "FileName:" + file[i].getName());
            }
            filesList = new ArrayList<File>();
            filesList.addAll(Arrays.asList(file));
            SelfieAdapter adapter = new SelfieAdapter(getApplicationContext(), filesList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplication(), PictureActivity.class);
                    intent.putExtra("FilePath", filesList.get(position).getAbsolutePath());
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_camera:
                //Start the camera if user click camera icon
                Log.d("Camera", "Start the camera");
                dispatchTakePictureIntent();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            if (requestCode == REQUEST_TAKE_PHOTO) {
                if(data != null){

                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        //Intent for camera and start camera app activity
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME);
        if (!storageDir.mkdirs()) {
            Log.e("file", "Directory not created");
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}