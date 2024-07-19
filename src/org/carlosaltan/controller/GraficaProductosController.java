
package org.carlosaltan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class GraficaProductosController implements  Initializable  {
     private Principal escenarioPrincipal; 
    ObservableList<PieChart.Data> pieChartData; 
    @FXML
    private AnchorPane panelG;
    @FXML
    private PieChart grafica;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
       grafica.setData(pieChartData);
    }
    
     public void cargarDatos(){
        pieChartData = FXCollections.observableArrayList(); 
        try {
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ContarPlatos;"); 
           ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {                
                pieChartData.add(new PieChart.Data(resultado.getString("nombrePlato"), 
                                                resultado.getInt("cantidad"))); 
            }           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la gráfica");
            e.printStackTrace();
        }         
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
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProductos();
    }
}
