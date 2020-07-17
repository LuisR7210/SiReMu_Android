package mx.uv.siremu_android;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.protobuf.ByteString;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import mx.uv.manejoCanciones.Album;
import mx.uv.manejoCanciones.AlbumAG;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCanciones.RespuestaCanciones;
import mx.uv.manejoListas.ListaAG;
import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.manejoListas.ManejoListasGrpc;
import mx.uv.manejoListas.RespuestaListas;

public class NuevaLista extends Fragment {

    private ComunicacionAVentanaPricipal comunicacion;
    private int idUsuario;
    private int idListaMegusta;
    ImageView ivFoto;
    Button btnSeleccionarImagen;

    Uri imagenUri;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotosApp";
    String CARPETAS_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;

    public NuevaLista() {
        // Required empty public constructor
    }

    public NuevaLista(int idUsuario, int listaMeGusta){
        this.idUsuario = idUsuario;
        this.idListaMegusta = listaMeGusta;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (comunicacion != null) comunicacion.CambiarTitulo("Nueva playlist");
        View vista = inflater.inflate(R.layout.fragment_nueva_lista, container, false);
        ivFoto = vista.findViewById(R.id.ivFoto);
        btnSeleccionarImagen = vista.findViewById(R.id.btnSeleccionarImagen);

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        Button btAgregar = vista.findViewById(R.id.btAgregar);
        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarAlbum();
            }
        });
        return vista;
    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, SELEC_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            ivFoto.setImageURI(imagenUri);
            ivFoto.setImageBitmap(reduceBitmap(requireContext(), imagenUri.toString(),512,512));
        }
    }

    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      int maxAncho, int maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void agregarAlbum() {
        try {
            File imagen = new File(imagenUri.getPath());
            ContentResolver cR = getContext().getContentResolver();
            InputStream inputStream = cR.openInputStream(imagenUri);
            byte[] archivo = IOUtils.toByteArray(inputStream);
            inputStream.close();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String extensionArchivo = "." + mime.getExtensionFromMimeType(cR.getType(imagenUri));
            EditText nombre = getView().findViewById(R.id.etNombreLista);
            EditText descripcion = getView().findViewById(R.id.etDescripcion);
            ListaReproduccion nuevaLista = ListaReproduccion.newBuilder()
                    .setNombre(nombre.getText().toString())
                    .setDescripcion(descripcion.getText().toString())
                    .setEsPublica(true)
                    .setIlustracion(ByteString.copyFrom(archivo))
                    .setExtensionIlustracion(extensionArchivo)
                    .build();
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            RespuestaListas respuesta = cliente.crearListaReproduccion(ListaAG.newBuilder().setIdUsuario(idUsuario).setListaAAgregar(nuevaLista).build());
            if (respuesta.getRespuesta()){
                MisPlaylists fragment = MisPlaylists.newInstance(idUsuario, idListaMegusta);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        } catch (FileNotFoundException io) {
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_archivo), Snackbar.LENGTH_LONG).show();
        } catch (Exception ex){
            Log.d("Error Servidor Listas", ex.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }

    }
}