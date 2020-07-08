package mx.uv.siremu_android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoListas.IdUsuario;
import mx.uv.manejoListas.ListaListas;
import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.manejoListas.ManejoListasGrpc;

public class MisPlaylists extends Fragment {

    private List<ListaReproduccion> misListas = new ArrayList<ListaReproduccion>();

    private static final String ARG_PARAM1 = "parametro1";
    private static final String ARG_PARAM2 = "parametro2";
    private int idUsuario;
    private int idListaMegusta;

    public MisPlaylists() {
        // Required empty public constructor
    }

    public static MisPlaylists newInstance(int idUsuario, int listaMeGusta) {
        MisPlaylists fragment = new MisPlaylists();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idUsuario);
        args.putInt(ARG_PARAM2, listaMeGusta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idUsuario = getArguments().getInt(ARG_PARAM1);
            this.idListaMegusta = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mis_playlists, container, false);
        this.CargarListas();
        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), misListas);
        ListView lvListas=(ListView)vista.findViewById(R.id.listas_reproduccion);
        lvListas.setAdapter(adaptador);
        lvListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VistaPlaylist fragment = new VistaPlaylist(misListas.get(position), idUsuario, idListaMegusta);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
        FloatingActionButton fab = vista.findViewById(R.id.bt_nuevo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
        return vista;
    }

    private void CargarListas(){
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            ListaListas listas = cliente.consultarMisListas(IdUsuario.newBuilder().setId(idUsuario).build());
            misListas.addAll(listas.getListasList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private class MiAdaptador extends ArrayAdapter<ListaReproduccion> {

        private final Activity contexto;
        private final List<ListaReproduccion> misListas;

        public MiAdaptador(Activity contexto, List<ListaReproduccion> listas) {
            super(contexto, R.layout.fila_lista_ilustracion, listas);
            this.contexto=contexto;
            this.misListas=listas;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_ilustracion, null,true);
            TextView principal = (TextView) rowView.findViewById(R.id.tvPrincipal);
            ImageView imagen = (ImageView) rowView.findViewById(R.id.imgIlustracion);
            TextView secundario = (TextView) rowView.findViewById(R.id.tvSecundario);
            TextView tercero = (TextView) rowView.findViewById(R.id.tvTercero);
            principal.setText(misListas.get(position).getNombre());
            if (misListas.get(position).getEsPublica()){
                secundario.setText("Pública");
                tercero.setText("Likes: "+String.valueOf(misListas.get(position).getLikes()));
            } else{
                secundario.setText("Privada");
            }
            byte[] img = misListas.get(position).getIlustracion().toByteArray();
            Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
            imagen.setImageBitmap(ilustracion);
            return rowView;
        };
    }
}
