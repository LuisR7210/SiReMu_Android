package mx.uv.siremu_android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.*;

public class VistaAlbum extends Fragment {

    private Album miAlbum;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();
    private int idUsuario;
    private ComunicacionAVentanaPricipal comunicacion;

    public VistaAlbum() {
        // Required empty public constructor
    }

    public VistaAlbum(Album album, int idUsuario) {
        this.miAlbum = album;
        this.idUsuario = idUsuario;
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
        this.CargarCanciones();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (comunicacion != null) comunicacion.CambiarTitulo(miAlbum.getNombre());
        View vista = inflater.inflate(R.layout.fragment_vista_album, container, false);
        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), misCanciones);
        ListView lvCanciones = vista.findViewById(R.id.lvCanciones);
        lvCanciones.setAdapter(adaptador);
        lvCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        lvCanciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
        return vista;
    }

    private void CargarCanciones(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaCanciones canciones = cliente.consultarCancionesAlbum(Album.newBuilder().setId(miAlbum.getId()).build());
            misCanciones.addAll(canciones.getCancionesList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private class MiAdaptador extends ArrayAdapter<Cancion> {

        private final Activity contexto;
        private final List<Cancion> misCanciones;

        public MiAdaptador(Activity contexto, List<Cancion> canciones) {
            super(contexto, R.layout.fila_lista_ilustracion, canciones);
            this.contexto=contexto;
            this.misCanciones=canciones;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_cancion, null,true);
            TextView principal = rowView.findViewById(R.id.tvPrincipal);
            TextView secundario = rowView.findViewById(R.id.tvSecundario);
            TextView tercero = rowView.findViewById(R.id.tvTercero);
            principal.setText(misCanciones.get(position).getNombre());
            secundario.setText(misCanciones.get(position).getArtista());
            tercero.setText(misCanciones.get(position).getDuracion());
            return rowView;
        };
    }
}