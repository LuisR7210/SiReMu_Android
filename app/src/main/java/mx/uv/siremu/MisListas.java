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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoListas.IdUsuario;
import mx.uv.manejoListas.ListaListas;
import mx.uv.manejoListas.ListaReproduccion;
import mx.uv.manejoListas.ManejoListasGrpc;

public class MisListas extends AppCompatActivity {

    private List<ListaReproduccion> misListas = new ArrayList<ListaReproduccion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);
        this.CargarListas();
        MiAdaptador adaptador = new MiAdaptador(this, misListas);
        ListView lvListas=(ListView)findViewById(R.id.listas_reproduccion);
        lvListas.setAdapter(adaptador);
        lvListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), VistaListaReproduccion.class);
                intent.putExtra("Titulo", misListas.get(position).getNombre());
                intent.putExtra("IdLista", misListas.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void CargarListas(){
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            ListaListas listas = cliente.consultarMisListasAgregadas(IdUsuario.newBuilder().setId(1).build());
            misListas.addAll(listas.getListasList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Toast.makeText(this, "Error al conectar al servidor. Inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
        }
    }

    public class MiAdaptador extends ArrayAdapter<ListaReproduccion> {

        private final Activity contexto;
        private final List<ListaReproduccion> misListas;

        public MiAdaptador(Activity contexto, List<ListaReproduccion> listas) {
            super(contexto, R.layout.fila_lista_ilustracion, listas);
            this.contexto=contexto;
            this.misListas=listas;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=contexto.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.fila_lista_ilustracion, null,true);
            TextView principal = (TextView) rowView.findViewById(R.id.tvPrincipal);
            ImageView imagen = (ImageView) rowView.findViewById(R.id.imgIlustracion);
            TextView secundario = (TextView) rowView.findViewById(R.id.tvSecundario);
            TextView tercero = (TextView) rowView.findViewById(R.id.tvTercero);
            principal.setText(misListas.get(position).getNombre());
            if (misListas.get(position).getEsPublica()){
                secundario.setText("Pública");
                tercero.setText("Likes: "+String.valueOf(misListas.get(position).getLikes()));
            } else{
                secundario.setText("Privada");
            }

            byte[] img = misListas.get(position).getIlustracion().toByteArray();
            Bitmap ilustracion = BitmapFactory.decodeByteArray(img, 0, img.length);
            imagen.setImageBitmap(ilustracion);
            return rowView;
        };
    }

    /*private static class GrpcTask extends AsyncTask<String, Void, String> {
        private final WeakReference<Activity> actActual;
        private List<ListaReproduccion> misListas = new ArrayList<ListaReproduccion>();

        private GrpcTask(Activity activity) {
            this.actActual = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(String...params) {
            try {
                ManejoListasGrpc.ManejoListasBlockingStub cliente =
                        ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
                ListaListas listas = cliente.consultarMisListas(IdUsuario.newBuilder().setId(1).build());
                misListas.addAll(listas.getListasList());
            } catch (Exception e) {
                Log.d("myTag", "ErrorLuis: "+e.getMessage());
                Toast.makeText(actActual.get(), "Error al conectar al servidor. Inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
                return "Fallado";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String resultado) {
            Activity activity = actActual.get();
            if (activity == null || misListas == null) {
                return;
            }
            for ( ListaReproduccion lista: misListas) {
                Toast.makeText(activity, lista.getNombre(), Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
