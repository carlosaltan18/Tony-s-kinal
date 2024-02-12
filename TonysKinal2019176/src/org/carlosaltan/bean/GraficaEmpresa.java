
package org.carlosaltan.bean;


public class GraficaEmpresa {
    private int tipoServicio; 
    private String nombreEmpresa; 

    public GraficaEmpresa() {
    }

    public GraficaEmpresa(int tipoServicio, String nombreEmpresa) {
        this.tipoServicio = tipoServicio;
        this.nombreEmpresa = nombreEmpresa;
    }

    public int getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(int tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    
}
