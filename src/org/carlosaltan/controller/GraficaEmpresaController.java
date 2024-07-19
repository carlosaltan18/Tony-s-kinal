
package org.carlosaltan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javax.swing.GroupLayout.Group;
import javax.swing.JOptionPane;
import org.carlosaltan.bean.GraficaEmpresa;
import org.carlosaltan.db.Conexion;
import org.carlosaltan.main.Principal;


public class GraficaEmpresaController implements Initializable{
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
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_contarServicios;"); 
           ResultSet resultado = procedimiento.executeQuery(); 
            while (resultado.next()) {                
                pieChartData.add(new PieChart.Data(resultado.getString("nombreEmpresa"), 
                                                resultado.getInt("tipoServicio"))); 
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
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
}
