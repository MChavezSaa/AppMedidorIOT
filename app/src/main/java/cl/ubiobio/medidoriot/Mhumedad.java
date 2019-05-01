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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

 //comentario
    //creamos la consulta
    private TextView result;//texto
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhumedad);
        result = findViewById(R.id.texto);
        queue = Volley.newRequestQueue(this);
        servicioWeb();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicioWeb();
            }
        });
        volver = findViewById(volverM);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volvermenu = new Intent(Mhumedad.this, MainActivity.class);
                startActivity(volvermenu);
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
        //int mes = fechaActual.getMonth()+1;
        int mes = fechaActual.get(Calendar.MONTH)+1;
        //int anio = fechaActual.getYear()-100;
        int anio = fechaActual.get(Calendar.YEAR);
        String fechaConcatenada = "";
        if (dia<10){
            String diaa = "0"+Integer.toString(dia);
            if (mes<10){
                String mess="0"+Integer.toString(mes);
                fechaConcatenada=""+diaa;
                fechaConcatenada=fechaConcatenada+mess;
                fechaConcatenada=fechaConcatenada+Integer.toString(anio);
                System.out.println("*******************************************************");
                System.out.println("*******************************************************");
                System.out.println("*******************************************************");
                System.out.println("dia: "+diaa);
                System.out.println("mes: "+mess);
                System.out.println("anio: "+Integer.toString(anio));
                System.out.println("fecha concatenada: "+fechaConcatenada);
                System.out.println("*******************************************************");
                System.out.println("*******************************************************");
                System.out.println("*******************************************************");
            }
        }
        //url de consulta
        //String WS_URL = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/lWTXt6CeLP/8IvrZCP3qa/01052019";
        String WS_URL = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/KIl6Exp5mB/E1yGxKAcrg/"+fechaConcatenada;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int promedio= 0;
                    int actual =0;
                    result.setText("");//limpiar texto
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        String temperatura = data.getString("valor");
                        promedio += Integer.parseInt(temperatura);
                        if(i== jsonArray.length()-1) {
                            actual = Integer.parseInt(temperatura);
                            result.append("la Temperatura actual es: " + temperatura+"\n");
                        }
                    }
                    promedio=promedio/jsonArray.length();
                    result.append("Promedio de la temperatura es : "+promedio);
                    grafico(promedio,actual);
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
    public void grafico(int prom, int act){
        final DecoView artView = (DecoView)findViewById(R.id.dynamicArcView); //llamada del com.hookedonplay.decoviewlib.DecoView
        final TextView tvPorciento = (TextView) findViewById(R.id.tv_porciento);
        artView.deleteAll();
        artView.configureAngles(280,0);//configurar alguno total y inicial
        int Lineapromedio = 0;
        int LineaActual = 0;
        artView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))//background
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(200f)
                .setCapRounded(false)
                .build());
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(200, 255, 0, 0)) //LineaPromedio
                .setRange(0, 100, 0)
                .setInitialVisibility(true)
                .setLineWidth(100f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.5f))
                .setSeriesLabel(new SeriesLabel.Builder("Promedio %.0f  °C").build())
                .setInterpolator(new DecelerateInterpolator())
                .setCapRounded(false)
                .setInset(new PointF(-50f, -50f))
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();
        final SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 100, 0)
                .setLineWidth(100f)
                .setInitialVisibility(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER,Color.parseColor("#22000000"),0.5f))
                .setInterpolator(new DecelerateInterpolator())
                .setInset(new PointF(50f, 50f))
                .setCapRounded(false)
                .build();

        LineaActual = artView.addSeries(seriesItem1);
        Lineapromedio = artView.addSeries(seriesItem2);
        artView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDelay(500).setDuration(1000).build());
        artView.addEvent(new DecoEvent.Builder(act).setIndex(LineaActual).setDelay(1000).build());
        artView.addEvent(new DecoEvent.Builder(prom).setIndex(Lineapromedio).setDelay(1000).build());
        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() { // cambia el valor del texto por el porcentaje
            @SuppressLint("DefaultLocale")
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                //obtenemos el porcentaje a mostrar
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                //se lo pasamos al TextView
                tvPorciento.setText(String.format("%.0f", percentFilled * 100f )+ "°C");
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }
}