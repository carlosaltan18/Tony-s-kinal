
package org.carlosaltan.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.carlosaltan.bean.Usuario;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class UsuarioController implements Initializable {
    private Principal escenarioPrincipal; 
    private enum operaciones {GUARDAR, NINGUNO }; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    public String ruta; 
    private FileInputStream fis; 
    private int longitudBytes; 
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnElimiinar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtApellidosUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtConstrasena;
    @FXML
    private ImageView imagenU; 
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    /*
        byte[] imageBytes = resultSet.getBytes("imagen");
                        Image image = new Image(new ByteArrayInputStream(imageBytes));
                        imageView.setImage(image);
    */
    
    
    
   /* public File cargarImagen(){
        FileChooser archivo = new FileChooser(); 
        archivo.getExtensionFilters().addAll(
                      new FileChooser.ExtensionFilter("All Images ", "*.*"),
                      new FileChooser.ExtensionFilter("JPG ", "*.jpg"),
                      new FileChooser.ExtensionFilter("PNG ", "*.png")
        );
        archivo.setTitle("Cargar Imagn");
        File ventana = archivo.showOpenDialog(null);
        if(ventana != null){
            
            Image ima = new Image("file:"+ventana.getAbsolutePath());
            imagenU.setImage(ima); 
            ruta = String.valueOf(ventana.getAbsolutePath()); 
                
        }
        return ventana;
    }
   */
   private byte[] readImageBytes(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            inputStream.read(buffer);
            return buffer;
        }
    }
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            
            case NINGUNO: 
                    activarControles(); 
                    btnNuevo.setText("Guardar");
                    btnElimiinar.setText("Cancelar");
                    imgNuevo.setImage(new Image("/org/carlosaltan/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    tipoDeOperacion = operaciones.GUARDAR; 
                    limpiarControles(); 
                   
                break;
            case GUARDAR:
                    guardar();
                    limpiarControles(); 
                    desactivarControles(); 
                    btnNuevo.setText("Nuevo");
                    btnElimiinar.setText("Cancelar");
                    imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    ventanaLogin(); 
                    
                break;  
        }
        
    }
    
    public void guardar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        File selectedFile = fileChooser.showOpenDialog(btnNuevo.getScene().getWindow());
        
        if (selectedFile != null) {
            
        String nombre = txtNombreUsuario.getText(); 
        String apellidos = txtApellidosUsuario.getText(); 
        String user = txtUsuario.getText(); 
        String con = txtConstrasena.getText(); 
        nombre = nombre.replaceAll(" ", ""); 
        apellidos = apellidos.replaceAll(" ", ""); 
        user = user.replaceAll(" ", ""); 
        con = con.replaceAll(" ", ""); 
        if (nombre.length() == 0 || apellidos.length() == 0 || user.length() == 0 || con.length() == 0 ) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados");
        }else{
            try {
                Usuario registro = new Usuario();
                registro.setNombreUsuario(txtNombreUsuario.getText());
                registro.setApellidoUsuario(txtApellidosUsuario.getText());
                registro.setUsuarioLogin(txtUsuario.getText());
                String contra = txtConstrasena.getText(); 
                String encript = DigestUtils.md5Hex(contra); 
                registro.setContrasena(encript);
                System.out.println(encript);
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?, ?)}");
                procedimiento.setString(1, registro.getNombreUsuario());
                procedimiento.setString(2, registro.getApellidoUsuario());
                procedimiento.setString(3, registro.getUsuarioLogin());
                procedimiento.setString(4, registro.getContrasena());
                byte[] imageBytes = readImageBytes(selectedFile);
                    procedimiento.setBytes(5, imageBytes);
                procedimiento.executeUpdate(); 
                
            }catch(MySQLIntegrityConstraintViolationException e){
               JOptionPane.showMessageDialog(null, "Este usuario ya existe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
        }
    }
    
    
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR: 
                 limpiarControles();
                 desactivarControles();
                 btnNuevo.setText("Nuevo");
                 btnElimiinar.setText("Cancelar");
                 imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                 imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                 tipoDeOperacion = operaciones.NINGUNO; 
                 break;
            
                            
        }
    }
    
    
    public void desactivarControles(){
        txtNombreUsuario.setEditable(false);
        txtApellidosUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtConstrasena.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreUsuario.setEditable(true );
        txtApellidosUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtConstrasena.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreUsuario.clear();
        txtApellidosUsuario.clear();
        txtUsuario.clear();
        txtConstrasena.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
    
}
