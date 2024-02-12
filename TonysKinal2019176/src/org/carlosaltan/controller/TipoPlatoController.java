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
import org.carlosaltan.bean.TipoPlato;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;

public class TipoPlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private TextField txtCodgoTipoPlato;
    @FXML
    private TextField txtDescripcionPlato;
    @FXML
    private TableView tblTipoPlato;
    @FXML
    private TableColumn colCodigoTipoPlato;
    @FXML
    private TableColumn colDescripcionPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tblTipoPlato.setItems(getTipoPlatos());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionPlato"));

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
    // ///////////////////////////////////////BOTONES///////////////////////////////////

    public void nuevo() {
        switch (tipoDeOperacion) {
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

    public void seleccionarElemento() {
        if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
            txtCodgoTipoPlato.setText(String.valueOf(((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionPlato.setText(((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            switch (tipoDeOperacion) {
                case GUARDAR:
                    deseleccionar();
                    JOptionPane.showMessageDialog(null, "No puedes seleccionar un elemento en este momento");
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
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
                imgNuevo.setImage(new Image("/org/carlosaltan/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
               case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Reporte");
                btnNuevo.setDisable(false);
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                deseleccionar();
                break;
            default:
                if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar este registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, ((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tblTipoPlato.getSelectionModel().clearSelection();
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

    public void guardar() {
        String descripcionPlato = txtDescripcionPlato.getText();
        descripcionPlato = descripcionPlato.replaceAll(" ", "");
        if (descripcionPlato.length() == 0) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben de esta llenos");
        } else {
            TipoPlato registro = new TipoPlato();
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
                procedimiento.setString(1, registro.getDescripcionPlato());
                procedimiento.execute();
                listaTipoPlato.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setText("Cancelar");
                    btnEditar.setText("Actualizar");
                    imgEditar.setImage(new Image("/org/carlosaltan/image/Actualizar.png"));
                    imgEliminar.setImage(new Image("/org/carlosaltan/image/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }

                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                imgEditar.setImage(new Image("/org/carlosaltan/image/Editar.png"));
                imgEliminar.setImage(new Image("/org/carlosaltan/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        String descripcionPlato = txtDescripcionPlato.getText();
        descripcionPlato = descripcionPlato.replaceAll(" ", "");
        if (descripcionPlato.length() == 0) {
            JOptionPane.showMessageDialog(null, "Todas las celdas deben de estar llenas");
        } else {
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?, ?)");
                TipoPlato registro = (TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem();
                registro.setDescripcionPlato(txtDescripcionPlato.getText());
                procedimiento.setInt(1, registro.getCodigoTipoPlato());
                procedimiento.setString(2, registro.getDescripcionPlato());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void desactivarControles() {
        txtCodgoTipoPlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
    }

    public void activarControles() {
        txtCodgoTipoPlato.setEditable(false);
        txtDescripcionPlato.setEditable(true);
    }

    public void limpiarControles() {
        txtCodgoTipoPlato.clear();
        txtDescripcionPlato.clear();
    }

    public void deseleccionar() {
        limpiarControles();
        tblTipoPlato.getSelectionModel().clearSelection();
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
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlatos();
    }

}
