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

        estacion = new Estacion("pdc1_reto1", "Punto 1 - Reto 1", 1, 2, R.drawable.pdc1_reto1);
        add(estacion);
        estacion = new Estacion("pdc1_reto3", "Punto 1 - Reto 3", 1, 3, R.drawable.pdc1_reto2, 25);
        add(estacion);
        estacion = new Estacion("pdc1_reto4", "Punto 1 - Reto 4", 1, 2, R.drawable.pdc1_reto3);
        add(estacion);
        estacion = new Estacion("pdc1_reto2", "Punto 1 - Reto 2", 1, 2, R.drawable.pdc1_reto4);
        add(estacion);
        estacion = new Estacion("pdc1_reto5", "Punto 1 - Reto 5", 1, 2, R.drawable.pdc1_reto5);
        add(estacion);

        estacion = new Estacion("pdc2_reto1", "Punto 2 - Reto 1", 2, 3, R.drawable.pdc2_reto1, 25);
        add(estacion);
        estacion = new Estacion("pdc2_reto2", "Punto 2 - Reto 2", 2, 2, R.drawable.pdc2_reto2);
        add(estacion);
        estacion = new Estacion("pdc2_reto3", "Punto 2 - Reto 3", 2, 3, R.drawable.pdc2_reto3, 25);
        add(estacion);
        estacion = new Estacion("pdc2_reto4", "Punto 2 - Reto 4", 2, 2, R.drawable.pdc2_reto4);
        add(estacion);
        estacion = new Estacion("pdc2_reto5", "Punto 2 - Reto 5", 2, 2, R.drawable.pdc2_reto5);
        add(estacion);

        estacion = new Estacion("pdc3_reto1", "Punto 3 - Reto 1", 3, 3, R.drawable.pdc3_reto1, 25);
        add(estacion);
        estacion = new Estacion("pdc3_reto2", "Punto 3 - Reto 2", 3, 2, R.drawable.pdc3_reto2);
        add(estacion);
        estacion = new Estacion("pdc3_reto3", "Punto 3 - Reto 3", 3, 2, R.drawable.pdc3_reto3);
        add(estacion);
        estacion = new Estacion("pdc3_reto4", "Punto 3 - Reto 4", 3, 2, R.drawable.pdc3_reto4);
        add(estacion);
        estacion = new Estacion("pdc3_reto5", "Punto 3 - Reto 5", 3, 2, R.drawable.pdc3_reto5);
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
