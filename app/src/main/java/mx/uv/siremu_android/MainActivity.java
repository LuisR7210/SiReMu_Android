package mx.uv.siremu_android;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.grpc.ManagedChannel;
import mx.uv.manejoCanciones.CancionAG;
import mx.uv.manejoCanciones.CancionPP;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoListas.*;
import mx.uv.manejoListas.IdUsuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ComunicacionAVentanaPricipal{

    private AppBarConfiguration mAppBarConfiguration;
    private List<ListaReproduccion> misListasDefault = new ArrayList<ListaReproduccion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", 0);
        String nombreUsuario = intent.getStringExtra("nombreUsuario");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fondo, R.id.nav_inicio, R.id.nav_buscar, R.id.nav_mis_canciones,
                R.id.nav_playlists, R.id.nav_me_gusta, R.id.nav_albumes, R.id.nav_subir_cancion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        TextView tvUsuario = header.findViewById(R.id.tvUsuario);
        tvUsuario.setText(nombreUsuario);

        ImageButton play = findViewById(R.id.ibt_play);
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Play prrross", Toast.LENGTH_LONG).show();
            }
        });
        this.CargarMisListasDefault();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d("", "Num fragmentos" + getSupportFragmentManager().getBackStackEntryCount());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                return;
            } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            }
            super.onBackPressed();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int title;
        ListaReproduccion listaDefault = this.BuscarListaDefault(getText(R.string.menu_me_gusta).toString());
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.nav_inicio:
                title = R.string.menu_home;
                fragment = Inicio.newInstance(1);
                break;
            case R.id.nav_buscar:
                title = R.string.menu_buscar;
                fragment = Busqueda.newInstance("", "");
                break;
            case R.id.nav_mis_canciones:
                title = R.string.menu_mis_canciones;
                fragment = MisCanciones.newInstance(1);
                break;
            case R.id.nav_playlists:
                title = R.string.menu_playlists;
                fragment = MisPlaylists.newInstance(1, listaDefault.getId());
                break;
            case R.id.nav_me_gusta:
                title = R.string.menu_me_gusta;
                fragment = new VistaPlaylist(listaDefault, 1, listaDefault.getId());
                break;
            case R.id.nav_historial:
                title = R.string.menu_historial;
                listaDefault = this.BuscarListaDefault(getText(R.string.menu_historial).toString());
                fragment = new VistaPlaylist(listaDefault, 1, listaDefault.getId());
                break;
            case R.id.nav_albumes:
                title = R.string.menu_albumes;
                fragment = MisAlbums.newInstance(1);
                break;
            case R.id.nav_subir_cancion:
                title = R.string.menu_subir_cancion;
                fragment = NuevaCancion.newInstance(1);
                break;
            default:
                return true;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        fragment = new Inicio();
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(getString(title));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        getSupportFragmentManager().popBackStack();
        return true;
    }

    private ListaReproduccion BuscarListaDefault(String nombre){
        ListaReproduccion listaDefault = null;
        for (ListaReproduccion listaReproduccion : misListasDefault) {
            if (listaReproduccion.getNombre().contentEquals(nombre)){
                listaDefault = listaReproduccion;
                break;
            }
        }
        return listaDefault;
    }

    private void CargarMisListasDefault(){
        try {
            ManejoListasGrpc.ManejoListasBlockingStub cliente =
                    ManejoListasGrpc.newBlockingStub(Canales.getCanalListas());
            ListaListas listas = cliente.consultarMisListasDefault(IdUsuario.newBuilder().setId(1).build());
            misListasDefault.addAll(listas.getListasList());
        } catch (Exception e) {
            Log.d("myTag", "Error Servidor Listas: "+e.getMessage());
            Snackbar.make(this.findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void CambiarTitulo(String titulo) {
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(titulo);
    }

    private void ReproducirCancion(){
    }

    private static class GrpcTask extends AsyncTask<String, Void, String> {
        private final WeakReference<Activity> activityReference;

        private GrpcTask(Activity activity) {
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String message = params[1];
            String portStr = params[2];
            int idUsuario = TextUtils.isEmpty(id) ? 0 : Integer.parseInt(id);
            try {
                ManejoCancionesGrpc.ManejoCancionesBlockingStub stub = ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
                CancionAG request = CancionAG.newBuilder().setIdUsuario(idUsuario).build();
                Iterator<CancionPP> pedazos;
                pedazos = stub.reproducirCancion(request);
                while(pedazos.hasNext()){
//                    File cancionTemporal = File.createTempFile("kurchina", "mp3", getCacheDir());
//                    tempMp3.deleteOnExit();
//                    FileOutputStream fos = new FileOutputStream(tempMp3);
//                    fos.write(mp3SoundByteArray);
                }
                return "Listo";
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}

interface ComunicacionAVentanaPricipal {
    void CambiarTitulo(String titulo);
}
