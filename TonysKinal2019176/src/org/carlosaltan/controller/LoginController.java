
package org.carlosaltan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.carlosaltan.bean.Login;
import org.carlosaltan.bean.Usuario;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class LoginController implements Initializable {
    private Principal escenarioPrincipal; 
    private ObservableList<Usuario> listaUsuario; 
    private static LoginController instancia; 
    private String usuarioS;
    
    @FXML
    //private TextField txtPassword;
    private PasswordField txtPassword; 
    @FXML
    private TextField txtUserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public static LoginController getInstancia(){
         if(instancia == null)
            instancia = new LoginController ();
        return instancia;
    }
    

    
    public ObservableList<Usuario> getUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios}"); 
            ResultSet  resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
            lista.add(new Usuario(resultado.getInt("codigoUsuario"), 
                                    resultado.getString("nombreUsuario"), 
                                    resultado.getString("apellidoUsuario"),
                                    resultado.getString("usuarioLogin"), 
                                    resultado.getString("contrasena"), 
                                    resultado.getBytes("imagen")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableArrayList(lista); 
    }
    
    @FXML
    private void sesion(){
        Login login = new Login(); 
        int x = 0; 
        boolean bandera = false; 
        login.setUsuarioMaster(txtUserName.getText());
        String contra = txtPassword.getText(); 
        String encript = DigestUtils.md5Hex(contra); 
        login.setPasswordLogin(String.valueOf(encript));
        
        while (x < getUsuario().size() ) {            
            String user = getUsuario().get(x).getUsuarioLogin(); 
            String password = getUsuario().get(x).getContrasena(); 
            byte[] img = getUsuario().get(x).getImagen();
            if (user.equals(login.getUsuarioMaster()) && password.equals(login.getPasswordLogin()) || password.equals(txtPassword.getText())) {
              
                if (img != null) {
                   /* ImageIcon imageIcon = new ImageIcon(img);
                    JLabel label = new JLabel(imageIcon);
                    JOptionPane.showMessageDialog(null, " Sesión iniciada\n" + getUsuario().get(x).getNombreUsuario() + " "+
                    getUsuario().get(x).getApellidoUsuario()+"\n"+"Bienvenido!!");  
                    JOptionPane.showMessageDialog(null, label, "Bienvenido", JOptionPane.PLAIN_MESSAGE); 
                    */
                    LoginController log = LoginController.getInstancia(); 
                    log.setUsuarioS(login.getUsuarioMaster());
                   escenarioPrincipal.ventanaBienvenida();
                }else{
                     /*
                         JOptionPane.showMessageDialog(null, " Sesión iniciada\n" + getUsuario().get(x).getNombreUsuario() + " "+
                        getUsuario().get(x).getApellidoUsuario()+"\n"+"Bienvenido!!");   
                         */
                       setUsuarioS(login.getUsuarioMaster());

                        escenarioPrincipal.ventanaBienvenida();
                    }   
                
                x = getUsuario().size(); 
                bandera = true;
                //escenarioPrincipal.menuPrincipal();
                   
            }
            x++; 
        }
        if (bandera == false) {
            JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrecta");
            txtPassword.clear();
            txtUserName.clear();
        }
    }

    public String getUsuarioS() {
        return usuarioS;
    }

    public void setUsuarioS(String usuarioS) {
        this.usuarioS = usuarioS;
    }

    
    
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventananUsuario();
    }
    public void ventanaMenu(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaBienvenida(){
        escenarioPrincipal.ventanaBienvenida();
    }
}
