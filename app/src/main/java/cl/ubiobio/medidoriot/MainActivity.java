package cl.ubiobio.medidoriot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btntemperatura;
    Button btnhumedad;
    Button btnuv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btntemperatura = (Button)findViewById(R.id.btntemp);
        btnhumedad = (Button)findViewById(R.id.btnhum);
        btnuv = (Button)findViewById(R.id.btnuv);

        btntemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irtemperatura = new Intent(MainActivity.this, Mtemperatura.class);
                startActivity(irtemperatura);
            }
        });

        btnhumedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irhumedad = new Intent(MainActivity.this, Mhumedad.class);
                startActivity(irhumedad);
            }
        });

        btnuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iruv = new Intent(MainActivity.this, MradiacionUV.class);
                startActivity(iruv);
            }
        });

    }



}
