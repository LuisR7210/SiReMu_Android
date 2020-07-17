package mx.uv.siremu_android;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.protobuf.ByteString;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.stub.StreamObserver;
import mx.uv.manejoCanciones.Album;
import mx.uv.manejoCanciones.AlbumAG;
import mx.uv.manejoCanciones.Cancion;
import mx.uv.manejoCanciones.CancionPP;
import mx.uv.manejoCanciones.IdUsuario;
import mx.uv.manejoCanciones.ListaAlbums;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCanciones.RespuestaCanciones;

import static android.app.Activity.RESULT_OK;

public class NuevaCancion extends Fragment {

    private static final String ARG_PARAM1 = "parametro1";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int idUsuario;
    Spinner opciones;
    private List<Album> misAlbums = new ArrayList<Album>();
    Uri cancionUri;
    int SELEC_IMAGEN = 1;

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
        ArrayAdapter<String> adapter1 = generarArrayAlbums();
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

        Button cargarArchivo = vista.findViewById(R.id.subirBT);
        cargarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarArchivo();
            }
        });

        Button agregarCancion = vista.findViewById(R.id.agregarBT);
        agregarCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarCancion();
            }
        });
        return vista;
    }

    private ArrayAdapter<String> generarArrayAlbums(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        try {
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            ListaAlbums listas = cliente.consultarAlbums(IdUsuario.newBuilder().setId(idUsuario).build());
            misAlbums.addAll(listas.getAlbumsList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
        for (Album album: misAlbums) {
            arrayAdapter.add(album.getNombre());
        }
        return arrayAdapter;
    }

    public void seleccionarArchivo() {
        Intent musica = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(musica, SELEC_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELEC_IMAGEN) {
            if(resultCode == RESULT_OK){
                cancionUri = data.getData();
            }
        }
    }

    public void agregarCancion() {
        try {
            Spinner spAlbum = getView().findViewById(R.id.albumSP);
            Album album = buscarAlbum(spAlbum.getSelectedItem().toString());
            EditText nombre = getView().findViewById(R.id.nombreCancionTF);
            EditText artista = getView().findViewById(R.id.artistaTF);
            Spinner genero = getView().findViewById(R.id.generoSP);
            Cancion nuevaCancion = Cancion.newBuilder().setNombre(nombre.getText().toString())
                    .setGenero(genero.getSelectedItem().toString())
                    .setAlbum(Album.newBuilder().setId(album.getId()).setNombre(album.getNombre()).build())
                    .setDuracion("05:23")
                    .setArtista(artista.getText().toString())
                    .setEsPublica(true)
                    .build();
            ContentResolver cR = getContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String extensionArchivo = "." + mime.getExtensionFromMimeType(cR.getType(cancionUri));
            CancionPP datosCancion = CancionPP.newBuilder().setExtensionarchivo(extensionArchivo).setIdUsuario(idUsuario).setCancion(nuevaCancion).build();
            ManejoCancionesGrpc.ManejoCancionesStub cliente = ManejoCancionesGrpc.newStub(Canales.getCanalCanciones());
            subirCancion(cliente, datosCancion);
            Fragment fragment = MisAlbums.newInstance(idUsuario);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        } catch (Exception ex){
            Log.d("TAG", ex.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    private void subirCancion(ManejoCancionesGrpc.ManejoCancionesStub asyncStub, CancionPP datosCancion)
            throws InterruptedException, RuntimeException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<RespuestaCanciones> responseObserver = new StreamObserver<RespuestaCanciones>() {
            @Override
            public void onNext(RespuestaCanciones summary) {
            }

            @Override
            public void onError(Throwable t) {
                Log.d("Error Servidor Canciones: ", ""+t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                Log.d("Cancion subida con éxito: ", "Si!!!!!!!");
                finishLatch.countDown();
            }
        };

        StreamObserver<CancionPP> requestObserver = asyncStub.subirCancion(responseObserver);
        try {
            requestObserver.onNext(datosCancion);
            ContentResolver cR = getContext().getContentResolver();
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
            InputStream inputStream = cR.openInputStream(cancionUri);
            byte[] pedazo = new byte[512];
            int tamPedazo = 0;
            while((tamPedazo = inputStream.read(pedazo)) != -1){
                requestObserver.onNext(CancionPP.newBuilder().setContenido(ByteString.copyFrom(pedazo)).build());
                if (finishLatch.getCount() == 0) {
                    // RPC completed or errored before we finished sending.
                    // Sending further requests won't error, but they will just be thrown away.
                    break;
                }
            }
            inputStream.close();
        } catch (RuntimeException | IOException e) {
            // Cancel RPC
            requestObserver.onError(e);
            Log.d("Error dsadadasdsada: ", "No"+e.getMessage());
        }
        // Mark the end of requests
        requestObserver.onCompleted();
        // Receiving happens asynchronously
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            throw new RuntimeException(
                    "Could not finish rpc within 1 minute, the server is likely down");
        }
    }


    private Album buscarAlbum(String nombre){
        for (Album album: misAlbums) {
            if (album.getNombre().equals(nombre)){
                return album;
            }
        }
        return null;
    }
}