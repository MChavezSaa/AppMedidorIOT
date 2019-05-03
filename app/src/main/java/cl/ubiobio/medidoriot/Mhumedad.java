package cl.ubiobio.medidoriot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.github.anastr.speedviewlib.ProgressiveGauge;
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

public class Mhumedad extends AppCompatActivity {

    Button volver;
    Button Actualizar2;

 //comentario
    //creamos la consulta
    private TextView result;//texto
    private TextView result2;//texto
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhumedad);
        result = findViewById(R.id.texto);
        result2 = findViewById(R.id.textoprom);
        queue = Volley.newRequestQueue(this);
        servicioWeb();
        Actualizar2 = findViewById(R.id.Actualizar2);
        Actualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Actualizando medición", Toast.LENGTH_SHORT);

                toast1.show();
                servicioWeb();
            }
        });

        volver = findViewById(volverM);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volvermenu = new Intent(Mhumedad.this, MainActivity.class);
                startActivity(volvermenu);
                finish();
            }
        });


    }
    public void servicioWeb() {

        //buscar la fecha actual
        final Calendar fechaActual = new GregorianCalendar(TimeZone.getTimeZone("Chile/Continental"));
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
        //URL con token
        String WS_URL = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/o0Z5HP1S4p/VIbSnGKyLW/"+fechaConcatenada;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int promedio= 0;
                    int actual =0;

                    ProgressiveGauge progressiveGauge= (ProgressiveGauge) findViewById(R.id.progressiveGauge);
                    progressiveGauge.speedTo(actual);
                    ProgressiveGauge progressiveGaugeProm= (ProgressiveGauge) findViewById(R.id.progressiveGaugeProm);
                    progressiveGaugeProm.speedTo(promedio);
                    result.setText("");//limpiar texto
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        String humedad = data.getString("valor");
                        promedio += Integer.parseInt(humedad);
                        if(i== jsonArray.length()-1) {
                            actual = Integer.parseInt(humedad);
                            result.setText("");
                            result.append(" "+ "\n");
                            result.append("     La humedad actual es: " + humedad+"%RH"+"\n"+"\n");
                        }
                    }
                    promedio=promedio/jsonArray.length();
                    progressiveGauge.setMaxSpeed(100);
                    progressiveGaugeProm.setMaxSpeed(100);


                    progressiveGauge.speedTo(actual);
                    progressiveGaugeProm.speedTo(promedio);
                    result2.setText("");
                    result2.append("     El promedio de humedad es : "+promedio+"%RH");
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