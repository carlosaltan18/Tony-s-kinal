
package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
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
import org.carlosaltan.bean.TipoPlato;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class PlatosController implements Initializable{
    private Principal escenarioPrincipal; 
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private  ObservableList<TipoPlato> listaTipoPlato; 
    private ObservableList<Plato> listaPlato; 
    
    
    @FXML
    private Button btnNuevo;
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
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtDrescipcionPlato;
    @FXML
    private TextField txtPrecioPlato;
    @FXML
    private ComboBox cmbCodigoTipoPlato;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodigoPĺato;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcionPlato;
    @FXML
    private TableColumn colPrecioPlato;
    @FXML
    private TableColumn colCodigoTipoPlato;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbCodigoTipoPlato.setItems(getTipoPlatos());
    }

    
    
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colCodigoPĺato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
    }
    
    
    public void seleccionarElemento(){
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDrescipcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            switch(tipoDeOperacion){
                case GUARDAR: 
                    deseleccionar(); 
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar un elemento en este momento");
                    break;
            }  
        }else{
            JOptionPane.showMessageDialog(null, "Debe de seleccionar un registro");
        }      
    }
    
    
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {                
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"), 
                                            registro.getString("descripcionPlato")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  resultado; 
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
    public ObservableList<TipoPlato> getTipoPlatos() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionPlato"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    
    
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO: 
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
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
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
        }
        
        
    }
    
    public void guardar(){
        String cantidad = txtCantidad.getText(); 
        String nombrePlato = txtNombrePlato.getText(); 
        String descripcionPlato = txtDrescipcionPlato.getText(); 
        String precioPlato = txtPrecioPlato.getText(); 
        cantidad = cantidad.replaceAll(" ", "");
        nombrePlato = nombrePlato.replaceAll(" ", "");
        descripcionPlato = descripcionPlato.replaceAll(" ", "");
        precioPlato = precioPlato.replaceAll(" ", "");
        if (cantidad.length() == 0 || nombrePlato.length() == 0 || descripcionPlato.length() == 0 || precioPlato.length() == 0 || cmbCodigoTipoPlato.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas"); 
        }else{
            try {
                int cant = Integer.parseInt(cantidad); 
                double pre = Double.parseDouble(precioPlato); 
                
                if (cant > 0 || cant > 0){
                    Plato registro = new Plato(); 
                    registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
                    registro.setNombrePlato(txtNombrePlato.getText());
                    registro.setDescripcionPlato(txtDrescipcionPlato.getText());
                    registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
                    registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?, ?, ?, ?, ?)"); 
                    procedimiento.setInt(1, registro.getCantidad());
                    procedimiento.setString(2, registro.getNombrePlato());
                    procedimiento.setString(3, registro.getDescripcionPlato());
                    procedimiento.setDouble(4, registro.getPrecioPlato());
                    procedimiento.setInt(5, registro.getCodigoTipoPlato());
                    procedimiento.execute(); 
                    listaPlato.add(registro); 
                }
               
            } catch (MysqlDataTruncation error) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "verificar el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Un número no puede contener caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }   
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
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                deseleccionar();
                break;
            default:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                     int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (respuesta == JOptionPane.YES_OPTION) {
                         try {
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)"); 
                             procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                             procedimiento.execute();
                             listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex()); 
                             limpiarControles();
                             tblPlatos.getSelectionModel().clearSelection();
                         } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este dato ya que esta relacionado con otro");
                            deseleccionar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }

            
            
            
        }
    }
    
    public void actualizar(){
        String cantidad = txtCantidad.getText(); 
        String nombrePlato = txtNombrePlato.getText(); 
        String descripcionPlato = txtDrescipcionPlato.getText(); 
        String precioPlato = txtPrecioPlato.getText(); 
        cantidad = cantidad.replaceAll(" ", "");
        nombrePlato = nombrePlato.replaceAll(" ", "");
        descripcionPlato = descripcionPlato.replaceAll(" ", "");
        precioPlato = precioPlato.replaceAll(" ", "");
        if (cantidad.length() == 0 || nombrePlato.length() == 0 || descripcionPlato.length() == 0 || precioPlato.length() == 0 ){
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas"); 
        }else{
            try {
                int cant = Integer.parseInt(cantidad); 
                double pre = Double.parseDouble(precioPlato); 
                 if (cant > 0 || cant > 0){
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?, ?, ?, ?, ?)"); 
                Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem(); 
                registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
                registro.setNombrePlato(txtNombrePlato.getText());
                registro.setDescripcionPlato(txtDrescipcionPlato.getText());
                registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText())); 
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCantidad());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4, registro.getDescripcionPlato());
                procedimiento.setDouble(5, registro.getPrecioPlato());
                procedimiento.execute();
                 
                 }
            } catch (MysqlDataTruncation e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Verifique el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Un número no puede contener caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    cmbCodigoTipoPlato.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbCodigoTipoPlato.setValue(null);

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }
    

  
    
    public void limpiarControles(){
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtNombrePlato.clear();
        txtDrescipcionPlato.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue(null);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDrescipcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
        
    }
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDrescipcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    public void deseleccionar(){
        limpiarControles();
        tblPlatos.getSelectionModel().clearSelection();
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
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    public void ventanaGrafica(){
        escenarioPrincipal.ventanaGraficaPlatos();
    }
    
    
}
