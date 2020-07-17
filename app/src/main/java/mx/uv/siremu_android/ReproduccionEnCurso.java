package mx.uv.siremu_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.Cancion;


public class ReproduccionEnCurso extends Fragment {

    Cancion miCancion;
    private List<Cancion> misCanciones;
    private ComunicacionAVentanaPricipal comunicacion;

    public ReproduccionEnCurso() {
        // Required empty public constructor
    }

    public ReproduccionEnCurso(Cancion cancion, List<Cancion> canciones) {
        this.miCancion = cancion;
        this.misCanciones = canciones;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            comunicacion = (ComunicacionAVentanaPricipal) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_reproduccion_en_curso, container, false);
        ImageView ivAlbum = vista.findViewById(R.id.ivPortada);
        byte[] img = miCancion.getAlbum().getIlustracion().toByteArray();
        Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
        ivAlbum.setImageBitmap(ilustracion);
        TextView artista = vista.findViewById(R.id.tvArtista);
        TextView album = vista.findViewById(R.id.tvAlbum);
        TextView genero = vista.findViewById(R.id.tvGenero);
        artista.setText(miCancion.getArtista());
        album.setText(miCancion.getAlbum().getNombre());
        genero.setText(miCancion.getGenero());
        if(misCanciones.size()>1){
            misCanciones.remove(miCancion);
            misCanciones.set(0, miCancion);
        }
        if (comunicacion != null){
            comunicacion.CambiarTitulo(miCancion.getNombre());
            comunicacion.ReproducirCanciones(misCanciones);
        }
        return vista;
    }

}
