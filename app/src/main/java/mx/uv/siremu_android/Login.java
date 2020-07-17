package mx.uv.siremu_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import mx.uv.manejoCanciones.ManejoCancionesGrpc;
import mx.uv.manejoCuentas.ManejoCuentasGrpc;
import mx.uv.manejoCuentas.RespuestaCuentas;
import mx.uv.manejoCuentas.Usuario;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btLogin = findViewById(R.id.btIniciarSesion);
        btLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IniciarSesion(v);
            }
        });
    }

    private void IniciarSesion(View vista){
        EditText usuario = findViewById(R.id.etBusqueda);
        EditText contraseña = findViewById(R.id.etContraseña);
        try {
            ManejoCuentasGrpc.ManejoCuentasBlockingStub cliente = ManejoCuentasGrpc.newBlockingStub(Canales.getCanalCuentas());
            System.out.println(usuario.getText().toString());
            RespuestaCuentas respuesta = cliente.iniciarSesion(Usuario.newBuilder().setUsuario(usuario.getText().toString()).setContrasenia(contraseña.getText().toString()).build());
            if (respuesta.getRespuesta()){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idUsuario", 1);
                intent.putExtra("nombreUsuario", usuario.getText().toString());
                startActivity(intent);
            } else{
                Snackbar.make(vista, "Datos inválidos. Inténtelo de nuevo.", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception ex){
            Log.d("Error Servidor Cuentas", ex.getMessage());
            Snackbar.make(vista, getText(R.string.excepcion_no_conexion_servidor), Snackbar.LENGTH_LONG).show();
        }
    }
}