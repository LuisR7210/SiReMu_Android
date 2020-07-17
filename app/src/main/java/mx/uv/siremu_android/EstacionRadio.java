package mx.uv.siremu_android;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.Cancion;
import mx.uv.manejoCanciones.IdLista;
import mx.uv.manejoCanciones.ListaCanciones;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.siremu_android.adaptadores.AdaptadorCanciones;

public class EstacionRadio extends Fragment {

    private Cancion miCancion;
    private ComunicacionAVentanaPricipal comunicacion;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();
    private int idUsuario;
    private int idListaMegusta;

    public EstacionRadio() {
        // Required empty public constructor
    }

    public EstacionRadio(Cancion cancion, int idUsuario, int idListaMegusta){
        this.miCancion = cancion;
        this.idUsuario = idUsuario;
        this.idListaMegusta = idListaMegusta;
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
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_estacion_radio, container, false);
        if (comunicacion != null) comunicacion.CambiarTitulo("Radio: "+miCancion.getNombre());
        AdaptadorCanciones adaptador = new AdaptadorCanciones(this.getActivity(), misCanciones);
        ListView lvCanciones=(ListView)vista.findViewById(R.id.lvRadio);
        lvCanciones.setAdapter(adaptador);
        lvCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReproduccionEnCurso fragment = new ReproduccionEnCurso(misCanciones.get(position), misCanciones);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("Cancion").commit();
            }
        });
        lvCanciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new PlaylistSeleccionMultiple(misCanciones, idUsuario, idListaMegusta);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("SeleccionCanciones").commit();
                return true;
            }
        });
        return vista;
    }

    private void CargarCanciones(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaCanciones canciones = cliente.generarRadio(miCancion);
            misCanciones.addAll(canciones.getCancionesList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }
}