package mx.uv.siremu_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.Cancion;
import mx.uv.manejoCanciones.IdLista;
import mx.uv.manejoCanciones.ListaCanciones;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCanciones.NombreCancion;
import mx.uv.manejoListas.IdUsuario;
import mx.uv.manejoListas.ListaListas;
import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.manejoListas.ManejoListasGrpc;
import mx.uv.manejoListas.NombreLista;
import mx.uv.siremu_android.adaptadores.AdaptadorCanciones;
import mx.uv.siremu_android.adaptadores.AdaptadorListas;

public class Busqueda extends Fragment {

    private List<ListaReproduccion> misListas = new ArrayList<ListaReproduccion>();
    private List<Cancion> misCanciones = new ArrayList<Cancion>();

    private static final String ARG_PARAM1 = "parametro1";
    private static final String ARG_PARAM2 = "parametro2";
    private int idUsuario;
    private int idListaMegusta;
    private String elementosABuscar = "Canciones";
    private String busqueda;

    public Busqueda() {
        // Required empty public constructor
    }

    public static Busqueda newInstance(int idUsuario, int listaMeGusta) {
        Busqueda fragment = new Busqueda();
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
        final View vista = inflater.inflate(R.layout.fragment_busqueda, container, false);

        EditText etBusqueda = vista.findViewById(R.id.etBusqueda);
        etBusqueda.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EmpezarBusqueda();
                    return true;
                }
                return false;
            }
        });
        ImageButton fab = vista.findViewById(R.id.ibtBuscar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmpezarBusqueda();
            }
        });
        Button btCanciones = vista.findViewById(R.id.btCanciones);
        btCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementosABuscar = "Canciones";
                EmpezarBusqueda();
            }
        });
        Button btListas = vista.findViewById(R.id.btListas);
        btListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementosABuscar = "Listas";
                EmpezarBusqueda();
            }
        });
        return vista;
    }

    private void EmpezarBusqueda(){
        EditText etBusqueda = getView().findViewById(R.id.etBusqueda);
        busqueda = etBusqueda.getText().toString();
        if(busqueda.contentEquals("")){
            return;
        }
        switch (elementosABuscar){
            case "Canciones":
                misCanciones.clear();
                this.CargarCanciones();
                break;
            case "Listas":
                misListas.clear();
                this.CargarListas();
                break;
        }
    }

    private void CargarCanciones(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaCanciones canciones = cliente.buscarCanciones(NombreCancion.newBuilder().setNombre(busqueda).build());
            misCanciones.addAll(canciones.getCancionesList());
            AdaptadorCanciones adaptador = new AdaptadorCanciones(this.getActivity(), misCanciones);
            ListView lvBuscados = getView().findViewById(R.id.lvBuscados);
            lvBuscados.setAdapter(adaptador);
            lvBuscados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ReproduccionEnCurso fragment = new ReproduccionEnCurso(misCanciones.get(position));
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("Cancion").commit();
                }
            });
            lvBuscados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = new PlaylistSeleccionMultiple(misCanciones, idUsuario, idListaMegusta);
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("SeleccionCanciones").commit();
                    return true;
                }
            });
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private void CargarListas(){
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            ListaListas listas = cliente.buscarListas(NombreLista.newBuilder().setNombre(busqueda).build());
            misListas.addAll(listas.getListasList());
            AdaptadorListas adaptador = new AdaptadorListas(this.getActivity(), misListas);
            ListView lvBuscados = getView().findViewById(R.id.lvBuscados);
            lvBuscados.setAdapter(adaptador);
            lvBuscados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    VistaPlaylist fragment = new VistaPlaylist(misListas.get(position), idUsuario, idListaMegusta);
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                }
            });
            lvBuscados.setOnItemLongClickListener(null);
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }
}