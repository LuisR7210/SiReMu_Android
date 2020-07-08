package mx.uv.siremu_android;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import mx.uv.manejoListas.*;
import mx.uv.manejoListas.IdUsuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private List<ListaReproduccion> misListasDefault = new ArrayList<ListaReproduccion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fondo, R.id.nav_inicio, R.id.nav_mis_canciones, R.id.nav_playlists, R.id.nav_me_gusta)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

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
        Log.d("", "Num fragmentos" + getFragmentManager().getBackStackEntryCount());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            }
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
            default:
                return true;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        fragment = new Inicio();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(title));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

}
