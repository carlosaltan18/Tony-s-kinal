
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
import org.carlosaltan.bean.Servicio;
import org.carlosaltan.bean.ServicioPlato;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;

public class ServiciosPlatosController implements Initializable  {
    private Principal escenarioPrincipal; 
    private enum operaciones {GUARDAR, ELIMINAR, EDITAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Plato> listaPlato; 
    private ObservableList<Servicio> listaServicio; 
    private ObservableList<ServicioPlato> listaServicioPlato; 
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgNuevo;   
    @FXML
    private ImageView imgEliminar;   
    @FXML
    private TextField txtServiciosCodigoServicio;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private TableView tblServicioPlatos;
    @FXML
    private TableColumn colServiciosCodigoServicio;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoServicio;

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoServicio.setItems(getServicios());
    }
    
    
    
   public void cargarDatos(){
       tblServicioPlatos.setItems(getServicioPlato());
       colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("Servicios_codigoServicio"));
       colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoPlato"));
       colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoServicio"));
       
   }
    
    
    public void seleccionarElemento(){
        if (tblServicioPlatos.getSelectionModel().getSelectedItem() != null) {
            txtServiciosCodigoServicio.setText(String.valueOf(((ServicioPlato)tblServicioPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServicioPlato)tblServicioPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioPlato)tblServicioPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
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
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)"); 
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {
            resultado = new Servicio(registro.getInt("codigoServicio"), 
                                            registro.getDate("fechaServicio"), 
                                            registro.getString("tipoServicio"), 
                                            registro.getString("horaServicio"), 
                                            registro.getString("lugarServicio"), 
                                            registro.getString("telefonoContacto"), 
                                            registro.getInt("codigoEmpresa"));               
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;      
    }
    
    
    public ObservableList<ServicioPlato> getServicioPlato(){
        ArrayList<ServicioPlato> lista = new ArrayList<ServicioPlato>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
                lista.add(new ServicioPlato(resultado.getInt("Servicios_codigoServicio"), 
                                                resultado.getInt("codigoPlato"), 
                                                resultado.getInt("codigoServicio")));               
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioPlato = FXCollections.observableArrayList(lista); 
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
    public ObservableList<Servicio> getServicios(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios"); 
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"), 
                                            resultado.getDate("fechaServicio"), 
                                            resultado.getString("tipoServicio"), 
                                            resultado.getString("horaServicio"), 
                                            resultado.getString("lugarServicio"), 
                                            resultado.getString("telefonoContacto"), 
                                            resultado.getInt("codigoEmpresa")));               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listaServicio = FXCollections.observableArrayList(lista); 
        
    }
    
    
    public void guardar() {

        if ( cmbCodigoPlato.getSelectionModel().isEmpty() || cmbCodigoServicio.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
        } else {
            try {

                    ServicioPlato registro = new ServicioPlato();
                    registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                    registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Plato( ?, ?)");
                    procedimiento.setInt(1, registro.getCodigoPlato());
                    procedimiento.setInt(2, registro.getCodigoServicio());
                    procedimiento.execute();
                    listaServicioPlato.add(registro);
                
                
               
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "El codigo Servicios_CodigoServicio ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (MysqlDataTruncation e) {
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
                    if (tblServicioPlatos.getSelectionModel().getSelectedItem() != null) {
                   int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar ServiciosPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                            try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio_has_Plato(?)"); 
                            procedimiento.setInt(1, ((ServicioPlato)tblServicioPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute(); 
                            listaServicioPlato.remove(tblServicioPlatos.getSelectionModel().getSelectedIndex()); 
                            limpiarControles();
                            tblServicioPlatos.getSelectionModel().clearSelection();
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
        txtServiciosCodigoServicio.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
    }
    public void activarControles(){
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }
    public void desactivarControles(){
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }
    public void deseleccionar(){
        limpiarControles();
        tblServicioPlatos.getSelectionModel().clearSelection();
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
