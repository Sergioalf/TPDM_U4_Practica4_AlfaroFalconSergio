package com.prueba.oansc.tpdm_u4_practica4_alfarofalconsergio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView caja1, caja2, caja3;
    TextView clientes1, clientes2, clientes3, tramites1, tramites2, tramites3;
    ColaCorrimiento cola1, cola2, cola3;
    ListView lista1, lista2, lista3;
    Thread cajero1, cajero2, cajero3, repartidor;
    boolean libre[];
    int[] totales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caja1 = findViewById(R.id.caja1);
        caja2 = findViewById(R.id.caja2);
        caja3 = findViewById(R.id.caja3);

        clientes1 = findViewById(R.id.cuenta1);
        clientes2 = findViewById(R.id.cuenta2);
        clientes3 = findViewById(R.id.cuenta3);

        cola1 = new ColaCorrimiento(5);
        cola2 = new ColaCorrimiento(5);
        cola3 = new ColaCorrimiento(5);

        lista1 = findViewById(R.id.cola1);
        lista2 = findViewById(R.id.cola2);
        lista3 = findViewById(R.id.cola3);

        cajero1 = inicializarHilo(1, cola1, lista1, clientes1, caja1);
        cajero2 = inicializarHilo(2, cola2, lista2, clientes2, caja2);
        cajero3 = inicializarHilo(3, cola3, lista3, clientes3, caja3);
        repartidor = inicializarRepartidor();

        libre = new boolean[3];
        totales = new int[3];
        for (int i = 0; i < totales.length; i++) {
            totales[i] = 0;
            libre[i] = true;
        }

        repartidor.start();
        cajero1.start();
        cajero2.start();
        cajero3.start();
    }

    private void llenarLista(ListView lista, ColaCorrimiento cola) {
        ArrayAdapter a = null;
        if (cola.mostrar() != null) {
            a = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cola.mostrar());
        } else {
            Object[] k = new Object[0];
            a = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, k);
        }
        lista.setAdapter(a);
    }

    private Thread inicializarHilo (final int cual, final ColaCorrimiento cola, final ListView lista, final TextView cliente, final ImageView caja) {
        final Thread aux = new Thread() {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenarLista(lista, cola);
                    }
                });
                while (true) {
                    try {
                        if (totales[cual-1] >= 20) {
                            libre[cual-1] = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    caja.setImageResource(R.drawable.not);
                                    cliente.setText("NOP");
                                }
                            });
                            try {
                                sleep(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            totales[cual-1] = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    caja.setImageResource(R.drawable.man);
                                    cliente.setText(totales[cual-1] + "/20");
                                }
                            });
                            libre[cual-1] = true;
                        }
                        while (((int) cola.siguiente() >= 0) && (!cola.estaVacia())) {
                            try {
                                sleep(((int) cola.siguiente())*500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cola.cambiarSiguiente((int) cola.siguiente() - 1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        llenarLista(lista, cola);
                                    } catch (java.lang.NullPointerException e) {
                                    }
                                }
                            });
                            totales[cual-1]++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    llenarLista(lista, cola);
                                    cliente.setText(totales[cual-1] + "/20");
                                }
                            });
                        }
                        cola.desencolar();
                    } catch (java.lang.NullPointerException e) {
                    }
                }
            }
        };
        return aux;
    }

    private Thread inicializarRepartidor () {
        Thread aux = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true) {
                    int a = (int)((Math.random()*2)+1);
                    try {
                        sleep(a*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int transacciones = (int) (Math.random()*(6)+1);
                            int a = asignar(transacciones);
                            switch (a) {
                                case 1:
                                    llenarLista(lista1, cola1);
                                    break;
                                case 2:
                                    llenarLista(lista2, cola2);
                                    break;
                                case 3:
                                    llenarLista(lista3, cola3);
                                    break;
                            }
                        }
                    });
                }
            }
        };
        return aux;
    }

    private int asignar (int transacciones) {
        int c1 = cola1.cantidadDatos();
        int c2 = cola2.cantidadDatos();
        int c3 = cola3.cantidadDatos();
        if (c1 < c2) {
            if (c1 < c3 && libre[0]) {
                cola1.encolar(transacciones);
                return 1;
            } else {
                if (c1 == c3 && libre[0]) {
                    cola1.encolar(transacciones);
                    return 1;
                } else {
                    if (libre[2]){
                        cola3.encolar(transacciones);
                        return 3;
                    }
                }
            }
        } else {
            if (c1 == c2) {
                if (c1 < c3 && libre[0]) {
                    cola1.encolar(transacciones);
                    return 1;
                } else {
                    if (c1 == c3 && libre[0]) {
                        cola1.encolar(transacciones);
                        return 1;
                    } else {
                        if (libre[2]) {
                            cola3.encolar(transacciones);
                            return 3;
                        }
                    }
                }
            } else {
                if (c2 < c3 && libre[1]) {
                    cola2.encolar(transacciones);
                    return 2;
                } else {
                    if (c2 == c3 && libre[1]) {
                        cola2.encolar(transacciones);
                        return 2;
                    } else {
                        if (libre[2]) {
                            cola3.encolar(transacciones);
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

}
