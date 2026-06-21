package com.safety.ticket.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.safety.ticket.R;
import com.safety.ticket.data.Photo;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.utils.DateTimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    private long ticketId;
    private TicketDao ticketDao;
    private static final int REQUEST_CAMERA = 100;

    private GridLayout gridPhotos;
    private Button btnTakePhoto;
    private EditText etPhotoDesc;
    private File currentPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ticketId = getIntent().getLongExtra("ticket_id", -1);
        ticketDao = new TicketDao(this);

        gridPhotos = findViewById(R.id.gridPhotos);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        etPhotoDesc = findViewById(R.id.etPhotoDesc);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        loadPhotos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhotos();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                currentPhotoFile = new File(storageDir, "IMG_" + System.currentTimeMillis() + ".jpg");
                Uri photoUri = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider", currentPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            } catch (Exception e) {
                Toast.makeText(this, "创建照片文件失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (currentPhotoFile != null && currentPhotoFile.exists()) {
                String desc = etPhotoDesc.getText().toString().trim();
                if (desc.isEmpty()) {
                    desc = "现场照片";
                }
                Photo photo = new Photo();
                photo.setTicketId(ticketId);
                photo.setPhotoPath(currentPhotoFile.getAbsolutePath());
                photo.setPhotoDesc(desc);
                photo.setTakeTime(DateTimeUtil.getNowFull());
                ticketDao.insertPhoto(photo);
                etPhotoDesc.setText("");
                loadPhotos();
                Toast.makeText(this, "照片已保存", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPhotos() {
        gridPhotos.removeAllViews();
        List<Photo> photos = ticketDao.getPhotosByTicketId(ticketId);
        for (final Photo photo : photos) {
            View view = getLayoutInflater().inflate(R.layout.item_photo_grid, gridPhotos, false);
            ImageView ivPhoto = view.findViewById(R.id.ivPhoto);
            TextView tvDesc = view.findViewById(R.id.tvPhotoDesc);

            Bitmap bmp = BitmapFactory.decodeFile(photo.getPhotoPath());
            if (bmp != null) {
                ivPhoto.setImageBitmap(bmp);
            }
            tvDesc.setText(photo.getPhotoDesc());

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeletePhotoDialog(photo);
                    return true;
                }
            });

            gridPhotos.addView(view);
        }
    }

    private void showDeletePhotoDialog(final Photo photo) {
        new AlertDialog.Builder(this)
                .setTitle("删除照片")
                .setMessage("是否删除此照片？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ticketDao.deletePhoto(photo.getId());
                        File f = new File(photo.getPhotoPath());
                        if (f.exists()) f.delete();
                        loadPhotos();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
