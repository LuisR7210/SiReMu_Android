package mx.uv.siremu_android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoCanciones.*;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisCanciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisCanciones extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "parametro1";

    private int idUsuario;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();

    public MisCanciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisCanciones.
     */
    // TODO: Rename and change types and number of parameters
    public static MisCanciones newInstance(int idUsuario) {
        MisCanciones fragment = new MisCanciones();
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
        View vista = inflater.inflate(R.layout.fragment_mis_canciones, container, false);
        this.CargarCanciones();
        MiAdaptador adaptador = new MiAdaptador(this.getActivity(), misCanciones);
        ListView lvCanciones=(ListView)vista.findViewById(R.id.lvCanciones);
        lvCanciones.setAdapter(adaptador);
        return vista;
    }

    private void CargarCanciones(){
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaCanciones canciones = cliente.consultarMisCanciones(IdUsuario.newBuilder().setId(idUsuario).build());
            misCanciones.addAll(canciones.getCancionesList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            //Toast.makeText(this, "Error al conectar al servidor. Inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
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
            principal.setText(misCanciones.get(position).getNombre());
            secundario.setText(misCanciones.get(position).getArtista());
            tercero.setText(misCanciones.get(position).getDuracion());
            ImageView imagen = (ImageView) rowView.findViewById(R.id.imgIlustracion);
            byte[] img = misCanciones.get(position).getAlbum().getIlustracion().toByteArray();
            Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
            imagen.setImageBitmap(ilustracion);
            return rowView;
        };
    }
}
