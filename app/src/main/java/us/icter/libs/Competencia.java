package us.icter.libs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;

import us.icter.brownsea.MainActivity;

/**
 * Created by jamp on 7/5/16.
 */
public class Competencia extends ArrayList<Estacion> {
    private static Competencia cInstancia = null;
    SharedPreferences shared;
    Pruebas p = new Pruebas();

    public Competencia(SharedPreferences s) {
        //Context context = a.getApplicationContext();
        String tmpList = s.getString("Pruebas", null);
        Log.d("C", tmpList);
        if (tmpList != null) {
            String[] list = tmpList.split(",");
            for (String i : list) {
                this.add(i);
            }
        }
    }

    public static Competencia getInstancia(SharedPreferences s) {
        if (cInstancia == null)
            cInstancia = new Competencia(s);
        return cInstancia;
    }

    public boolean aprobarEstacion(String code) {
        boolean result = false;
        for (Estacion e: this) {
            if (e.getCode().equals(code)) {
                e.approved();
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean add(String code) {
        boolean result = false;
        for (Estacion e: p) {
            if (e.getCode().equals(code)) {
                result = this.add(e);
                break;
            }
        }
        return result;
    }
}
