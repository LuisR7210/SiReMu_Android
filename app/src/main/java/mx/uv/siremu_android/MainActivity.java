package mx.uv.siremu_android;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
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
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.grpc.ManagedChannel;
import mx.uv.manejoCanciones.Cancion;
import mx.uv.manejoCanciones.CancionAG;
import mx.uv.manejoCanciones.CancionPP;
import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCuentas.Usuario;
import mx.uv.manejoListas.*;
import mx.uv.manejoListas.IdUsuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ComunicacionAVentanaPricipal{

    private AppBarConfiguration mAppBarConfiguration;
    private List<ListaReproduccion> misListasDefault = new ArrayList<ListaReproduccion>();
    private Usuario miUsuario;
    private List<Cancion> misCanciones = new ArrayList<Cancion>();
    private int cancionActual;
    private MediaPlayer mediaPlayer;
    private ImageButton btPlay;
    private ImageButton btAnterior;
    private ImageButton btSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", 0);
        String nombreUsuario = intent.getStringExtra("nombreUsuario");
        miUsuario = Usuario.newBuilder().setId(idUsuario).setUsuario(nombreUsuario).build();
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
        this.CargarMisListasDefault();
        Fragment fragment = MisCanciones.newInstance(miUsuario.getId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        btPlay = this.findViewById(R.id.ibt_play);
        btAnterior = this.findViewById(R.id.ibt_anterior);
        btSiguiente = this.findViewById(R.id.ibt_siguiente);
        btPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PausarCancion();
            }
        });
        btAnterior.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ReproducirAnterior();
            }
        });
        btSiguiente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ReproducirSiguiente();
            }
        });
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
                fragment = Inicio.newInstance(miUsuario.getId());
                break;
            case R.id.nav_buscar:
                title = R.string.menu_buscar;
                fragment = Busqueda.newInstance(miUsuario.getId(), listaDefault.getId());
                break;
            case R.id.nav_mis_canciones:
                title = R.string.menu_mis_canciones;
                fragment = MisCanciones.newInstance(miUsuario.getId());
                break;
            case R.id.nav_playlists:
                title = R.string.menu_playlists;
                fragment = MisPlaylists.newInstance(miUsuario.getId(), listaDefault.getId());
                break;
            case R.id.nav_me_gusta:
                title = R.string.menu_me_gusta;
                fragment = new VistaPlaylist(listaDefault, miUsuario.getId(), listaDefault.getId());
                break;
            case R.id.nav_historial:
                title = R.string.menu_historial;
                listaDefault = this.BuscarListaDefault(getText(R.string.menu_historial).toString());
                fragment = new VistaPlaylist(listaDefault, miUsuario.getId(), listaDefault.getId());
                break;
            case R.id.nav_albumes:
                title = R.string.menu_albumes;
                fragment = MisAlbums.newInstance(miUsuario.getId());
                break;
            case R.id.nav_subir_cancion:
                title = R.string.menu_subir_cancion;
                fragment = NuevaCancion.newInstance(miUsuario.getId());
                break;
            default:
                return true;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
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
            ListaListas listas = cliente.consultarMisListasDefault(IdUsuario.newBuilder().setId(miUsuario.getId()).build());
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

    @Override
    public void ReproducirCanciones(List<Cancion> canciones){
        DetenerCancion();
        cancionActual = 0;
        misCanciones = canciones;
        ReproducirCancion(misCanciones.get(0));
    }

    private void PausarCancion(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                btPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else{
                mediaPlayer.start();
                btPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            }
        }
    }

    private void ReproducirSiguiente(){
        if (cancionActual + 1 >= misCanciones.size()){
            return;
        }
        DetenerCancion();
        cancionActual++;
        ReproducirCancion(misCanciones.get(cancionActual));
    }

    private void ReproducirAnterior(){
        if (cancionActual -1 < 0){
            return;
        }
        DetenerCancion();
        cancionActual--;
        ReproducirCancion(misCanciones.get(cancionActual));
    }

    private void DetenerCancion(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void ReproducirCancion(Cancion cancion){
        File cancionTemporal = null;
        FileOutputStream fos = null;
        ManejoCancionesGrpc.ManejoCancionesBlockingStub stub = ManejoCancionesGrpc.newBlockingStub(Canales.getCanalCanciones());
        CancionAG request = CancionAG.newBuilder().setIdUsuario(miUsuario.getId()).setCancion(cancion).build();
        Iterator<CancionPP> pedazos;
        try {
            pedazos = stub.reproducirCancion(request);
            while(pedazos.hasNext()){
                CancionPP pedazo = pedazos.next();
                if (pedazo.hasCancion()){
                    cancionTemporal = File.createTempFile("temporal", pedazo.getExtensionarchivo(), getCacheDir());
                    cancionTemporal.deleteOnExit();
                    fos = new FileOutputStream(cancionTemporal);
                }
                fos.write(pedazo.getContenido().toByteArray());
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(cancionTemporal.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            btPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            btAnterior.setVisibility(View.VISIBLE);
            btSiguiente.setVisibility(View.VISIBLE);
        } catch (Exception e){
            Log.d("myTag", "Error Servidor Canciones: "+e.getMessage());
            Snackbar.make(this.findViewById(R.id.drawer_layout), getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }
}

interface ComunicacionAVentanaPricipal {
    void CambiarTitulo(String titulo);
    void ReproducirCanciones(List<Cancion> canciones);
}
