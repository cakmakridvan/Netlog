package com.example.mobiltestcase.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.mobiltestcase.R;

public class Image extends Activity implements View.OnClickListener {

    ImageView img;
    ImageButton closeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        img = findViewById(R.id.img_photo);
        img.setImageResource(R.drawable.img_irsaliye);
        closeImage = findViewById(R.id.close_image);
        closeImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_image) {
            finish();
        }
    }
}
