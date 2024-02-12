
package org.carlosaltan.controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.carlosaltan.main.Principal;


public class ProgramadorController implements Initializable {
    private Principal escenarioPrincipal; 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
    
    public void github(){
        try {
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop(); 
                if(desktop.isSupported(Desktop.Action.BROWSE)){
                    desktop.browse(new URI("https://github.com/caltan-2019176"));
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void correo(){
        try {
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop(); 
                if(desktop.isSupported(Desktop.Action.BROWSE)){
                    desktop.browse(new URI("mailto:caltan-2019176@kinal.edu.gt?subject=Consulta%20sobre%20Tony%C2%B4s%20Kinal-Programador%20&body=Saludos%20Carlos%20Alt%C3%A1n.%0AMe%20comunico%20contigo%20para%20hacerte%20una%20consulta%20sobre%20la%20aplicaci%C3%B3n%20de%20Tony%C2%B4s%20Kinal.%0A"));
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void insta(){
        try {
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop(); 
                if(desktop.isSupported(Desktop.Action.BROWSE)){
                    desktop.browse(new URI("https://www.instagram.com/_carlossss.a/"));
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
