
package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.Empresa;
import org.carlosaltan.bean.Servicio;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;


public class ServiciosController implements Initializable {
    private Principal escenarioPrincipal; 
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Servicio> listaServicio; 
    private ObservableList<Empresa> listaEmpresa; 
    private DatePicker fecha; 
    
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtTelefonoContacto;  
    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private TextField txtSegundos; 
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtMinutos;
    @FXML
    private TextField txtLugarServicio;
    @FXML
    private TextField txtHora;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblServicios;
    @FXML
    private GridPane grpFecha; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/carlosaltan/resource/TonysKinal.css");
        grpFecha.add(fecha, 1, 1);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    

    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory <Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
        
    }
    
    public void seleccionarElemento(){
        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
            String cadena = String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()); 
            String [] partes = cadena.split(":"); 
            txtHora.setText(partes[0]);
            txtMinutos.setText(partes[1]);
            txtSegundos.setText(partes[2]);
            txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            switch(tipoDeOperacion){
                case GUARDAR: 
                    deseleccionar(); 
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar el elemento es este momento");
                    break;   
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un registro ;)");
        }
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
      Empresa resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)"); 
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery(); 
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"), 
                                            registro.getString("nombreEmpresa"), 
                                            registro.getString("direccion"), 
                                            registro.getString("telefono"));                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado; 
    }
    
    
    public ObservableList<Servicio> getServicio(){
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
        return listaServicio = FXCollections.observableArrayList(lista); 
    }
    
    
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas()"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"), 
                                         resultado.getString("nombreEmpresa"), 
                                         resultado.getString("direccion"), 
                                         resultado.getString("telefono")));               
            }
        } catch (Exception e) {
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
  
    public void guardar() {
        String tipoServicio = txtTipoServicio.getText();
        String lugarServicio = txtLugarServicio.getText();
        String telefoContacto = txtTelefonoContacto.getText();
        String hora = txtHora.getText();
        String minutos = txtMinutos.getText();
        String segundos = txtSegundos.getText();

        tipoServicio = tipoServicio.replaceAll(" ", "");
        lugarServicio = lugarServicio.replaceAll(" ", "");
        telefoContacto = telefoContacto.replaceAll(" ", "");
        hora = hora.replaceAll(" ", "");
        minutos = minutos.replaceAll(" ", "");
        segundos = segundos.replaceAll(" ", "");
        try {
            int h = Integer.parseInt(hora);
            int m = Integer.parseInt(minutos);
            int s = Integer.parseInt(segundos);
            int tel = Integer.parseInt(telefoContacto); 
            if (tipoServicio.length() == 0 || lugarServicio.length() == 0 || telefoContacto.length() == 0 || hora.length() == 0 || minutos.length() == 0 || segundos.length() == 0 || cmbCodigoEmpresa.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
            } else if (hora.length() > 2 || minutos.length() > 2 || segundos.length() > 2) {
                JOptionPane.showMessageDialog(null, "ingrese bien los díjitos de la hora por favor");
            } else if (telefoContacto.length() > 10) {
                JOptionPane.showMessageDialog(null, "La cantidad de números del telefono se exceden de los permitidos");
            } else if (h > 24 || m > 59 || s > 59) {
                JOptionPane.showMessageDialog(null, "Debes usar el formato 24 horas");
            } else {
                
                Servicio registro = new Servicio();
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText());
                registro.setLugarServicio(txtLugarServicio.getText());
                registro.setTelefonoContacto(txtTelefonoContacto.getText());
                String horaServicio = txtHora.getText() + ":" + txtMinutos.getText() + ":" + txtSegundos.getText();
                registro.setHoraServicio(horaServicio);
                registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());

                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?, ?, ?, ?, ?, ?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2, registro.getTipoServicio());
                procedimiento.setTime(3, Time.valueOf(registro.getHoraServicio()));
                procedimiento.setString(4, registro.getLugarServicio());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setInt(6, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaServicio.add(registro);

            }
        } catch (MysqlDataTruncation error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "verificar el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Los números no pueden contener letras", "Aviso", JOptionPane.WARNING_MESSAGE);

        } catch (NullPointerException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe rellenar la fecha", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    public void eliminar() {
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

            default:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar este registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, ((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServicios.getSelectionModel().clearSelection();

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este registro ya que está relacionado con otro");
                            deseleccionar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        deseleccionar();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbCodigoEmpresa.setValue(null);

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
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

    public void actualizar(){
        String tipoServicio = txtTipoServicio.getText();
        String lugarServicio = txtLugarServicio.getText();
        String telefoContacto = txtTelefonoContacto.getText();
        String hora = txtHora.getText();
        String minutos = txtMinutos.getText();
        String segundos = txtSegundos.getText();
        tipoServicio = tipoServicio.replaceAll(" ", "");
        lugarServicio = lugarServicio.replaceAll(" ", "");
        telefoContacto = telefoContacto.replaceAll(" ", "");
        hora = hora.replaceAll(" ", "");
        minutos = minutos.replaceAll(" ", "");
        segundos = segundos.replaceAll(" ", "");
        try {
            int tel = Integer.parseInt(telefoContacto); 
            int h = Integer.parseInt(hora);
            int m = Integer.parseInt(minutos);
            int s = Integer.parseInt(segundos);
            if (tipoServicio.length() == 0 || lugarServicio.length() == 0 || telefoContacto.length() == 0 || hora.length() == 0 || minutos.length() == 0 || segundos.length() == 0) {
                JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
            } else if (hora.length() > 2 || minutos.length() > 2 || segundos.length() > 2) {
                JOptionPane.showMessageDialog(null, "ingrese bien los díjitos de la hora por favor");
            } else if (telefoContacto.length() > 10) {
                JOptionPane.showMessageDialog(null, "La cantidad de números del telefono se exceden de los permitidos");
            } else if (h > 24 || m > 59 || s > 59) {
                JOptionPane.showMessageDialog(null, "Debes usar el formato 24 horas");
            } else {

                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio( ?, ?, ?, ?, ?, ?)");
                Servicio registro = (Servicio) tblServicios.getSelectionModel().getSelectedItem();
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText());
                String horaServicio = txtHora.getText() + ":" + txtMinutos.getText() + ":" + txtSegundos.getText();
                registro.setHoraServicio(horaServicio);
                registro.setLugarServicio(txtLugarServicio.getText());
                registro.setTelefonoContacto(txtTelefonoContacto.getText());
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3, registro.getTipoServicio());
                procedimiento.setTime(4, Time.valueOf(registro.getHoraServicio()));
                procedimiento.setString(5, registro.getLugarServicio());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.execute();

            }
        } catch (MysqlDataTruncation e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "verificar el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Los números no pueden contener letras", "Aviso", JOptionPane.WARNING_MESSAGE);

        } catch (NullPointerException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe rellenar la fecha", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void reporte() {
        switch (tipoDeOperacion) {
            
            case NINGUNO: 
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                     imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar el registro del servicio");
                }
               
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

    public  void imprimirReporte(){
        Map parametros = new HashMap(); 
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()); 
        parametros.put("SUBREPORT_DIR", GenerarReporte.class.getResource("/org/carlosaltan/report/SubReporteEmpleados.jasper"));
        parametros.put("imagen", GenerarReporte.class.getResource("/org/carlosaltan/image/favicon.png"));
        parametros.put("codEmpresa", codEmpresa );
        GenerarReporte.mostrarReporte("ReporteServicio.jasper", "Reporte General", parametros);
        
    }
    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        txtHora.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtMinutos.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtTipoServicio.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
        txtSegundos.setEditable(true);
        fecha.setDisable(false);
    }
       
    public void limpiarControles(){
        txtCodigoServicio.clear();
        txtHora.clear();
        txtLugarServicio.clear();  
        txtMinutos.clear();
        txtTelefonoContacto.clear();
        txtTipoServicio.clear();
        txtSegundos.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.setSelectedDate(null);
        
    }
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtHora.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtMinutos.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtSegundos.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
    }
    public void deseleccionar(){
        limpiarControles();
        tblServicios.getSelectionModel().clearSelection();
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
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    public void ventanaGraficaServicios(){
        escenarioPrincipal.ventanaGraficaServicios();
    }
}
