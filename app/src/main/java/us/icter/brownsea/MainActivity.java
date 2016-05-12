package us.icter.brownsea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Objects;

import us.icter.activitys.ConfigActivity;
import us.icter.activitys.ImageActivity;
import us.icter.libs.Estacion;
import us.icter.libs.EstacionAdapter;
import us.icter.libs.Pruebas;

public class MainActivity extends AppCompatActivity {
    public final static String PFTAG = "Bronwsea";
    public final static String PFDATA = "Patrulla";
    public final static String EDATA = "Resultado";
    public boolean activo = false;

    private Pruebas pruebas = new Pruebas();
    private ArrayList<Estacion> listaPruebas = new ArrayList<Estacion>();
    private ArrayAdapter<Estacion> adapter;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mostrar el logo de la actividad solo cuando lista este vacía
        logo = (ImageView) findViewById(R.id.tempBackground);

        ListView lstPruebas = (ListView) findViewById(R.id.lstPruebas);
        adapter = new EstacionAdapter(getApplicationContext(), listaPruebas);
        lstPruebas.setAdapter(adapter);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnTest);
        button.setOnClickListener(new ButtonListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences(PFTAG, Context.MODE_PRIVATE);
        String patrulla = prefs.getString(PFDATA, null);

        if (patrulla == null) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
        } else {
            firebaseComponent(patrulla);
        }
    }

    private void firebaseComponent(String patrulla) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.loading_data));
        pDialog.setIndeterminate(false);

        pDialog.show();

        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://brilliant-inferno-3084.firebaseio.com/");

        myFirebaseRef
                .child("respuesta")
                .orderByChild("patrulla")
                .equalTo(patrulla)
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (pDialog != null)
                    pDialog.dismiss();

                int total = 0;
                int aprobadas = 0;
                listaPruebas.clear();

                for (DataSnapshot d: snapshot.getChildren()){
                    total += 1;
                    String codigo = (String) d.child("codigo").getValue();
                    Long estado = (Long) d.child("estado").getValue();

                    Estacion p = pruebas.getEstacion(codigo);
                    Estacion e = new Estacion(codigo, p.getName(), p.getPunto(), p.getType(), p.getTask(), p.getExtras());
                    p = null;

                    if (estado == 1L) {
                        e.approved();
                        aprobadas += 1;
                    } else
                       e.unapproved();
                    listaPruebas.add(e);
                }

                if (total == aprobadas)
                    activo = true;
                else
                    activo = false;

                adapter.notifyDataSetChanged();

                if (listaPruebas.size() != 0)
                    logo.setVisibility(View.INVISIBLE);
                else
                    logo.setVisibility(View.VISIBLE);
            }

            @Override public void onCancelled(FirebaseError error) {
                if (pDialog != null)
                    pDialog.dismiss();
            }
        });
    }

    public Pruebas getPruebas() {
        return pruebas;
    }

    public void addPrueba(Estacion e) {
        listaPruebas.add(e);
        // adapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_OK)
            Log.d("RESULT", intent.getStringExtra(EDATA));

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanId = scanningResult.getContents();

            if (scanId != null) {
                if (pruebas.getEstacion(scanId) != null) {
                    //setCode(scanId);
                    Intent intentActivity = new Intent(MainActivity.this, ImageActivity.class);
                    intentActivity.putExtra("Code", scanId);
                    startActivity(intentActivity);
                    // setResult(Activity.RESULT_OK);
                } else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_code), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_scan), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_scan), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    private class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (MainActivity.this.activo) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                // integrator.setPrompt("Scan a barcode");
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog alertDialog = alertDialogBuilder
                        .setTitle("No puedes")
                        .setMessage("Debes aprobar todas las pruebas que tienes antes de continuar")
                        .create();
                alertDialog.show();
            }
        }
    }
}
