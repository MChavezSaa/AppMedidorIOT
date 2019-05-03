package cl.ubiobio.medidoriot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static cl.ubiobio.medidoriot.R.id.volverM;
import static cl.ubiobio.medidoriot.R.id.vuelveM;

public class Mtemperatura extends AppCompatActivity {


    Button volver;
    Button Actualizar1;
    //comentario
    //creamos la consulta
    private TextView result;//texto
    private TextView result2;//texto
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtemperatura);
        result = findViewById(R.id.texto);
        result2 = findViewById(R.id.textoprom);
        queue = Volley.newRequestQueue(this);
        servicioWeb();
        Actualizar1 = findViewById(R.id.Actualizar1);
        Actualizar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Actualizando medición", Toast.LENGTH_SHORT);

                toast1.show();
                servicioWeb();
            }
        });
      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicioWeb();
            }
        });*/
        volver = findViewById(vuelveM);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volvermenu = new Intent(Mtemperatura.this, MainActivity.class);
                startActivity(volvermenu);
                finish();
            }
        });


    }
    public void servicioWeb() {
        //buscamos la fecha actual

        // Calendar rightNow = Calendar.getInstance();
        final Calendar fechaActual = new GregorianCalendar(TimeZone.getTimeZone("Chile/Continental"));
        //Date fechaActual = rightNow.getTime();
        //int dia = fechaActual.getDate();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH)+1;
        int anio = fechaActual.get(Calendar.YEAR);
        String fechaConcatenada = "";
        if (dia<10){
            String diaa = "0"+Integer.toString(dia);
            if (mes<10){
                String mess="0"+Integer.toString(mes);
                fechaConcatenada=""+diaa;
                fechaConcatenada=fechaConcatenada+mess;
                fechaConcatenada=fechaConcatenada+Integer.toString(anio);
            }
        }
        //url de consulta
        String WS_URL = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/o0Z5HP1S4p/E1yGxKAcrg/"+fechaConcatenada;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int promedio= 0;
                    int actual =0;
                    TubeSpeedometer tubeSpeedometer = (TubeSpeedometer) findViewById(R.id.tubeSpeedometer);
                    tubeSpeedometer.speedTo(actual);//reiniciamos el gauge a 0
                    TubeSpeedometer tubeSpeedometer2 = (TubeSpeedometer) findViewById(R.id.tubeSpeedometer2);
                    tubeSpeedometer2.speedTo(promedio);//reiniciamos el gauge a 0

                    result.setText("");//limpiar texto
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        String radiacionUV = data.getString("valor");
                        promedio += Integer.parseInt(radiacionUV);
                        if(i== jsonArray.length()-1) {
                            actual = Integer.parseInt(radiacionUV);
                            result.setText("");
                            result.append("      La temperatura actual es: " + actual+"°C"+"\n"+"\n");
                        }
                    }

                    promedio=promedio/jsonArray.length();
                    result2.setText("");
                    result2.append("      El promedio de temperatura es : "+promedio+"°C");
                    tubeSpeedometer.setMaxSpeed(60);
                    tubeSpeedometer2.setMaxSpeed(60);
                    tubeSpeedometer.speedTo(actual);
                    tubeSpeedometer2.speedTo(promedio);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


}
