package mx.uv.siremu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import mx.uv.manejoListas.IdUsuario;
import mx.uv.manejoListas.ListaListas;
import mx.uv.manejoListas.ManejoListasGrpc;

public class CancionEnCurso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_en_curso);
    }


}
