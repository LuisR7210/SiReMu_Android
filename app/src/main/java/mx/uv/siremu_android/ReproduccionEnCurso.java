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

import mx.uv.manejoCanciones.Cancion;


public class ReproduccionEnCurso extends Fragment {

    Cancion miCancion;
    private ComunicacionAVentanaPricipal comunicacion;

    public ReproduccionEnCurso() {
        // Required empty public constructor
    }

    public ReproduccionEnCurso(Cancion cancion) {
        this.miCancion = cancion;
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
        // Inflate the layout for this fragment
        if (comunicacion != null) comunicacion.CambiarTitulo(miCancion.getNombre());
        View vista = inflater.inflate(R.layout.fragment_reproduccion_en_curso, container, false);
        ImageView ivAlbum = vista.findViewById(R.id.ivPortada);
        byte[] img = miCancion.getAlbum().getIlustracion().toByteArray();
        Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
        ivAlbum.setImageBitmap(ilustracion);
        return vista;
    }

}
