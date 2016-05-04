package us.icter.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.ortiz.touch.TouchImageView;

import us.icter.brownsea.R;

public class ImageActivity extends AppCompatActivity {
    private TouchImageView image;
    //private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        int idImage = getIntent().getIntExtra("Image", R.drawable.infografia_ebola);
        image = (TouchImageView) findViewById(R.id.img);
        image.setImageDrawable(getResources().getDrawable(idImage));

        FloatingActionButton btnAnswer = (FloatingActionButton) findViewById(R.id.btnAnswer);
        btnAnswer.setOnClickListener(new AnswerListener());
    }

    private class AnswerListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ImageActivity.this, AnswerActivity.class);
            startActivity(intent);
        }
    }
}
