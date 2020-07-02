package mx.uv.siremu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class crear_album extends AppCompatActivity {

    ImageView ivFoto;
    Button btnSeleccionarImagen;

    Uri imagenUri;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotosApp";
    String CARPETAS_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_album);

        ivFoto = findViewById(R.id.ivFoto);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);

        if(ContextCompat.checkSelfPermission(crear_album.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(crear_album.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, SELEC_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            ivFoto.setImageURI(imagenUri);
            ivFoto.setImageBitmap(reduceBitmap(this, imagenUri.toString(), 512,      512));
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
            Toast.makeText(contexto, "Fichero/recurso no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    public void agregarAlbum(View view) {
        boolean validacion = ValidarImagen();
        if(validacion){
            Toast toast = Toast.makeText(this, "validacion exitosa", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(this, "error en la validacion", Toast.LENGTH_SHORT);
            toast.show();
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
            if(c=='j' && b1==true){
                b2=true;
            }
            if(c=='p' && b2==true){
                b3=true;
            }
            if(c=='g' && b3==true){
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
