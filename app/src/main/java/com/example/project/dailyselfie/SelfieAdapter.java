package com.example.project.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hoang on 24/09/2015.
 */
class SelfieAdapter extends ArrayAdapter {
    private static class ViewHolder {
        ImageView picture;
        TextView fileName;
    }

    public SelfieAdapter(Context context, ArrayList<File> pictures) {
        super(context, R.layout.item_pic, pictures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        File file = (File)getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pic, parent, false);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
            viewHolder.fileName = (TextView) convertView.findViewById(R.id.picture_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        setPic(viewHolder.picture, file.getAbsolutePath());
        viewHolder.fileName.setText(file.getName());
        // Return the completed view to render on screen
        return convertView;
    }

    private void setPic(ImageView image, String path) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        image.setImageBitmap(bitmap);
    }
}
