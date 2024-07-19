package org.carlosaltan.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.carlosaltan.bean.Empresa;
import org.carlosaltan.bean.Presupuesto;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;
import org.carlosaltan.report.GenerarReporte;

public class PresupuestoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
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
    private TextField txtCantidadPresupuesto;
    @FXML
    private TextField txtCodigoPresupuesto;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private TableView tblPresupuestos;
    @FXML
    private TableColumn colCodigoPresupuesto;
    @FXML
    private TableColumn colFechaSolicitud;
    @FXML
    private TableColumn colCantidadPresupuesto;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private GridPane grpFecha;
    @FXML
    private ComboBox cmbCodigoEmpresa;

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

    public void cargarDatos() {
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }

    public void seleccionarElemento() {

        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fecha.selectedDateProperty().set(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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

    public Empresa buscarEmpresa(int codigoEmpresa) {
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

    public ObservableList<Presupuesto> getPresupuesto() {
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
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

    public void guardar() {

        String presupuesto = txtCantidadPresupuesto.getText();
        presupuesto = presupuesto.replaceAll(" ", "");
        if (presupuesto.length() == 0 || cmbCodigoEmpresa.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
        } else {
            try {
                int can = Integer.parseInt(presupuesto); 
                if (can > 0) {
                    Presupuesto registro = new Presupuesto();
                registro.setFechaSolicitud(fecha.getSelectedDate());
                registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
                registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?, ?, ?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaPresupuesto.add(registro);
                }   
            } catch (MysqlDataTruncation e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "verificar el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);

            } catch (NullPointerException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Debe rellenar la fecha", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblPresupuestos.getSelectionModel().clearSelection();

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

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
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

    public void actualizar() {
        String presupuesto = (txtCantidadPresupuesto.getText());
        presupuesto = presupuesto.replaceAll(" ", "");
        if (presupuesto.length() == 0) {
            JOptionPane.showMessageDialog(null, "Todos las celdas deben de estar llenas");
        } else {
            try {
                double can = Double.parseDouble(presupuesto); 
                if (can > 0) {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?, ?, ?)");
                Presupuesto registro = (Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem();
                registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
                registro.setFechaSolicitud(fecha.getSelectedDate());
                procedimiento.setInt(1, registro.getCodigoPresupuesto());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(3, registro.getCantidadPresupuesto());
                procedimiento.execute();
                }
            } catch (MysqlDataTruncation e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Verifique el número de caracteres", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor incorrecto", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (NullPointerException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Debes rellenar la fecha ;)", "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void reporte() {
        switch (tipoDeOperacion) {
       
             case NINGUNO: 
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
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
    
    public void imprimirReporte(){
        Map parametros = new HashMap(); 
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()); 
        parametros.put("SUBREPORT_DIR", GenerarReporte.class.getResource("/org/carlosaltan/report/SubReportePresupuesto.jasper"));
        parametros.put("imagen", GenerarReporte.class.getResourceAsStream("/org/carlosaltan/image/favicon.png"));
        parametros.put("image", GenerarReporte.class.getResourceAsStream("/org/carlosaltan/image/favicon.png"));
        parametros.put("codEmpresa", codEmpresa); 
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuestos", parametros);
         
    }
    
    
    

    public void limpiarControles() {
        txtCantidadPresupuesto.clear();
        txtCodigoPresupuesto.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.setSelectedDate(null);
    }

    public void activarControles() {
        txtCantidadPresupuesto.setEditable(true);
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);

    }

    public void deseleccionar() {
        limpiarControles();
        tblPresupuestos.getSelectionModel().clearSelection();
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

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaGraficar(){
        escenarioPrincipal.ventanaGraficaPresupuesto();
    }
}
