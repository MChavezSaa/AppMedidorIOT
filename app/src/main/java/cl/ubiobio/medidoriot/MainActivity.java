package cl.ubiobio.medidoriot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btntemperatura;
    ImageButton btnhumedad;
    ImageButton btnuv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btntemperatura = (ImageButton)findViewById(R.id.btntemp);
        btnhumedad = (ImageButton) findViewById(R.id.btnhum);
        btnuv = (ImageButton) findViewById(R.id.btnuv);

        btntemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irtemperatura = new Intent(MainActivity.this, Mtemperatura.class);
                startActivity(irtemperatura);
                finish();
            }
        });

        btnhumedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irhumedad = new Intent(MainActivity.this, Mhumedad.class);
                startActivity(irhumedad);
                finish();
            }
        });

        btnuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iruv = new Intent(MainActivity.this, MradiacionUV.class);
                startActivity(iruv);
                finish();
            }
        });

    }


    public void onBackPressed(){
        //Toast.makeText(this,"Quieres volver?",Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.mensajes).setTitle(R.string.titulo).setPositiveButton(R.string.positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(R.string.negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }


}
