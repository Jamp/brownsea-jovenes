package us.icter.libs;

import android.content.Context;
import java.util.ArrayList;

import us.icter.brownsea.R;

/**
 * Created by jamp on 1/5/16.
 */
public class Pruebas extends ArrayList<Estacion> {

    public Pruebas() {
        Estacion estacion;

        estacion = new Estacion("123", "Ebola", 1, R.drawable.infografia_ebola);
        add(estacion);

        estacion = new Estacion("133", "Redes Sociales", 2, R.drawable.infografia_adictos_a_las_redes);
        add(estacion);

        estacion = new Estacion("134", "Prevención de Ebola", 3, R.drawable.infografia_ebola, 5);
        add(estacion);
    }

    public Estacion getEstacion(String code) {
        for (Estacion e : this) {
            if (e.getCode().equals(code)) {
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
