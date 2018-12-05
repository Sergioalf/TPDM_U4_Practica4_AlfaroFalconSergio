package com.prueba.oansc.tpdm_u4_practica4_alfarofalconsergio;

public class ColaSimple {

    protected int INI, FIN;
    protected Object[] vector;

    public ColaSimple(int tamaño){
        INI = FIN = -1;
        vector = new Object[tamaño];
    }

    public boolean encolar(Object dato){
        if(estaLLena())
            return false;
        FIN++;
        if(INI==-1)
            INI = FIN;
        vector[FIN] = dato;
        return true;
    }

    public boolean desencolar(){
        if(estaVacia())
            return false;
        if(unSoloElemento()){
            INI = FIN = -1;
            return true;
        }
        INI++;
        return true;
    }

    public Object[] mostrar(){
        if(estaVacia())
            return null;
        Object[] auxiliar = new Object[cantidadDatos()];
        for(int i=INI; i<=FIN; i++)
            auxiliar[i-INI] = vector[i];
        return auxiliar;
    }

    public Object siguiente () {
        if (!estaVacia()) {
            return vector[INI];
        }
        return null;
    }

    public void cambiarSiguiente (Object nuevo) {
        if (!estaVacia()) {
            vector[INI] = nuevo;
        }
    }

    public int cantidadDatos(){
        return FIN-INI+1;
    }

    protected boolean estaLLena(){
        return FIN==vector.length-1;
    }

    protected boolean estaVacia(){
        return INI==-1 && FIN==-1;
    }

    protected boolean insertaPrimereElemento(){
        return INI==-1;
    }

    protected boolean unSoloElemento(){
        return INI==FIN;
    }

}
