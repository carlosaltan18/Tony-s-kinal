package org.carlosaltan.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void imprimirReporte(){
         Map parametros = new HashMap(); 
         parametros.put("imagen", GenerarReporte.class.getResource("/org/carlosaltan/image/favicon.png")); 
         GenerarReporte.mostrarReporte("Menu.jasper", "Menú", parametros);
     }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void ventanaProductos() {
        escenarioPrincipal.ventanaProductos();
    }


    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void ventanaServiciosHasEmpleados() {
        escenarioPrincipal.ventanaServiciosEmpleados();
    }

    public void ventanaServiciosPlatos() {
        escenarioPrincipal.ventanaServiciosPlatos();
    }

    public void ventanaProductosPlatos() {
        escenarioPrincipal.ventanaProductoPlato();
    }
    
    public void login(){
        escenarioPrincipal.ventanaLogin();
    }
    public void ventanaUsuario(){
        escenarioPrincipal.ventananUsuario();
    }
    
    public void ventanaGraficaEmpresa(){
        escenarioPrincipal.ventanaGraficaEmpresa();
    }
    public void ventanaControlUsuario(){
        escenarioPrincipal.ventanaControlUsuario();
    }
    
}
