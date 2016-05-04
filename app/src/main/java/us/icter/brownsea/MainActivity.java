package us.icter.brownsea;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import us.icter.activitys.ImageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnTest);
        button.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.putExtra("Image", R.drawable.infografia_ebola);
            startActivity(intent);
        }
    }
}
