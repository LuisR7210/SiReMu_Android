package mx.uv.siremu_android.adaptadores;

import android.annotation.SuppressLint;
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

import mx.uv.manejoCanciones.Cancion;
import mx.uv.siremu_android.R;

public class AdaptadorCanciones extends ArrayAdapter<Cancion> {
    private final Activity contexto;
    private final List<Cancion> misCanciones;

    public AdaptadorCanciones(Activity contexto, List<Cancion> canciones) {
        super(contexto, R.layout.fila_lista_ilustracion, canciones);
        this.contexto=contexto;
        this.misCanciones=canciones;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=contexto.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_lista_ilustracion, null,true);
        TextView principal = (TextView) rowView.findViewById(R.id.tvPrincipal);
        TextView secundario = (TextView) rowView.findViewById(R.id.tvSecundario);
        TextView tercero = (TextView) rowView.findViewById(R.id.tvTercero);
        ImageView imagen = (ImageView) rowView.findViewById(R.id.imgIlustracion);
        principal.setText(misCanciones.get(position).getNombre());
        secundario.setText(misCanciones.get(position).getArtista());
        tercero.setText(misCanciones.get(position).getDuracion());
        byte[] img = misCanciones.get(position).getAlbum().getIlustracion().toByteArray();
        Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
        imagen.setImageBitmap(ilustracion);
        return rowView;
    };
}
