package mx.uv.siremu_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NuevaCancion extends Fragment {

    private static final String ARG_PARAM1 = "parametro1";
    private int idUsuario;
    Spinner opciones;

    public NuevaCancion() {
        // Required empty public constructor
    }

    public static NuevaCancion newInstance(int idUsuario) {
        NuevaCancion fragment = new NuevaCancion();
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
        View vista = inflater.inflate(R.layout.fragment_nueva_cancion, container, false);
        opciones = vista.findViewById(R.id.generoSP);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.preference_category);
        opciones.setAdapter(adapter);

        opciones = vista.findViewById(R.id.albumSP);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.albums, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.preference_category);
        opciones.setAdapter(adapter1);

        Button nuevoAlbum = vista.findViewById(R.id.nuevoAlbumBT);
        nuevoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NuevoAlbum(idUsuario);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
        return vista;
    }
}