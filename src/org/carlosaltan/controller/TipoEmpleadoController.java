
package org.carlosaltan.controller;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.TipoEmpleado;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class TipoEmpleadoController implements Initializable {
    private Principal escenarioPrincipal; 
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList <TipoEmpleado> listaTipoEmpleado; 
    
    @FXML private Button btnAgregar;
    @FXML private ImageView imgNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgEliminar;
    @FXML private Button btnEditar;
    @FXML private ImageView imgEditar;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;    
    @FXML private TableView tblTipoEmpleados; 
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion; 
    
    
  
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       desactivarControles();
    }
    public void cargarDatos(){
        tblTipoEmpleados.setItems(getTipoEmpleados());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
    }
    public ObservableList<TipoEmpleado> getTipoEmpleados(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarTipoEmpleados;");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
             lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion"))
             );
            }     
        } catch (Exception e) {
            e.printStackTrace();
        }       
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    // ////////////////////////////////BOTONES///////////////////////////////////////
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles(); 
                btnEditar.setDisable(true);
                btnAgregar.setText("Guardar");
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
                btnEditar.setDisable(false);
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos(); 
                break;
        }
    }

        public void seleccionarElemento (){
            if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
                txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
                txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
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
            switch (tipoDeOperacion) {
                case GUARDAR:
                    limpiarControles(); 
                    desactivarControles(); 
                    btnAgregar.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO; 
                    break;
                    
                case ACTUALIZAR:
                    limpiarControles(); 
                    desactivarControles(); 
                    btnEditar.setText("Editar");
                    btnEliminar.setText("Eliminar");
                    btnAgregar.setDisable(false);
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                   imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                   tipoDeOperacion = operaciones.NINGUNO ; 
                   deseleccionar(); 
                    break;
                default:
                    if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar este registro?", "Eliminar TipoEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)"); 
                                procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                 procedimiento.execute(); 
                                listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedItem());    
                                limpiarControles();
                                tblTipoEmpleados.getSelectionModel().clearSelection();
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
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }     
            }
        }
        
        
        public void guardar(){
            String descripcion = txtDescripcion.getText(); 
            descripcion = descripcion.replaceAll(" ", ""); 
            if(descripcion.length() == 0){
                JOptionPane.showMessageDialog(null, "Todos los campos deben de estar llenos");
            }else{
                TipoEmpleado registro = new TipoEmpleado(); 
                registro.setDescripcion(txtDescripcion.getText());
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)"); 
                    procedimiento.setString(1, registro.getDescripcion());
                    procedimiento.execute(); 
                    listaTipoEmpleado.add(registro); 

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
                        
        }
        
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
                        activarControles(); 
                        btnAgregar.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnEliminar.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                        imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                        tipoDeOperacion = operaciones.ACTUALIZAR;

                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    break;
                case ACTUALIZAR:
                    actualizar(); 
                    limpiarControles(); 
                    desactivarControles(); 
                    btnAgregar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnEliminar.setText("Eliminar");
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO; 
                    cargarDatos(); 
                    break;
                    
                    
            }
        }
        
        public void actualizar(){
            String descripcion = txtDescripcion.getText(); 
            descripcion = descripcion.replaceAll(" ", ""); 
            if(descripcion.length() == 0){
                JOptionPane.showMessageDialog(null, "Todos los campos deben de estar llenos");
            }else{
                try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?, ?)"); 
                TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute(); 
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
                        
        }
    
    
    // //////////////////////////////////////METODOS PARA LIMPIAR TEXTFIELD//////////////////////////////////////
     public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
        
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoTipoEmpleado.clear();
        txtDescripcion.clear();
        
    } 
     public void deseleccionar(){
        limpiarControles();
        tblTipoEmpleados.getSelectionModel().clearSelection();
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
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
}
