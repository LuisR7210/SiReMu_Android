package mx.uv.siremu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoListas.Cancion;
import mx.uv.manejoListas.IdUsuario;
import mx.uv.manejoListas.ListaListas;
import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.manejoListas.ManejoListasGrpc;

public class VistaListaReproduccion extends AppCompatActivity {

    private int idLista;
    private final List<Cancion> misCanciones = new ArrayList<Cancion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproduccion);
        Intent intent = getIntent();
        this.setTitle(intent.getStringExtra("Titulo"));
        idLista = intent.getIntExtra("IdLista", 0);

    }

    private void CargarCanciones(){
        try {
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Toast.makeText(this, "Error al conectar al servidor. Inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
        }
    }

    public class MiAdaptador extends ArrayAdapter<Cancion> {

        private final Activity contexto;
        private final List<Cancion> misCanciones;

        public MiAdaptador(Activity contexto, List<Cancion> canciones) {
            super(contexto, R.layout.fila_lista_ilustracion, canciones);
            this.contexto=contexto;
            this.misCanciones=canciones;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_cancion, null,true);
            TextView principal = (TextView) rowView.findViewById(R.id.tvPrincipal);
            TextView secundario = (TextView) rowView.findViewById(R.id.tvSecundario);
            TextView tercero = (TextView) rowView.findViewById(R.id.tvTercero);
            principal.setText(misCanciones.get(position).getNombre());
            secundario.setText(misCanciones.get(position).getArtista());
            return rowView;
        };
    }
}
