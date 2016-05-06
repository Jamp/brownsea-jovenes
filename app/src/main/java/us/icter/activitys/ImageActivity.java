package us.icter.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ortiz.touch.TouchImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import us.icter.brownsea.MainActivity;
import us.icter.brownsea.R;
import us.icter.libs.Estacion;
import us.icter.libs.Pruebas;
import us.icter.libs.Upload;

public class ImageActivity extends AppCompatActivity implements OnClickListener {
    static final int REQUEST_VIDEO_CAPTURE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 010;
    private Pruebas pruebas = new Pruebas();
    private TouchImageView image;
    private String fileURI;
    Estacion prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String code = getIntent().getStringExtra("Code");

        prueba = pruebas.getEstacion(code);
        //mainActivity.addPrueba(prueba);

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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "Bronwsea");
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, "Patrulla_" + timeStamp + ".3gp");
        Uri uriSavedVideo = Uri.fromFile(image);

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, prueba.getExtras());
            takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, "Landscape");
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            //takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void takePhoto() {
        //camera stuff
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "Bronwsea");
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, "Patrulla_" + timeStamp + ".jpg");
        Uri uriSavedImage = Uri.fromFile(image);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
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
                //Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                Log.i("VIDEO", data.getData().toString());


                SendData upload = new SendData();
                upload.activity = ImageActivity.this;
                upload.type = 2;
                upload.URI = getRealPathFromURI(data.getData());
                upload.execute();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                SendData upload = new SendData();
                upload.activity = ImageActivity.this;
                upload.type = 3;
                upload.URI = fileURI;
                upload.execute();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Foto cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Fallo al tomar foto", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private class SendData extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog;
        private ImageActivity activity;
        public int type;
        public String patrulla;
        public String URI;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getResources().getString(R.string.loading_upload));
            pDialog.setIndeterminate(false);

            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Upload upload = new Upload(type, URI);
                upload.doFileUpload();
            } catch (Exception e) {
                Log.e("COMUNICACION", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            Toast.makeText(activity, "Subida Exitosa", Toast.LENGTH_LONG).show();
        }
    }

}
