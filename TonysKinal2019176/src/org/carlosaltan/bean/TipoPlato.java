
package org.carlosaltan.bean;


public class TipoPlato {
    
    private int codigoTipoPlato; 
    private String descripcionPlato;
    
    public TipoPlato(){
        
    }

    public TipoPlato(int codigoTipoPlato, String descripcionPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionPlato = descripcionPlato;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    @Override
    public String toString() {
        return  codigoTipoPlato + " | " + descripcionPlato ;
    }
    
    
    
    
    
}
