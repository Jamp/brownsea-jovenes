package us.icter.libs;

/**
 * Created by jamp on 30/4/16.
 */
public class Estacion {
    private String code; // Código del QR
    private String name; // Nombre de la Estación
    private int type; // Tipo de estación (Texto, Vídeo, foto)
    private int extras; // Duración del video
    private int task; // Imagen de la tarea
    private boolean status; // Estado del estación

    public Estacion(String code, String name, int type, int task) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.task = task;
        this.status = false;
    }

    public Estacion(String code, String name, int type, int task, int extras) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.task = task;
        this.extras = extras;
        this.status = false;
    }

    public void approved() {
        this.status = true;
    }

    public void unapproved() {
        this.status = false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getExtras() {
        return extras;
    }

    public int getTask() {
        return task;
    }

    public boolean getStatus() {
        return this.status;
    }
}
