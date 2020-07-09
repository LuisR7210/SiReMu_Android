package mx.uv.siremu_android;

import android.app.Activity;
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
import mx.uv.manejoListas.ListaReproduccion;

public class MisAlbums extends Fragment {

    private List<Album> misAlbums = new ArrayList<Album>();

    private static final String ARG_PARAM1 = "parametro1";
    private int idUsuario;

    public MisAlbums() {
        // Required empty public constructor
    }

    public static MisAlbums newInstance(int idUsuario) {
        MisAlbums fragment = new MisAlbums();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idUsuario = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mis_albums, container, false);
        this.CargarAlbums();

        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), misAlbums);
        ListView lvAlbums = vista.findViewById(R.id.lista_albums);
        lvAlbums.setAdapter(adaptador);
        lvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VistaAlbum fragment = new VistaAlbum(misAlbums.get(position), idUsuario);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });

        return vista;
    }

    private void CargarAlbums(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaAlbums listas = cliente.consultarAlbums(IdUsuario.newBuilder().setId(idUsuario).build());
            misAlbums.addAll(listas.getAlbumsList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private class MiAdaptador extends ArrayAdapter<Album> {

        private final Activity contexto;
        private final List<Album> misAlbums;

        public MiAdaptador(Activity contexto, List<Album> albums) {
            super(contexto, R.layout.fila_lista_ilustracion, albums);
            this.contexto=contexto;
            this.misAlbums=albums;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_ilustracion, null,true);
            TextView principal = rowView.findViewById(R.id.tvPrincipal);
            ImageView imagen = rowView.findViewById(R.id.imgIlustracion);
            TextView secundario = rowView.findViewById(R.id.tvSecundario);
            TextView tercero = rowView.findViewById(R.id.tvTercero);
            principal.setText(misAlbums.get(position).getNombre());
            secundario.setText(misAlbums.get(position).getCompania());
            tercero.setText(misAlbums.get(position).getFechaLanzamiento());
            byte[] img = misAlbums.get(position).getIlustracion().toByteArray();
            Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
            imagen.setImageBitmap(ilustracion);
            return rowView;
        }
    }
}