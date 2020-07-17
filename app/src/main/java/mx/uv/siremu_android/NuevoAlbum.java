package mx.uv.siremu_android;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.protobuf.ByteString;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

import javax.xml.transform.Result;

import mx.uv.manejoCanciones.Album;
import mx.uv.manejoCanciones.AlbumAG;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCanciones.RespuestaCanciones;

public class NuevoAlbum extends Fragment {

    private int idUsuario;
    private ComunicacionAVentanaPricipal comunicacion;
    ImageView ivFoto;
    Button btnSeleccionarImagen;

    Uri imagenUri;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotosApp";
    String CARPETAS_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;
    String fecha;

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public NuevoAlbum(){

    }

    public NuevoAlbum(int idUsuario){
        this.idUsuario = idUsuario;
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
        if (comunicacion != null) comunicacion.CambiarTitulo("Nuevo Album");
        View vista = inflater.inflate(R.layout.fragment_nuevo_album, container, false);

        mDisplayDate = vista.findViewById(R.id.fechaTP);

        ivFoto = vista.findViewById(R.id.ivFoto);
        btnSeleccionarImagen = vista.findViewById(R.id.btnSeleccionarImagen);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                fecha = year + "-" + month + "-" + day;
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

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
            EditText nombre = getView().findViewById(R.id.nombreAlbumTF);
            EditText compania = getView().findViewById(R.id.companiaTF);
            Album nuevoAlbum = Album.newBuilder()
                    .setNombre(nombre.getText().toString())
                    .setFechaLanzamiento(fecha)
                    .setIlustracion(ByteString.copyFrom(archivo))
                    .setCompania(compania.getText().toString())
                    .setExtensionIlustracion(extensionArchivo)
                    .build();
            ManejoCancionesGrpc.ManejoCancionesBlockingStub cliente =
                    ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
            RespuestaCanciones respuesta = cliente.crearAlbum(AlbumAG.newBuilder().setIdUsuario(idUsuario).setAlbum(nuevoAlbum).build());
            if (respuesta.getRespuesta()){
                MisAlbums fragment = MisAlbums.newInstance(idUsuario);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        } catch (FileNotFoundException io) {
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_archivo), Snackbar.LENGTH_LONG).show();
        } catch (Exception ex){
            Log.d(TAG, ex.getMessage());
            Snackbar.make(this.getActivity().findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }

    }

    private boolean ValidarImagen() {
        File fs = new File(imagenUri.toString());
        String ex=imagenUri.toString();
        char[] characters=ex.toCharArray();
        boolean b1 = false;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 =false;
        for(char c:characters){
            if(c=='.'){
                b1=true;
            }
            if(c=='j' && b1){
                b2=true;
            }
            if(c=='p' && b2){
                b3=true;
            }
            if(c=='g' && b3){
                b4=true;
            }
        }
        if(b1 && b2 && b3 && b4){
            return  true;
        }else {
            return false;
        }

    }
}