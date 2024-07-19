
package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.carlosaltan.bean.Empleado;
import org.carlosaltan.bean.TipoEmpleado;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;

public class EmpleadosController implements Initializable  {
    private Principal escenarioPrincipal; 
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Empleado> listaEmpleado; 
    private ObservableList<TipoEmpleado> listaTipoEmpleado; 
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnReporte; 
    @FXML
    private ImageView imgReporte; 
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNumeroEmpleado;
    @FXML
    private TextField txtApellidosEmpleado;
    @FXML
    private TextField txtNombresEmpleado;
    @FXML
    private TextField txtDireccionEmpleado;
    @FXML
    private TextField txtTelefonoEmpleado;
    @FXML
    private TextField txtGradoCocinero;
    @FXML
    private ComboBox cmbTipoEmpleado;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colNumeroEmpleado;
    @FXML
    private TableColumn colApellidosEmpleado;
    @FXML
    private TableColumn colNombresEmpleado;
    @FXML
    private TableColumn colDireccionEmpleado;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colGradoCocinero;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    
    
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        desactivarControles();
        cmbTipoEmpleado.setItems(getTipoEmpleado());
    }

    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
    
    
    
    public void seleccionarElemento(){
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtNombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefonoEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            switch(tipoDeOperacion){
                case GUARDAR: 
                    deseleccionar(); 
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar un elemento en este momento");
                    break;
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
            
        }
        
        
    } 
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)"); 
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"), 
                                                    registro.getString("descripcion"));               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  resultado ; 
    } 
    
    
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
                   lista.add(new Empleado(resultado.getInt("codigoEmpleado"), 
                                   resultado.getInt("numeroEmpleado"), 
                                   resultado.getString("apellidosEmpleado"),
                                   resultado.getString("nombresEmpleado"), 
                                   resultado.getString("direccionEmpleado"), 
                                   resultado.getString("telefonoContacto"),
                                   resultado.getString("gradoCocinero"), 
                                   resultado.getInt("codigoTipoEmpleado"))); 
            }         
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista); 
        
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery(); 
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"), 
                                                resultado.getString("descripcion")));                 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista); 
    }
    
    
    
    


    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles(); 
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos(); 
                break; 
        }
        
        
    }
    
    public void guardar(){
        String numeroEmp = txtNumeroEmpleado.getText(); 
        String apellidos = txtApellidosEmpleado.getText();
        String nombres = txtNombresEmpleado.getText(); 
        String direccion = txtDireccionEmpleado.getText(); 
        String telefono = txtTelefonoEmpleado.getText(); 
        numeroEmp = numeroEmp.replaceAll(" ", ""); 
        apellidos = apellidos.replaceAll(" ", ""); 
        nombres = nombres.replaceAll(" ", ""); 
        direccion = direccion.replaceAll(" ",  ""); 
        telefono = telefono.replaceAll(" ", ""); 
        if (numeroEmp.length() == 0 || apellidos.length() == 0 || nombres.length() == 0 || direccion.length() == 0 || telefono.length() == 0 || cmbTipoEmpleado.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null , "Todas las celdas deben de estar llenas");    
        }else if(telefono.length() > 10){
              JOptionPane.showMessageDialog(null , "Esta excediendo el número de dijitos del teléfono");  
        }else{
            try {
                int tel = Integer.parseInt(telefono); 
                int numEmpleados = Integer.parseInt(numeroEmp); 
                if (tel > 0 && numEmpleados > 0) {
                    Empleado registro = new Empleado(); 
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?, ?)"); 
                procedimiento.setInt(1, registro.getNumeroEmpleado());
                procedimiento.setString(2, registro.getApellidosEmpleado());
                procedimiento.setString(3, registro.getNombresEmpleado());
                procedimiento.setString(4, registro.getDireccionEmpleado());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setString(6, registro.getGradoCocinero());
                procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
                procedimiento.execute();
                listaEmpleado.add(registro); 

                }
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
    
    
    
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                deseleccionar(); 
                break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 
                    if (respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)"); 
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute(); 
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex()); 
                            limpiarControles();
                            tblEmpleados.getSelectionModel().clearSelection();
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
    
    public void actualizar(){
        String numeroEmp = txtNumeroEmpleado.getText(); 
        String apellidos = txtApellidosEmpleado.getText();
        String nombres = txtNombresEmpleado.getText(); 
        String direccion = txtDireccionEmpleado.getText(); 
        String telefono = txtTelefonoEmpleado.getText(); 
        numeroEmp = numeroEmp.replaceAll(" ", ""); 
        apellidos = apellidos.replaceAll(" ", ""); 
        nombres = nombres.replaceAll(" ", ""); 
        direccion = direccion.replaceAll(" ",  ""); 
        telefono = telefono.replaceAll(" ", ""); 
        if (numeroEmp.length() == 0 || apellidos.length() == 0 || nombres.length() == 0 || direccion.length() == 0 || telefono.length() == 0 ) {
            JOptionPane.showMessageDialog(null , "Todas las celdas deben de estar llenas");    
       }else if(telefono.length() > 10){
              JOptionPane.showMessageDialog(null , "Esta excediendo el número de dijitos del teléfono");  
        }else{
            try {
                int tel = Integer.parseInt(telefono); 
                int numEmpleados = Integer.parseInt(numeroEmp); 
                if (tel > 0 && numEmpleados > 0) {
            
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?, ?, ?, ?, ?, ?, ?)");
                Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem(); 
                registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
                registro.setNombresEmpleado(txtNombresEmpleado.getText());
                registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
                registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
                registro.setGradoCocinero(txtGradoCocinero.getText());
                procedimiento.setInt(1, registro.getCodigoEmpleado());
                procedimiento.setInt(2, registro.getNumeroEmpleado());
                procedimiento.setString(3, registro.getApellidosEmpleado());
                procedimiento.setString(4, registro.getNombresEmpleado());
                procedimiento.setString(5, registro.getDireccionEmpleado());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.setString(7, registro.getGradoCocinero());
                procedimiento.execute(); 
                }
                
           } catch (MysqlDataTruncation e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Verifique el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();  
            }
        }
        
        
        
    }
    
    public void editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    cmbTipoEmpleado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR; 
                    cmbTipoEmpleado.setValue(null);
                        
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un registro");
                }
                
                break;
            case ACTUALIZAR: 
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos();
                break; 
        }
    }
    
    public void reporte() {
        switch (tipoDeOperacion) {
            
            case NINGUNO: 
                     imprimirReporte();
                
        }
    }
    public  void imprimirReporte(){
        Map parametros = new HashMap();  
        parametros.put("imagen", GenerarReporte.class.getResource("/org/carlosaltan/image/favicon.png"));
        GenerarReporte.mostrarReporte("ReporteEmpleados.jasper", "ReporteEmpleados", parametros);
    
    }
    
    
    
    
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtNombresEmpleado.clear();
        txtDireccionEmpleado.clear(); 
        txtTelefonoEmpleado.clear();
        txtGradoCocinero.clear();
        cmbTipoEmpleado.setValue(null);
    }
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbTipoEmpleado.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoEmpleado.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbTipoEmpleado.setDisable(true);   
    }
    
    public void deseleccionar(){
        limpiarControles();
        tblEmpleados.getSelectionModel().clearSelection();
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
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
}
