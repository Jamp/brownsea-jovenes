package us.icter.libs;

import android.content.Context;
import java.util.ArrayList;

import us.icter.brownsea.R;

/**
 * Created by jamp on 1/5/16.
 */
public class Pruebas extends ArrayList<Estacion> {

    public void Pruebas(Context context) {
        Estacion estacion;

        estacion = new Estacion("123", "Ebola", 1, context.getResources().getDrawable(R.drawable.infografia_ebola));
        this.add(estacion);

        estacion = new Estacion("133", "Ebola", 2, context.getResources().getDrawable(R.drawable.infografia_ebola));
        this.add(estacion);

        estacion = new Estacion("134", "Ebola", 3, context.getResources().getDrawable(R.drawable.infografia_ebola));
        this.add(estacion);
    }

    public Estacion getEstacion(String code) {
        for (Estacion e : this) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

/*    public int getApproved() {

    }*/

    public int getSize() {
        return this.size();
    }
}
