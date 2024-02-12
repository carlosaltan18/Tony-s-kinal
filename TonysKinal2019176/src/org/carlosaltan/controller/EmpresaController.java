
package org.carlosaltan.controller;


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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.Empresa;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;



public class EmpresaController implements Initializable {
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal; 
    private ObservableList<Empresa> listaEmpresa;
        
    
    
    
    @FXML private Button btnNuevo;
    @FXML private ImageView imgNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgEliminar;
    @FXML private Button btnEditar;
    @FXML private ImageView imgEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas; 
    @FXML private TableColumn colCodigoEmpresa; 
    @FXML private TableColumn colNombreEmpresa; 
    @FXML private TableColumn colDireccionEmpresa; 
    @FXML private TableColumn colTelefonoEmpresa; 
     
    
   
   @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       desactivarControles();
    }
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarEmpresas;");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"), 
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono"))
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
 
    
       public void nuevo(){
        switch(tipoDeOperacion){
            
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
    
    public void seleccionarElemento(){
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
          txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
          txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
          txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
          txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());  
           switch(tipoDeOperacion){
                case GUARDAR: 
                    deseleccionar();
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar un elemento en este momento");
                    break; 
                
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un registro ;)");
        }
          
    
    }
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
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
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1,((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute(); 
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblEmpresas.getSelectionModel().clearSelection();
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
    
  
    public void guardar(){  
        
        String nombreEmpresa = txtNombreEmpresa.getText(); 
        String direccionEmpresa = txtDireccionEmpresa.getText(); 
        String telefonoEmpresa = txtTelefonoEmpresa.getText(); 
        nombreEmpresa = nombreEmpresa.replaceAll(" ", ""); 
        direccionEmpresa = direccionEmpresa.replaceAll(" ", ""); 
        telefonoEmpresa = telefonoEmpresa.replaceAll(" ", ""); 
        if (nombreEmpresa.length() == 0 || direccionEmpresa.length() == 0 || telefonoEmpresa.length() == 0) {
           JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados");
        }else if(telefonoEmpresa.length() > 10){
            JOptionPane.showMessageDialog(null , "Esta excediendo el número de dijitos del teléfono");  
        
        }else{
             try {
             Empresa registro = new Empresa(); 
            //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
           
                int tel = Integer.parseInt(telefonoEmpresa); 
                if(tel > 0){
                  PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarEmpresa(?, ?, ?)");
                procedimiento.setString(1, registro.getNombreEmpresa());
                procedimiento.setString(2, registro.getDireccion());
                procedimiento.setString(3, registro.getTelefono());
                procedimiento.execute();
                listaEmpresa.add(registro);
  
                }
            }catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);
            }catch (Exception e) {
                e.printStackTrace();    
            }
        }
       
        
    }
     public void reporte(){
        switch(tipoDeOperacion){
            
            case NINGUNO: 
                imprimirReporte(); 
                
                break;
            case ACTUALIZAR:
                 limpiarControles();
                 desactivarControles();
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);
                 imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                 imgReporte.setImage(new Image("/org/carlosaltan/image/Reporte.png"));
                 tipoDeOperacion = operaciones.NINGUNO;
                 deseleccionar();
                break;
        }
    }
     
     
     public void imprimirReporte(){
         Map parametros = new HashMap(); 
         parametros.put("imagen", GenerarReporte.class.getResource("/org/carlosaltan/image/favicon.png")); 
         parametros.put("codigoEmpresa", null);
         GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "ReporteEmpresa", parametros);
     }
     
     
      public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");                        
                } 
                break;
            case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles(); 
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                    imgReporte.setImage(new Image("/org/carlosaltan/image/Reporte.png"));
                    tipoDeOperacion = operaciones.NINGUNO; 
                    cargarDatos();    
                break;
        }
    }
    public void actualizar() {
        String nombreEmpresa = txtNombreEmpresa.getText();
        String direccionEmpresa = txtDireccionEmpresa.getText();
        String telefonoEmpresa = txtTelefonoEmpresa.getText();
        nombreEmpresa = nombreEmpresa.replaceAll(" ", "");
        direccionEmpresa = direccionEmpresa.replaceAll(" ", "");
        telefonoEmpresa = telefonoEmpresa.replaceAll(" ", "");
        if (nombreEmpresa.length() == 0 || direccionEmpresa.length() == 0 || telefonoEmpresa.length() == 0) {
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
        } else {
            try {
                int tel = Integer.parseInt(telefonoEmpresa);
                if (tel > 0) {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?, ?, ?, ?)");
                    Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
                    registro.setNombreEmpresa(txtNombreEmpresa.getText());
                    registro.setDireccion(txtDireccionEmpresa.getText());
                    registro.setTelefono(txtTelefonoEmpresa.getText());
                    procedimiento.setInt(1, registro.getCodigoEmpresa());
                    procedimiento.setString(2, registro.getNombreEmpresa());
                    procedimiento.setString(3, registro.getDireccion());
                    procedimiento.setString(4, registro.getTelefono());
                    procedimiento.execute();
                }
             }catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);
            }catch (Exception e) {
                e.printStackTrace();    
            }
        }
    }
   
    
     public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
    }   
    
    public void deseleccionar(){
        limpiarControles();
        tblEmpresas.getSelectionModel().clearSelection();
    }
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal; 
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
     public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
     public void ventanaServicio(){
         escenarioPrincipal.ventanaServicios();
     }
    public void ventanaGrafica(){
        escenarioPrincipal.ventanaGraficaEmpresa();
    } 
    
    
}
