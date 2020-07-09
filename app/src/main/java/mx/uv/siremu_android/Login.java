package mx.uv.siremu_android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btLogin = findViewById(R.id.btIniciarSesion);
        btLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IniciarSesion();
            }
        });
    }

    private void IniciarSesion(){
        EditText usuario = findViewById(R.id.etUsuario);
        EditText contraseña = findViewById(R.id.etContraseña);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idUsuario", 1);
        intent.putExtra("nombreUsuario", usuario.getText().toString());
        startActivity(intent);
    }
}