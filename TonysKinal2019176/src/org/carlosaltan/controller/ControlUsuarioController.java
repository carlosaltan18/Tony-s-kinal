
package org.carlosaltan.controller;

import com.mysql.jdbc.ResultSetRow;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.Usuario;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Arrays;
import javafx.scene.image.Image;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;


public class ControlUsuarioController implements Initializable{
    private Principal escenarioPrincipal; 
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO   }; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Usuario> listaUsuario;
    String secretKey = "SomosProgramadores";

    
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView menuPrincipal;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasena;
    @FXML
    private TableView tblControlUsuario;
    @FXML
    private TableColumn colCodigoUsuario;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellido;
    @FXML
    private TableColumn colUsuario;
    @FXML
    private TableColumn colContrasena;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles(); 
    }
    
       
    public void cargarDatos(){
        tblControlUsuario.setItems(getControlUsuario());
        colCodigoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario , Integer>("codigoUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuario , String>("nombreUsuario"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Usuario , String>("apellidoUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Usuario , String>("usuarioLogin"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<Usuario, String>("contrasena"));
    }
     
    public ObservableList getControlUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarUsuarios"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while(resultado.next()){
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
    public void seleccionarElemento() {
        if (tblControlUsuario.getSelectionModel().getSelectedItem() != null) {
            txtUsuario.setText(String.valueOf(((Usuario) tblControlUsuario.getSelectionModel().getSelectedItem()).getUsuarioLogin()));
            String desencriptado = ((Usuario) tblControlUsuario.getSelectionModel().getSelectedItem()).getContrasena();
            txtContrasena.setText(desencriptado);
            switch (tipoDeOperacion) {
                case GUARDAR:
                    deseleccionar();
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar un elemento en este momento");
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro ;)");
        }
    }
    
     public void eliminar() {
        switch (tipoDeOperacion) {
               case ACTUALIZAR:
                limpiarControles();
                desactivarControles();;
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                deseleccionar();
                break;
            default:
                if (tblControlUsuario.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar este registro?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarUsuario(?)");
                            procedimiento.setInt(1, ((Usuario) tblControlUsuario.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                            procedimiento.execute();
                            listaUsuario.remove(tblControlUsuario.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblControlUsuario.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este dato ya que esta relacionado con otro");
                            deseleccionar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        deseleccionar();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
                }
        }
    }
     
     
     public void actualizar(){
         String cont = txtContrasena.getText(); 
         cont = cont.replaceAll(" ", ""); 
         if ( cont.length() == 0) {
             JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
         }else{
             try {
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarContrsena(?, ?) "); 
                 Usuario registro = (Usuario)tblControlUsuario.getSelectionModel().getSelectedItem(); 
                 registro.setContrasena(txtContrasena.getText());
                 procedimiento.setInt(1, registro.getCodigoUsuario());
                 procedimiento.setString(2, registro.getContrasena() );
                 procedimiento.execute();                 
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         
     }
    
     public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblControlUsuario.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }
    
      
   /* 
    public String deencnode(String secretKey, String cadenaEncriptada ){
        String desencriptacion = ""; 
        try {
            byte[] message = Base64.decodeBase64(cadenaEncriptada.getBytes("utf-8")); 
            MessageDigest md5 = MessageDigest.getInstance("MD5"); 
            byte[] digestOfPassword = md5.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24); 
            SecretKey key = new SecretKeySpec(keyBytes, "DESede"); 
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = decipher.doFinal(message); 
            desencriptacion = new String (plainText, "UTF-8"); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  desencriptacion; 
    }
    

    */
      public void desactivarControles() {
        txtContrasena.setEditable(false);
        txtUsuario.setEditable(false);

    }
    
    public void activarControles() {
        txtContrasena.setEditable(true);
        txtUsuario.setEditable(false);

    }

    public void limpiarControles() {
        txtUsuario.clear();
        txtContrasena.clear();

    }
     
 
    public void deseleccionar() {
        limpiarControles();
        tblControlUsuario.getSelectionModel().clearSelection();
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
