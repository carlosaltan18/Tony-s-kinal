
package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.Plato;
import org.carlosaltan.bean.Producto;
import org.carlosaltan.bean.ProductoPlato;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class ProductoPlatoController implements Initializable  {
    private Principal escenarioPrincipal; 
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Plato> listaPlato; 
    private ObservableList<Producto> listaProducto; 
    private ObservableList<ProductoPlato> listaProductoPlato; 
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgNuevo;  
    @FXML
    private ImageView imgEliminar;    
    @FXML
    private TextField txtProductoCodigoProducto;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoProducto;
    @FXML
    private TableView tblProductoPlatos;
    @FXML
    private TableColumn colProductoCodigoProducto;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoProducto;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        desactivarControles();
        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoProducto.setItems(getProducto());
    }

    
    
    
    
    public void cargarDatos(){
        tblProductoPlatos.setItems(getProductoPlatos());
        colProductoCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("codigoProducto"));
        
        
    }
    
    
    public void seleccionarElemento(){
        if (tblProductoPlatos.getSelectionModel().getSelectedItem() != null) {
            txtProductoCodigoProducto.setText(String.valueOf(((ProductoPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductoPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato())); 
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductoPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            switch (tipoDeOperacion) {
                case GUARDAR:
                    deseleccionar();
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar un elemento en este momento");
                    break;
            }                   
        }else{
             JOptionPane.showMessageDialog(null, "Seleccione un registro ;)");
        }
        
        
    }
    
    
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)"); 
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"), 
                                            registro.getString("nombreProducto"), 
                                            registro.getInt("cantidadProducto")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;   
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)"); 
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"), 
                                        registro.getInt("cantidad"), 
                                        registro.getString("nombrePlato"), 
                                        registro.getString("descripcionPlato"), 
                                        registro.getDouble("precioPlato"), 
                                        registro.getInt("codigoTipoPlato"));                
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  resultado;        
    }
    
    public ObservableList<ProductoPlato> getProductoPlatos(){
        ArrayList<ProductoPlato> lista = new ArrayList<ProductoPlato>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos");
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
                lista.add(new ProductoPlato(resultado.getInt("Productos_codigoProducto"), 
                                                resultado.getInt("codigoProducto"), 
                                                resultado.getInt("codigoPlato"))); 
            }                       
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return listaProductoPlato = FXCollections.observableArrayList(lista); 
    }
    
    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarProductos;");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidadProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
     public ObservableList<Plato> getPlatos(){
        ArrayList <Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"), 
                                        resultado.getInt("cantidad"), 
                                        resultado.getString("nombrePlato"), 
                                        resultado.getString("descripcionPlato"), 
                                        resultado.getDouble("precioPlato"), 
                                        resultado.getInt("codigoTipoPlato")));      
            }
        } catch (Exception e) {
            e.printStackTrace(  );
        }
        return listaPlato = FXCollections.observableArrayList(lista);  
    }
    
     
     public void guardar(){
       
         if ( cmbCodigoPlato.getSelectionModel().isEmpty() || cmbCodigoProducto.getSelectionModel().isEmpty()) {
                 JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
         }else{
            try {
               
                    ProductoPlato registro = new ProductoPlato();
                    registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                    registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto_has_Plato( ?, ?)");
                    procedimiento.setInt(1, registro.getCodigoPlato());
                    procedimiento.setInt(2, registro.getCodigoProducto());
                    procedimiento.execute();
                    listaProductoPlato.add(registro);    
                
                
             } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
                 Toolkit.getDefaultToolkit().beep();
                 JOptionPane.showMessageDialog(null, "El codigo Producto_CodigoProducto ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
             } catch (MysqlDataTruncation error) {
                 Toolkit.getDefaultToolkit().beep();
                 JOptionPane.showMessageDialog(null, "verificar el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
             } catch (java.lang.NumberFormatException e) {
                 Toolkit.getDefaultToolkit().beep();
                 JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);

             } catch (Exception e) {
                 e.printStackTrace();
             }
        }

    }

    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                deseleccionar();
                break;

            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
           default:
               if (tblProductoPlatos.getSelectionModel().getSelectedItem() != null) {
                   int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar ProductoPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                            try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto_has_Plato(?)"); 
                            procedimiento.setInt(1, ((ProductoPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto());
                            procedimiento.execute(); 
                            listaProductoPlato.remove(tblProductoPlatos.getSelectionModel().getSelectedIndex()); 
                            limpiarControles();
                            tblProductoPlatos.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este dato ya que esta relacionado con otro");
                           deseleccionar(); 
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    }else{
                        deseleccionar();
                    }
               }else{
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
               }       
        }
    }
            
    
    
    
    public void limpiarControles(){
        txtProductoCodigoProducto.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
        
    }
    public void activarControles(){
        txtProductoCodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    public void desactivarControles(){
        txtProductoCodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    public void deseleccionar(){
        limpiarControles();
        tblProductoPlatos.getSelectionModel().clearSelection();
    }
    
    
    public Principal getEscePrincipal() {
        return escenarioPrincipal;
    }

    public void setEscePrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
