package us.icter.libs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.icter.brownsea.R;

/**
 * Created by jamp on 5/5/16.
 */
public class EstacionAdapter extends ArrayAdapter<Estacion> {
    Context context;
    ArrayList<Estacion> data;
    LayoutInflater inflater;

    public EstacionAdapter(Context context, ArrayList<Estacion> estaciones) {
        super(context, -1, estaciones);

        this.context = context;
        this.data = estaciones;
        this.inflater =  LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Estacion currentEstacion = data.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pruebas_item, null);

            holder = new ViewHolder();
            holder.imageItem = (ImageView) convertView.findViewById(R.id.imageItem);
            holder.titleItem = (TextView) convertView.findViewById(R.id.titleItem);
            holder.subtitleItem = (TextView) convertView.findViewById(R.id.subtitleItem);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (currentEstacion.getStatus())
            holder.imageItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_black_24dp));
        else
            holder.imageItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_query_builder_black_24dp));

        holder.titleItem.setText(currentEstacion.getName());
        switch (currentEstacion.getType()) {
            case 1:
                holder.subtitleItem.setText("Texto");
                break;
            case 2:
                holder.subtitleItem.setText("Foto");
                break;
            case 3:
                holder.subtitleItem.setText("Vídeo");
                break;
        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageItem;
        public TextView titleItem;
        public TextView subtitleItem;
    }
}
