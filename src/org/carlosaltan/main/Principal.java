/*  DOCUMENTACIÓN 
    Nombre: Carlos Daniel Altán Cortez
    Carné: 2019176
    Código Técnico: IN5AM
    Fecha de creación: 28/03/2023
    Fechas de modificación: 
        11/04/2023 vista Menu Principal y Vista Programador
        12/04/2023 Vista Empresas y conexión con la BD
        15/04/2023 Revisión y mejora del proyecto (css y vistas)
        16/04/2023 Vista Productos
        17/04/2023 Modificación de las Vistas y conbtroladores
        18/04/2023 Trabajo en clase de los controladores y modelo de datos
        19/04/2023 Trabajo del crud de empresa
        21/04/2023 Trabajo del crud Producto 
        22/04/2023 Solución de errores del Crud
        23/04/2023 Inicio del crud TipoEmpleado
        24/04/2023 Finalización del crud TipoEmpleado e inicio del crud TipoPlato
        25/04/2023 Validación de que no se ingresen datos nulos, deseleccionar la TableView y excepción al seleccionar la TableView
        26/04/2023 Creación de las demás vistas mejora de algunas validaciones
        02/05/2023 Inicio del crud Presupuesto
        05/05/2023 Finalización del crud Presupuesto 
        08/05/2023 Se trabajaron las validaciones en PresupuestoController
        10/05/2023 se inicio a trabajar el crud servicios
        19/05/2023 Finalización del crud de servicios
        21/05/2023 Incio del crud de Empleados y Platos
        22/05/2023 Finalización del crud de Empleados, continuación del crud de Platos
        23/05/2023 Finalización del crud Platos, Productos_has_Platos y Servicios_has_Platos
        24/05/2023 Se trabajo el reporte servicios y el crud ServiciosEmpleados
        25/05/2023 Se trabajaron las validaciones de los teléfonos y números
        30/05/2023 Se levantaron los reportes
        31/05/2023 Se trabajo el login 
        02/05/2023 algunos cambios en las validaciones
 
 */
package org.carlosaltan.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.carlosaltan.controller.BienvenidaController;
import org.carlosaltan.controller.ControlUsuarioController;
import org.carlosaltan.controller.EmpleadosController;
import org.carlosaltan.controller.EmpresaController;
import org.carlosaltan.controller.GraficaEmpresaController;
import org.carlosaltan.controller.GraficaPlatosController;
import org.carlosaltan.controller.GraficaPresupuestoController;
import org.carlosaltan.controller.GraficaProductosController;
import org.carlosaltan.controller.GraficaServiciosController;
import org.carlosaltan.controller.LoginController;
import org.carlosaltan.controller.MenuPrincipalController;
import org.carlosaltan.controller.PlatosController;
import org.carlosaltan.controller.PresupuestoController;
import org.carlosaltan.controller.ProductoController;
import org.carlosaltan.controller.ProductoPlatoController;
import org.carlosaltan.controller.ProgramadorController;
import org.carlosaltan.controller.ServiciosController;
import org.carlosaltan.controller.ServiciosEmpleadosController;
import org.carlosaltan.controller.ServiciosPlatosController;
import org.carlosaltan.controller.TipoEmpleadoController;
import org.carlosaltan.controller.TipoPlatoController;
import org.carlosaltan.controller.UsuarioController;

public class Principal extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/carlosaltan/view/";

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2019176");
        escenarioPrincipal.getIcons().add(new Image("/org/carlosaltan/image/favicon.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/carlosaltan/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        //menuPrincipal();
        ventanaLogin();
        escenarioPrincipal.show();

    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 520, 522);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 632, 444);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 992, 520);
            empresa.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProductos() {
        try {
            ProductoController productos = (ProductoController) cambiarEscena("ProductosView.fxml", 992, 520);
            productos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    public void ventanaEmpleados() {
        try {
            EmpleadosController empleados = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 1071, 520);
            empleados.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPlatos() {
        try {
            PlatosController platos = (PlatosController) cambiarEscena("PlatosView.fxml", 992, 520);
            platos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios() {
        try {
            ServiciosController servicios = (ServiciosController) cambiarEscena("ServiciosView.fxml", 992, 520);
            servicios.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 992, 520);
            tipoPlato.setEscenarioPrincipal(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 992, 520);
            tipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProductoPlato() {
        try {
            ProductoPlatoController productoPlato = (ProductoPlatoController) cambiarEscena("ProductoPlatoView.fxml", 992, 520);
            productoPlato.setEscePrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 992, 520);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServiciosEmpleados() {
        try {
            ServiciosEmpleadosController serviciosEmpleados = (ServiciosEmpleadosController) cambiarEscena("ServiciosEmpleadosView.fxml", 992, 520);
            serviciosEmpleados.setEscenearioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServiciosPlatos() {
        try {
            ServiciosPlatosController serviciosPlatos = (ServiciosPlatosController) cambiarEscena("ServiciosPlatosView.fxml", 992, 520);
            serviciosPlatos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 700, 500); 
            login.setEscenarioPrincipal(this); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventananUsuario(){
        try {
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 733, 520); 
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaGraficaEmpresa(){
        try {
            GraficaEmpresaController graficar = (GraficaEmpresaController) cambiarEscena("GraficaEmpresa.fxml", 992, 520); 
            graficar.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaGraficaPresupuesto(){
        try {
            GraficaPresupuestoController grafica = (GraficaPresupuestoController) cambiarEscena("GraficaPresupuestoView.fxml", 992, 520); 
            grafica.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaGraficaServicios(){
        try {
            GraficaServiciosController grafica = (GraficaServiciosController) cambiarEscena("GraficaServiciosView.fxml", 992, 520); 
            grafica.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaGraficaPlatos(){
        try {
            GraficaPlatosController grafica = (GraficaPlatosController) cambiarEscena("GraficaPlatosView.fxml", 992, 520);
            grafica.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaGraficaProductos(){
        try {
            GraficaProductosController grafica = (GraficaProductosController) cambiarEscena("GraficaProductosView.fxml", 992, 520); 
            grafica.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaControlUsuario(){
        try {
            ControlUsuarioController control = (ControlUsuarioController) cambiarEscena("ControlUsuarioView.fxml", 992, 520); 
            control.setEscenarioPrincipal(this); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaBienvenida(){
        try{
            BienvenidaController bienvenida = (BienvenidaController) cambiarEscena("BienvenidaVista.fxml", 404, 284); 
            bienvenida.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.setResizable(false);
        resultado = (Initializable) cargadorFXML.getController();

        return resultado;
    }
}
