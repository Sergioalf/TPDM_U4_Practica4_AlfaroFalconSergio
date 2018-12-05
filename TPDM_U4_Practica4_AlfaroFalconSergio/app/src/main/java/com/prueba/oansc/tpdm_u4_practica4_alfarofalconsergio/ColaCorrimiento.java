package com.prueba.oansc.tpdm_u4_practica4_alfarofalconsergio;

public class ColaCorrimiento extends ColaSimple{

    public ColaCorrimiento(int tamaño) {
        super(tamaño);
    }

    public boolean encolar(Object dato){
        boolean sePudo = super.encolar(dato);
        if(!sePudo){
            if(corrimiento()){
                return super.encolar(dato);
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean desencolar() {
        super.desencolar();
        if (corrimiento()) {
            return true;
        }
        return false;
    }

    private boolean corrimiento(){
        if(INI==0){
            return false;
        }
        if (!estaVacia()) {
            int T1, T2;
            for (T1 = 0, T2 = INI; T2 <= FIN; T1++, T2++) {
                vector[T1] = vector[T2];
            }
            INI = 0;
            FIN = T1 - 1;
            return true;
        }
        return true;
    }
}
