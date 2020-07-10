package mx.uv.siremu_android.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.siremu_android.R;

public class AdaptadorListas extends ArrayAdapter<ListaReproduccion> {
    private final Activity contexto;
    private final List<ListaReproduccion> misListas;

    public AdaptadorListas(Activity contexto, List<ListaReproduccion> listas) {
        super(contexto, R.layout.fila_lista_ilustracion, listas);
        this.contexto=contexto;
        this.misListas=listas;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=contexto.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_lista_ilustracion, null,true);
        TextView principal = rowView.findViewById(R.id.tvPrincipal);
        ImageView imagen = rowView.findViewById(R.id.imgIlustracion);
        TextView secundario = rowView.findViewById(R.id.tvSecundario);
        TextView tercero = rowView.findViewById(R.id.tvTercero);
        principal.setText(misListas.get(position).getNombre());
        secundario.setText(misListas.get(position).getNombreCreador());
        tercero.setText("Likes: "+misListas.get(position).getLikes());
        byte[] img = misListas.get(position).getIlustracion().toByteArray();
        Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
        imagen.setImageBitmap(ilustracion);
        return rowView;
    }
}
