package us.icter.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import us.icter.brownsea.R;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView img1 = (ImageView) findViewById(R.id.imageView1);
        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        ImageView img3 = (ImageView) findViewById(R.id.imageView3);

        if (img1 != null) {
            img1.setImageDrawable(getResources().getDrawable(R.drawable.infografia_ebola));
        }
        if (img2 != null) {
            img2.setImageDrawable(getResources().getDrawable(R.drawable.infografia_ebola));
        }
        if (img3 != null) {
            img3.setImageDrawable(getResources().getDrawable(R.drawable.infografia_ebola));
        }

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
