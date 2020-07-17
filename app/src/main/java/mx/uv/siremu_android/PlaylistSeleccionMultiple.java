package mx.uv.siremu_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoListas.*;
import mx.uv.manejoCanciones.*;
import mx.uv.manejoListas.IdUsuario;
import mx.uv.siremu_android.adaptadores.FilaSeleccionable;

public class PlaylistSeleccionMultiple extends Fragment {
    private ListaReproduccion miPlaylist;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();
    private int idUsuario;
    private int idListaMegusta;
    private List<Integer> cancionesSeleccionadas = new ArrayList<>();
    private List<ListaReproduccion> misListas = new ArrayList<ListaReproduccion>();

    public PlaylistSeleccionMultiple() {
        // Required empty public constructor
    }

    public PlaylistSeleccionMultiple(List<Cancion> canciones, int idUsuario, int idListaMegusta) {
        this.misCanciones = canciones;
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
        final View vista = inflater.inflate(R.layout.fragment_playlist_seleccion_multiple, container, false);
        List<FilaSeleccionable> filasCanciones = new ArrayList<>();
        for (Cancion cancion : misCanciones) {
            filasCanciones.add(new FilaSeleccionable(cancion.getNombre(), cancion.getArtista(), cancion.getDuracion()));
        }
        final ListView lvCanciones=(ListView)vista.findViewById(R.id.lvBuscados);
        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), filasCanciones, lvCanciones);
        lvCanciones.setAdapter(adaptador);
        ImageButton btRadio = vista.findViewById(R.id.ibtGenerarEstacion);
        btRadio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                VerEstacionRadio();
            }
        });
        ImageButton btMeGusta = vista.findViewById(R.id.ibtMeGusta);
        btMeGusta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AgregarCancionesALista(idListaMegusta);
            }
        });
        ImageButton btAgregarAPlaylists = vista.findViewById(R.id.ibtAgregarPlaylists);
        btAgregarAPlaylists.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final ArrayAdapter<String> adaptador = CargarAdaptadorListas();
                new AlertDialog.Builder(getContext())
                        .setTitle("Agregar a:")
                        .setNegativeButton("Cancelar", null)
                        .setAdapter(adaptador, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int idLista = BuscarLista(adaptador.getItem(which));
                                AgregarCancionesALista(idLista);
                            }
                        })
                        .show();
            }
        });
        return vista;
    }

    private int BuscarLista(String nombre){
        for (ListaReproduccion lista : misListas) {
            if (lista.getNombre().contentEquals(nombre)){
                return lista.getId();
            }
        }
        return 1;
    }

    private List<Integer> ObtenerCancionesSeleccionadas(){
        List<Integer> listaCanciones = new ArrayList<>();
        ListView lvCanciones = this.getView().findViewById(R.id.lvBuscados);
        SparseBooleanArray checked = lvCanciones.getCheckedItemPositions();
        for (int i = 0; i < lvCanciones.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                listaCanciones.add(misCanciones.get(i).getId());
            }
        }
        return listaCanciones;
    }

    private void AgregarCancionesALista(int idLista){
        List<Integer> listaCanciones = new ArrayList<>();
        listaCanciones = this.ObtenerCancionesSeleccionadas();
        if (listaCanciones.size() == 0){
            return;
        }
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente = ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            RespuestaListas respuesta = cliente.agregarQuitarCancionesLista(
                    ListaAA.newBuilder().setAgregar(true).setIdLista(idLista).setIdUsuario(idUsuario).addAllIdCanciones(listaCanciones).build());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), "Canciones Añadidas", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private ArrayAdapter<String> CargarAdaptadorListas(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            ListaListas listas = cliente.consultarMisListas(IdUsuario.newBuilder().setId(idUsuario).build());
            misListas.addAll(listas.getListasList());
            for (ListaReproduccion lista : listas.getListasList()) {
                arrayAdapter.add(lista.getNombre());
            }
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
        return arrayAdapter;
    }

    private void VerEstacionRadio(){
        ListView lvCanciones = this.getView().findViewById(R.id.lvBuscados);
        SparseBooleanArray checked = lvCanciones.getCheckedItemPositions();
        for (int i = 0; i < lvCanciones.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                Fragment fragment = new EstacionRadio(misCanciones.get(i), idUsuario, idListaMegusta);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                return;
            }
        }
    }

    private class MiAdaptador extends ArrayAdapter<FilaSeleccionable> {

        private final Activity contexto;
        private final List<FilaSeleccionable> misCanciones;
        private final ListView miListView;

        public MiAdaptador(Activity contexto, List<FilaSeleccionable> canciones, ListView listView) {
            super(contexto, R.layout.fila_lista_cancion_seleccion, canciones);
            this.contexto=contexto;
            this.misCanciones=canciones;
            this.miListView = listView;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_cancion_seleccion, null,true);
            TextView principal = (TextView) rowView.findViewById(R.id.tvPrincipal);
            TextView secundario = (TextView) rowView.findViewById(R.id.tvSecundario);
            TextView tercero = (TextView) rowView.findViewById(R.id.tvTercero);
            CheckBox checkBox = (CheckBox)rowView.findViewById(R.id.cb_Seleccion);
            principal.setText(misCanciones.get(position).getTitulo());
            secundario.setText(misCanciones.get(position).getSubtitulo());
            tercero.setText(misCanciones.get(position).getExtra());
            checkBox.setChecked(misCanciones.get(position).isSeleccionado());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked)
                {
                    misCanciones.get(position).setSeleccionado(isChecked);
                    miListView.setItemChecked(position, isChecked);
                }
            });
            return rowView;
        };

        public List<FilaSeleccionable> getMisCanciones() {
            return misCanciones;
        }
    }
}
