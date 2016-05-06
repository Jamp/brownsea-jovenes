package us.icter.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ortiz.touch.TouchImageView;

import us.icter.brownsea.MainActivity;
import us.icter.brownsea.R;
import us.icter.libs.Estacion;
import us.icter.libs.Pruebas;

public class ImageActivity extends AppCompatActivity implements OnClickListener {
    static final int REQUEST_VIDEO_CAPTURE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 010;
    private TouchImageView image;
    Estacion prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String code = getIntent().getStringExtra("Code");
        MainActivity mainActivity = (MainActivity) getParent();
        Pruebas pruebas = mainActivity.getPruebas();

        prueba = pruebas.getEstacion(code);
        mainActivity.addPrueba(prueba);

        image = (TouchImageView) findViewById(R.id.img);
        image.setImageDrawable(getResources().getDrawable(prueba.getTask()));

        FloatingActionButton btnAnswer = (FloatingActionButton) findViewById(R.id.btnAnswer);
        btnAnswer.setOnClickListener(this);
    }

    private void responseTask() {
        Intent intent = new Intent(ImageActivity.this, AnswerActivity.class);
        startActivity(intent);
    }

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, prueba.getExtras());
        takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, "Landscape");
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onClick(View view) {
        if (prueba != null) {
            switch (prueba.getType()) {
                case 1:
                    responseTask();
                    break;
                case 2:
                    takePhoto();
                    break;
                case 3:
                    takeVideo();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }

}
