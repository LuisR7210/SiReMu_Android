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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.*;
import mx.uv.manejoListas.*;

public class VistaPlaylist extends Fragment {
    private ListaReproduccion miPlaylist;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();
    private int idUsuario;
    private int idListaMegusta;

    public VistaPlaylist() {
        // Required empty public constructor
    }

    public VistaPlaylist(ListaReproduccion playlist, int idUsuario, int idListaMegusta) {
        this.miPlaylist = playlist;
        this.idUsuario = idUsuario;
        this.idListaMegusta = idListaMegusta;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_vista_playlist, container, false);
        this.CargarCanciones();
        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), misCanciones);
        ListView lvCanciones=(ListView)vista.findViewById(R.id.lvCanciones);
        lvCanciones.setAdapter(adaptador);
        lvCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReproduccionEnCurso fragment = new ReproduccionEnCurso();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
        lvCanciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new PlaylistSeleccionMultiple(miPlaylist, misCanciones, idUsuario, idListaMegusta);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                return true;
            }
        });
        return vista;
    }

    private void CargarCanciones(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaCanciones canciones = cliente.consultarCancionesLista(IdLista.newBuilder().setId(miPlaylist.getId()).build());
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
}
