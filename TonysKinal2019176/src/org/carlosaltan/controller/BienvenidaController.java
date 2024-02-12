
package org.carlosaltan.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.Login;
import org.carlosaltan.bean.Usuario;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class BienvenidaController implements Initializable{
    private Principal escenarioPrincipal; 
    String input ; 
    byte[] imageBytes; 
    String nombre; 
    String apellido; 
    
    
    
    
     @FXML
    private Label lblPersona;

    @FXML
    private ImageView imgPersona;

    @FXML
    private Button btnSiguiente;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //  input = JOptionPane.showInputDialog("Confirmar Usuario");
        getDatos();
        
    }
   
    
    public void getDatos(){
        LoginController login = LoginController.getInstancia();
        String usuario = login.getUsuarioS();
       
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarImagen(?)"); 
            procedimiento.setString(1,usuario );
            
            ResultSet resultado = procedimiento.executeQuery();
            if (resultado.next()) {
              nombre = resultado.getString("nombreUsuario"); 
              apellido = resultado.getString("apellidoUsuario");
              imageBytes = resultado.getBytes("imagen");    
            }
            if (imageBytes != null) {
                lblPersona.setText(nombre+" "+ apellido);
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                 imgPersona.setImage(image);
                
            }else{
                lblPersona.setText(nombre+" "+ apellido);  
                imgPersona.setImage(new Image("/org/carlosaltan/image/Empleado.png"));
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
    
    public void ventanaMenu(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
