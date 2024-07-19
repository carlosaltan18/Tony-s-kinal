
package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.carlosaltan.bean.Empleado;
import org.carlosaltan.bean.Servicio;
import org.carlosaltan.bean.ServicioEmpleado;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;


public class ServiciosEmpleadosController implements Initializable {
    private Principal escenearioPrincipal; 
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio; 
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<ServicioEmpleado> listaServicioEmpleado; 
    private DatePicker fecha;
    
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgNuevo;    
    @FXML
    private ImageView imgEliminar;    
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private GridPane grpFecha;
    @FXML
    private TextField txtServiciosCodigoServicio;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private TextField txtHora;
    @FXML
    private TextField txtMinuto;
    @FXML
    private TextField txtSegundo;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private ComboBox cmbCodigoEmpleado;
    @FXML
    private TableView tblServiciosEmpleados;
    @FXML
    private TableColumn colServiciosCodigoServicio;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/carlosaltan/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpleado.setItems(getEmpleado());
        cmbCodigoServicio.setItems(getServicio());
        desactivarControles(); 

    }
    
    
    
    
    public void cargarDatos(){
        tblServiciosEmpleados.setItems(getServiciosEmpleado());
        
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("Servicios_codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("codigoEmpleado"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("codigoServicio"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, String>("lugarEvento"));
        
    }
    
    
    
    
    public void seleccionarElemento(){
        if (tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtServiciosCodigoServicio.setText(String.valueOf(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            String textoHora = String.valueOf(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()); 
            String [] partes = textoHora.split(":"); 
            txtHora.setText(partes[0]);
            txtMinuto.setText(partes[1]);
            txtSegundo.setText(partes[2]);
            txtLugarEvento.setText(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
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
    
    
    
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null; 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)"); 
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"), 
                                   registro.getInt("numeroEmpleado"), 
                                   registro.getString("apellidosEmpleado"),
                                   registro.getString("nombresEmpleado"), 
                                   registro.getString("direccionEmpleado"), 
                                   registro.getString("telefonoContacto"),
                                   registro.getString("gradoCocinero"), 
                                   registro.getInt("codigoTipoEmpleado")); 
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado; 
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
    
    public ObservableList<ServicioEmpleado> getServiciosEmpleado(){
        ArrayList<ServicioEmpleado> lista = new ArrayList<ServicioEmpleado>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Empleados"); 
            ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {                
                lista.add(new ServicioEmpleado(resultado.getInt("Servicios_codigoServicio"), 
                                                    resultado.getInt("codigoServicio"), 
                                                    resultado.getInt("codigoEmpleado"), 
                                                    resultado.getDate("fechaEvento"), 
                                                    resultado.getString("horaEvento"), 
                                                    resultado.getString("lugarEvento")));                
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioEmpleado = FXCollections.observableArrayList(lista); 
        
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
    
    
    public void guardar() {
       
        String hora = txtHora.getText();
        String minutos = txtMinuto.getText();
        String segundos = txtSegundo.getText();
        String lugar = txtLugarEvento.getText();
      
        hora = hora.replaceAll(" ", "");
        minutos = minutos.replaceAll(" ", "");
        segundos = segundos.replaceAll(" ", "");
        lugar = lugar.replaceAll(" ", "");
        try {
                int h = Integer.parseInt(hora);
                int m = Integer.parseInt(minutos);
                int s = Integer.parseInt(segundos);
                 
            if ( hora.length() == 0 || minutos.length() == 0 || segundos.length() == 0 || lugar.length() == 0 || cmbCodigoEmpleado.getSelectionModel().isEmpty() || cmbCodigoServicio.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
            } else if (hora.length() > 2 || minutos.length() > 2 || segundos.length() > 2) {
                JOptionPane.showMessageDialog(null, "ingrese bien los díjitos de la hora por favor");
            } else if (h > 24 || m > 59 || s > 59) {
                JOptionPane.showMessageDialog(null, "Debes usar el formato 24 horas");
            } else { 
                if (h >= 0 && m >= 0 && s >= 0) {
                    ServicioEmpleado registro = new ServicioEmpleado();
                   
                    registro.setCodigoEmpleado(((Empleado) cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                    registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                    registro.setFechaEvento(fecha.getSelectedDate());
                    String horaEvento = txtHora.getText() + ":" + txtMinuto.getText() + ":" + txtSegundo.getText();
                    registro.setHoraEvento(horaEvento);
                    registro.setLugarEvento(txtLugarEvento.getText());

                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Empleado(?, ?, ?, ?, ?)");
                    procedimiento.setInt(1, registro.getCodigoServicio());
                    procedimiento.setInt(2, registro.getCodigoEmpleado());
                    procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
                    procedimiento.setTime(4, Time.valueOf(registro.getHoraEvento()));
                    procedimiento.setString(5, registro.getLugarEvento());
                    procedimiento.execute();
                    listaServicioEmpleado.add(registro);
                }

            }
        } catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "El codigo Servicios_CodigoServicio ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
        }catch (MysqlDataTruncation e) {
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
    
    public void nuevo() {
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
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                deseleccionar();
                break;
            default: 
               if(tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar este registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 
                   if (respuesta == JOptionPane.YES_OPTION) {
                       try {
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio_has_Empleado(?)");
                           procedimiento.setInt(1, ((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                           procedimiento.execute(); 
                           listaServicioEmpleado.remove(tblServiciosEmpleados.getSelectionModel().getSelectedIndex());
                           limpiarControles();
                           tblServiciosEmpleados.getSelectionModel().clearSelection();
                       } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este registro ya que está relacionado con otro");
                            deseleccionar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                   } else {
                        deseleccionar();
                    }
               }else{
                 JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");  
               }                
        }
    }
    public void actualizar(){
        String lugarEvento = txtLugarEvento.getText();
        String hora = txtHora.getText();
        String minutos = txtMinuto.getText();
        String segundos = txtSegundo.getText();
        lugarEvento = lugarEvento.replaceAll(" ", "");
        hora = hora.replaceAll(" ", "");
        minutos = minutos.replaceAll(" ", "");
        segundos = segundos.replaceAll(" ", "");
        try {
            int h = Integer.parseInt(hora);
            int m = Integer.parseInt(minutos);
            int s = Integer.parseInt(segundos);
            if (lugarEvento.length() == 0 || hora.length() == 0 || minutos.length() == 0 || segundos.length() == 0) {
                JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
            } else if (hora.length() > 2 || minutos.length() > 2 || segundos.length() > 2) {
                JOptionPane.showMessageDialog(null, "ingrese bien los díjitos de la hora por favor");
            } else if (h > 24 || m > 59 || s > 59) {
                JOptionPane.showMessageDialog(null, "Debes usar el formato 24 horas");
            } else {

                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio_has_Empleado( ?, ?, ?, ?)");
                ServicioEmpleado  registro = (ServicioEmpleado) tblServiciosEmpleados.getSelectionModel().getSelectedItem();
                registro.setFechaEvento(fecha.getSelectedDate());
                registro.setLugarEvento(txtLugarEvento.getText());
                String horaEvento = txtHora.getText() + ":" + txtMinuto.getText() + ":" + txtSegundo.getText();
                registro.setHoraEvento(horaEvento);
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setTime(3, Time.valueOf(registro.getHoraEvento()));
                procedimiento.setString(4, registro.getLugarEvento());
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
       
     public void editar(){
         switch(tipoDeOperacion ){
              case NINGUNO:
                if (tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    activarControles();
                    cmbCodigoEmpleado.setDisable(true);
                    cmbCodigoServicio.setDisable(true);
                    txtServiciosCodigoServicio.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbCodigoEmpleado.setValue(null);
                    cmbCodigoServicio.setValue(null);

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
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
      //  int codEmpleado = Integer.valueOf(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()); 
        parametros.put("imagen", GenerarReporte.class.getResource("/org/carlosaltan/image/favicon.png"));
     //   parametros.put("codEmpleado", codEmpleado );
        GenerarReporte.mostrarReporte("ReporteServicioEmpleado.jasper", "Reporte ServiciosEmpleados", parametros);
        
    }

    
    public void limpiarControles(){
        txtServiciosCodigoServicio.clear();
        cmbCodigoEmpleado.setValue(null);
        cmbCodigoServicio.setValue(null);
        fecha.setSelectedDate(null);
        txtHora.clear();
        txtMinuto.clear();
        txtSegundo.clear();
        txtLugarEvento.clear();
    }
    public void activarControles(){
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        fecha.setDisable(false);
        txtHora.setEditable(true);
        txtMinuto.setEditable(true);
        txtSegundo.setEditable(true);
        txtLugarEvento.setEditable(true);
    }
    public void desactivarControles(){
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setDisable(true );
        cmbCodigoServicio.setDisable(true);
        fecha.setDisable(true);
        txtHora.setEditable(false);
        txtMinuto.setEditable(false);
        txtSegundo.setEditable(false);
        txtLugarEvento.setEditable(false);
    }
    public void deseleccionar(){
        limpiarControles();
        tblServiciosEmpleados.getSelectionModel().clearSelection();
    }

    public Principal getEscenearioPrincipal() {
        return escenearioPrincipal;
    }

    public void setEscenearioPrincipal(Principal escenearioPrincipal) {
        this.escenearioPrincipal = escenearioPrincipal;
    }
    public void menuPrincipal(){
        escenearioPrincipal.menuPrincipal();
    }
    
}
