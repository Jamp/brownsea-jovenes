package us.icter.brownsea;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import us.icter.activitys.ImageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnTest);
        button.setOnClickListener(new ButtonListener());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanId = scanningResult.getContents();

            if (scanId != null) {
                //setCode(scanId);
                Intent intentActivity = new Intent(MainActivity.this, ImageActivity.class);
                intentActivity.putExtra("Code", scanId);
                startActivity(intentActivity);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_scan), Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_scan), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            // integrator.setPrompt("Scan a barcode");
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setBeepEnabled(true);
            integrator.initiateScan();
        }
    }
}
