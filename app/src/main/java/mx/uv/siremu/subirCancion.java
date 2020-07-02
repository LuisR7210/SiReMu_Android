package mx.uv.siremu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class subirCancion extends AppCompatActivity {

    Spinner opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_cancion);

        opciones =(Spinner)findViewById(R.id.generoSP);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.preference_category);
        opciones.setAdapter(adapter);

        opciones =(Spinner)findViewById(R.id.albumSP);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.albums, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.preference_category);
        opciones.setAdapter(adapter1);

    }

    public void nuevoAlbumBT(View view) {
        Intent intent = new Intent (view.getContext(), crear_album.class);
        startActivityForResult(intent, 0);
    }
}
