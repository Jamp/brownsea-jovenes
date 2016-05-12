package us.icter.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
    private String code;
    Estacion prueba;
    String patrulla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        code = getIntent().getStringExtra("Code");

        prueba = pruebas.getEstacion(code);
        //mainActivity.addPrueba(prueba);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PFTAG, Context.MODE_PRIVATE);
        patrulla = sharedPreferences.getString(MainActivity.PFDATA, null);

        image = (TouchImageView) findViewById(R.id.img);
        image.setImageDrawable(getResources().getDrawable(prueba.getTask()));

        FloatingActionButton btnPhoto = (FloatingActionButton) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);

        FloatingActionButton btnVideo = (FloatingActionButton) findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(this);

        if (prueba.getType() == 2) {
            btnPhoto.setVisibility(View.VISIBLE);
            btnVideo.setVisibility(View.INVISIBLE);
        } else {
            btnPhoto.setVisibility(View.INVISIBLE);
            btnVideo.setVisibility(View.VISIBLE);
        }
    }

    private void responseTask() {
        Intent intent = new Intent(ImageActivity.this, AnswerActivity.class);
        startActivity(intent);
    }

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, prueba.getExtras());
            takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, "Landscape");
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/", getResources().getString(R.string.app_name));
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, "Patrulla_" + timeStamp + ".jpg");
        // File image = new File(newFileName() + ".jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        fileURI = image.toString();

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
                Log.i("VIDEO", getRealPathFromURI(data.getData()));


                SendData upload = new SendData();
                upload.activity = ImageActivity.this;
                upload.patrulla = patrulla;
                upload.punto = prueba.getPunto();
                upload.type = 3;
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
                upload.patrulla = patrulla;
                upload.punto = prueba.getPunto();
                upload.type = 2;
                upload.URI = fileURI;
                upload.execute();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Foto cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Fallo al tomar foto", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String newFileName() {
        //camera stuff
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/", getResources().getString(R.string.app_name));
        imagesFolder.mkdirs();

        // File image = new File(imagesFolder.toString() + "Patrulla_" + timeStamp + ".jpg");
        return imagesFolder.toString() + "/Patrulla_" + timeStamp;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private class SendData extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pDialog;
        private ImageActivity activity;
        public int type;
        public String patrulla;
        public int punto;
        public String URI;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getResources().getString(R.string.loading_upload));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);

            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            // try {
            if (patrulla != null) {
                    Upload upload = new Upload(code, patrulla, punto, type, URI);
                    resultado = upload.doFileUpload();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageActivity.this);
                    AlertDialog alertDialog = alertDialogBuilder
                            .setTitle("Sin Configuración")
                            .setMessage("Por favor, cierre y vuelva a iniciar la aplicación")
                            .create();
                    alertDialog.show();
                }
            // } catch (Exception e) {
            //     Log.e("COMUNICACION", e.getMessage());
            // }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            pDialog.dismiss();
            if (b) {
                Toast.makeText(activity, "Subida de datos exitosa", Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(activity, "Error durante la subida de datos", Toast.LENGTH_LONG).show();
        }
    }

}
