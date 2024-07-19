-- Nombre: Carlos Daniel Altán Cortez
-- Carnet: 2019176
-- Código Técnico: IN5AM
-- Fecha de creación: 28/03/2023

Drop database if exists DBTonysKinal2019176; 
Create database DBTonysKinal2019176; 

use DBTonysKinal2019176; 

ALTER DATABASE DBTonysKinal2019176 CHARACTER SET utf8 COLLATE utf8_general_ci;

Create table Empresas(
	codigoEmpresa int auto_increment not null, 
    nombreEmpresa varchar(150) not null, 
	direccion varchar(150) not null, 
    telefono varchar(8), 
    primary key PK_codigoEmpresa(codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment, 
    descripcion varchar(50) not null, 
    primary key PK_codigoTipoEmpleado(codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment, 
    descripcionPlato varchar(100) not null, 
    primary key PK_codigoTipoPlato(codigoTipoPlato)
);


Create table Productos(
	codigoProducto int not null auto_increment, 
	nombreProducto varchar(150) not null, 
    cantidadProducto int not null, 
    primary key PK_codigoProducto(codigoProducto)
    
);

Create table Empleados (
	codigoEmpleado int not null auto_increment, 
    numeroEmpleado int not null, 
    apellidosEmpleado varchar(150) not null, 
    nombresEmpleado varchar(150) not null, 
    direccionEmpleado varchar(150) not null, 
    telefonoContacto varchar(8) not null, 
    gradoCocinero varchar(50), 
    codigoTipoEmpleado int not null, 
    primary key PK_codigoEmpleado(codigoEmpleado), 
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado (codigoTipoEmpleado)
    
);

Create table Servicios(
	codigoServicio int not null auto_increment, 
    fechaServicio date not null, 
    tipoServicio varchar (150) not null, 
    horaServicio time not null, 
    lugarServicio varchar(150) not null, 
    telefonoContacto varchar(10) not null, 
    codigoEmpresa int not null, 
    primary key PK_codigoServicio(codigoServicio), 
    constraint FK_Servicios_Empresas foreign key
		(codigoEmpresa) references Empresas (codigoEmpresa)
);

Create table Presupuestos(
	codigoPresupuesto int not null auto_increment, 
    fechaSolicitud date not null, 
    cantidadPresupuesto decimal(10,2)not null,
	codigoEmpresa int not null, 
    primary key PK_codigoPresupuesto(codigoPresupuesto), 
    constraint FK_Presupuestos_Empresas foreign key(codigoEmpresa)
		references Empresas(codigoEmpresa)
);


Create table Platos(
	codigoPlato int not null auto_increment, 
	cantidad int not null, 
    nombrePlato varchar(150) not null, 
    descripcionPlato varchar(150) not null, 
    precioPlato decimal(10,2) not null, 
    codigoTipoPlato int not null, 
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key(codigoTipoPlato)
		references TipoPlato (codigoTipoPlato)
); 

Create table Productos_has_Platos (
	Productos_codigoProducto int not null auto_increment, 
    codigoPlato int not null,
    codigoProducto int not null, 
    primary key PK_Productos_codigoProducto (Productos_codigoProducto), 
	constraint FK_Productos_has_Platos_Productos foreign key(codigoProducto)
		references Productos (codigoProducto), 
	constraint FK_Productos_has_Platos_Platos foreign key (codigoPlato)
		references Platos (codigoPlato)
); 

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null auto_increment, 
    codigoPlato int not null, 
    codigoServicio int not null, 
    primary key PK_Servicios_codigoProducto(Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios (codigoServicio), 
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos (codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null auto_increment, 
    codigoServicio int not null, 
    codigoEmpleado int not null, 
    fechaEvento date not null, 
    horaEvento time not null, 
    lugarEvento varchar(150) not null, 
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio), 
    constraint FK_Servicios_has_Empleados_Servicios foreign key(codigoServicio)
		references Servicios(codigoServicio), 
	constraint FK_Servicio_has_Empleados_Empleados foreign key(codigoEmpleado)
		references Empleados (codigoEmpleado)
);


Create table Usuario(
	codigoUsuario int not null auto_increment, 
    nombreUsuario varchar(100), 
    apellidoUsuario varchar(100), 
    usuarioLogin varchar(50) unique, 
    contrasena varchar(50), 
    imagen LONGBLOB,
    primary key PK_codigoUsuario(codigoUsuario)
);

Create table Login(
	usuarioMaster varchar(50) not null, 
    passwordLogin varchar(50) not null, 
    primary Key PK_usuarioMaster(usuarioMaster)

);
-- ****************************************************Usuario*********************************************
Delimiter //
	Create procedure sp_AgregarUsuario1(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
		Begin
			Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena); 
        
        End//
Delimiter ; 


Delimiter //
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50), in imagen LONGBLOB)
		Begin
			Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena, imagen)
				values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena, imagen); 
        
        End//
Delimiter ; 

Delimiter //
	Create procedure sp_EditarContrsena(in id int, in contrasena varchar(50))
		Begin
			update Usuario U
				set U.contrasena = contrasena
                where U.codigoUsuario = id; 
		End//
Delimiter ; 


Delimiter //
	create procedure sp_EliminarUsuario(in id int)
		Begin
			Delete from Usuario 
				where codigoUsuario = id; 
		end//
Delimiter ; 
        
Delimiter //
	Create procedure sp_ListarUsuarios()
		begin
			Select 
				U.codigoUsuario, 
                U.nombreUsuario, 
                U.apellidoUsuario, 
                U.usuarioLogin, 
                U.contrasena,
                U.imagen
				from Usuario  U; 
        end//
Delimiter ; 

Delimiter //
	Create procedure sp_BuscarImagen(in usuario varchar(100))
		begin
			Select 
				U.nombreUsuario, 
                U.apellidoUsuario,
                U.imagen
				from Usuario  U
					where U.usuarioLogin = usuario; 
        end//
Delimiter ; 



-- call sp_AgregarUsuario1('Carlos', 'Altan', 'caltan', 'caltan'); 
call sp_AgregarUsuario1('Carlos', 'Altan', 'admin', 'admin' ); 
-- call sp_AgregarUsuario1('Pedro', 'Armas', 'parmas', 'parmas'); 
-- call sp_AgregarUsuario1('Sebastian', 'Abad', 'sabad', 'sabad'); 
-- call sp_AgregarUsuario1('Llanel', 'Escobar', 'lescobar', 'lescobar'); 
-- call sp_AgregarUsuario1('Daniel', 'Altán', 'daltan', 'daltan'); 
-- call sp_AgregarUsuario('Daniel', 'Altán', 'cvv', 'cvv', ''); 
call sp_ListarUsuarios();
select * from Usuario; 

-- //////////////////////////////////////////////////PROCEDIMIENTOS ALMACENADOS///////////////////////////////////////////////
-- *****************************************************EMPRESA**************************************************************
-- ----------------------------------------------Agregar Empresa----------------------------------------------------------
Delimiter //
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150),
		in telefono varchar(8))
        Begin
			Insert into Empresas (nombreEmpresa, direccion, telefono) values 
				(nombreEmpresa, direccion, telefono); 
		End//
Delimiter ;  
-- ----------------------------------------------Editar Empresa----------------------------------------------------
Delimiter //
	Create procedure sp_EditarEmpresa(in codEmpresa int, in nomEmpresa varchar(150), 
		in direcc varchar(150), in tel varchar(8))
        Begin
			Update Empresas E 
				set 
					E.nombreEmpresa = nomEmpresa,
                    E.direccion = direcc, 
                    E.telefono = tel
                    where E.codigoEmpresa = codEmpresa; 
        End//
Delimiter ; 

-- --------------------------------------------Eliminar Empresa----------------------------------------------------
Delimiter //
	Create procedure sp_EliminarEmpresa (in codEmpresa int)
		Begin
			Delete from Empresas 
				Where codigoEmpresa = codEmpresa; 
        End//
Delimiter ; 

-- ------------------------------------------------Listar Empresas-----------------------------------------------
Delimiter //
	Create procedure sp_ListarEmpresas ()
		Begin
			Select 
				E.codigoEmpresa, 
                E.nombreEmpresa, 
                E.direccion,
                E.telefono
                From Empresas E; 
        End//
Delimiter ; 
-- -----------------------------------------------Buscar Empresas---------------------------------------------
Delimiter //
	Create procedure sp_BuscarEmpresa (in codEmpresa int) 
		Begin
			Select 
				E.codigoEmpresa,
				E.nombreEmpresa, 
                E.direccion,
                E.telefono
                From Empresas E 
					where E.codigoEmpresa = codEmpresa; 
        End//
Delimiter ; 

-- ********************************************************PRESUPUESTO****************************************************
-- ------------------------------------------------Agregar Presupuesto-----------------------------------------
Delimiter //
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), 
		in codigoEmpresa int)
        Begin
			Insert into Presupuestos (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa); 
        End//
Delimiter ; 


-- -------------------------------------------Editar Presupuesto-----------------------------------------------
Delimiter //
	Create procedure sp_EditarPresupuesto(in codPresupuesto int, in fechaSoli date, 
		in cantidadPres decimal(10,2))
        Begin
			Update Presupuestos P 
				set 
					P.fechaSolicitud = fechaSoli,
					P.cantidadPresupuesto = cantidadPres
                    where P.codigoPresupuesto= codPresupuesto; 
        End//
Delimiter ; 
-- ---------------------------------------------Eliminar Presupuesto-----------------------------------------------
Delimiter //
	Create procedure sp_EliminarPresupuesto (in codPresupuesto int)
		Begin
			Delete from Presupuestos 
				where codigoPresupuesto = codPresupuesto; 
        End//
Delimiter ; 
-- -----------------------------------------------Listar Presupuestos---------------------------------------------------
Delimiter //
	Create procedure sp_ListarPresupuestos()
		Begin
			Select
				P.codigoPresupuesto, 
                P.fechaSolicitud, 
                P.cantidadPresupuesto, 
                P.codigoEmpresa
                From  presupuestos P; 
        End//
Delimiter ; 

-- ---------------------------------------------------Buscar Presupuesto-----------------------------------------------
Delimiter //
	create procedure sp_BuscarPresupuestos(in codPresupuesto int)
		Begin
			Select 
				P.codigoPresupuesto,
				P.fechaSolicitud, 
                P.cantidadPresupuesto, 
                P.codigoEmpresa
                From Presupuestos P
					where P.codigoPresupuesto = codPresupuesto; 
        End//
Delimiter ; 
-- **********************************************************TIPO EMPLEADO**********************************************************
-- -------------------------------------------------------Agregar TipoEmpleado---------------------------------------
Delimiter //
	create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin
			Insert into TipoEmpleado (descripcion)
				Values (descripcion); 
        End//
Delimiter ; 

-- -------------------------------------------------------Editar TipoEmpleado------------------------------------------
Delimiter //
	Create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descrip varchar(50))
		Begin
			Update TipoEmpleado T
				set
					T.descripcion = descrip
					Where codigoTipoEmpleado = codTipoEmpleado; 
        End//
Delimiter ; 
-- ---------------------------------------------------------Eliminar TipoEmpleado-----------------------------------------------
Delimiter //
	Create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		Begin
			Delete from TipoEmpleado 
				Where codigoTipoEmpleado = codTipoEmpleado; 
        End//
Delimiter ; 
-- ------------------------------------------------Listar TipoEmpleados--------------------------------------------------------
Delimiter //
	Create procedure sp_ListarTipoEmpleados ()
		Begin
			Select 
				T.codigoTipoEmpleado, 
                T.descripcion
                From TipoEmpleado T; 
        End//
Delimiter ; 
-- -------------------------------------------------Buscar TipoEmpleado--------------------------------------------------
Delimiter //
	Create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int )
		Begin
			select 
				T.codigoTipoEmpleado,
                T.descripcion
                From TipoEmpleado T
				where T.codigoTipoEmpleado = codTipoEmpleado; 
				
        End//
Delimiter ; 

-- ******************************************************TIPO PLATO****************************************************************
-- ----------------------------------------------------Agregar TipoPlato---------------------------------------------------------
Delimiter //
	Create procedure sp_AgregarTipoPlato(in descripcionPlato varchar(100))
		Begin
			Insert into TipoPlato (descripcionPlato)
				values (descripcionPlato); 
        End//
Delimiter ; 

-- ---------------------------------------------- Editar TipoPlato----------------------------------------------------------------
Delimiter //
	Create procedure sp_EditarTipoPlato (in codTipoPlato int, in descripcionPla varchar(100))
		Begin
			Update TipoPlato TP
				set
					TP.descripcionPlato = descripcionPla
                    where codigoTipoPlato = codTipoPlato; 
        End//
Delimiter ; 
-- ----------------------------------------------------Eliminar TipoPlato----------------------------------------------------------
Delimiter //
	create procedure sp_EliminarTipoPlato (in codTipoPlato int)
		Begin
			Delete from TipoPlato 
				where codigoTipoPlato = codTipoPlato;
        End//
Delimiter ; 
-- -------------------------------------------------------Listar TipoPlatos-------------------------------------------------------
Delimiter //
	Create procedure sp_ListarTipoPlatos ()
		Begin
			Select 
				TP.codigoTipoPlato, 
                TP.descripcionPlato
                From TipoPlato TP; 
        End//
Delimiter ; 
-- -------------------------------------------------------Buscar TipoPlato-----------------------------------------------------------
Delimiter //
	Create procedure sp_BuscarTipoPlato(in codTipoPlato int )
		Begin
			Select 
				TP.codigoTipoPlato,
                TP.descripcionPlato
                From TipoPlato TP
					Where TP.codigoTipoPlato = codTipoPlato; 
        End//
Delimiter ;
-- *********************************************************PRODUCTOS**************************************************************
-- ---------------------------------------------------------Agregar Producto------------------------------------------------------
Delimiter //
	Create procedure sp_AgregarProducto (in nombreProducto varchar (150), in cantidad int)
		Begin
			Insert into Productos (nombreProducto, cantidadProducto)
				Values (nombreProducto, cantidad); 
        End//
Delimiter ; 

-- ------------------------------------------------------Editar Producto-----------------------------------------------------------
Delimiter //
	create procedure sp_EditarProducto(in codProducto int,in nombreProduc varchar (150), in canti int)
		Begin
			Update Productos P
				set
					P.nombreProducto = nombreProduc, 
                    P.cantidadProducto = canti
                    Where P.codigoProducto = codProducto;
        End//
Delimiter ; 
-- ---------------------------------------------------Eliminar Producto-------------------------------------------------------------
Delimiter //
	Create procedure sp_EliminarProducto (in codProducto int)
		Begin
			Delete from Productos 
				where codigoProducto = codProducto; 
        End//
Delimiter ; 
-- --------------------------------------------------Listar Productos-----------------------------------------------------------
Delimiter //
	Create procedure sp_ListarProductos ()
		Begin
			Select 
				P.codigoProducto,
				P.nombreProducto, 
				P.cantidadProducto
                From Productos P; 
				
        End//
Delimiter ; 
-- -----------------------------------------------------------Buscar Producto----------------------------------------------------------
Delimiter //
	Create procedure sp_BuscarProducto (in codProducto int)
		Begin
			Select 
				P.codigoProducto,
				P.nombreProducto, 
				P.cantidadProducto
                From Productos P
					where P.codigoProducto = codProducto; 
        End//
Delimiter ; 
-- **********************************************************PLATOS*************************************************************
-- ----------------------------------------------------------Agregar plato----------------------------------------------------
Delimiter //
	create procedure sp_AgregarPlato(in cantidad int, in nombrePlato varchar(150), in descripcionPlato varchar(150),
		in precioPlato decimal (10,2), in codigoTipoPlato int )
        Begin
			Insert Into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End//
Delimiter ; 

-- ------------------------------------------------------Editar Plato-------------------------------------------------------
Delimiter //
	Create Procedure sp_EditarPlato (in codPlato int, in canti int, in nomPlato varchar(150), in descripPlato varchar(150),
		in preciPlato decimal (10,2))
        Begin
			Update Platos P
				set
                P.cantidad = canti, 
                P.nombrePlato = nomPlato, 
                P.descripcionPlato = descripPlato, 
                P.precioPlato = preciPlato
				Where codigoPlato = codPlato; 
					
        End//
Delimiter ; 
-- -------------------------------------------------Eliminar Plato-------------------------------------------------------------
Delimiter //
	Create procedure sp_EliminarPlato (in codPlato int)
		Begin
			Delete from Platos 
				Where codigoPlato = codPlato; 
        End//
Delimiter ; 
-- -----------------------------------------------------------Listaer Platos-----------------------------------------------
Delimiter //
	Create procedure sp_ListarPlatos()
		Begin
			Select 
				PL.codigoPlato,
				PL.cantidad,
				PL.nombrePlato, 
				PL.descripcionPlato, 
				PL.precioPlato, 
				PL.codigoTipoPlato
				From Platos PL ; 
				
        End//
Delimiter ; 
-- ----------------------------------------------------------Buscar Plato---------------------------------------------------
Delimiter //
	Create Procedure sp_BuscarPlato(in codPlato int)
		Begin
			Select 
				PL.codigoPlato,
				PL.cantidad,
				PL.nombrePlato, 
				PL.descripcionPlato, 
				PL.precioPlato, 
				PL.codigoTipoPlato
				From Platos PL 
					Where PL.codigoPlato =codPlato ; 
        End//
Delimiter ; 
-- ************************************************Productos_Has_Platos***************************************************
-- -----------------------------------------------------Agregar Producto_has_Plato--------------------------------------
Delimiter //
	Create procedure sp_AgregarProducto_has_Plato ( in codigoPlato int, in codigoProducto int)
        Begin
			Insert Into Productos_has_Platos( codigoPlato, codigoProducto)
				Values ( codigoPlato, codigoProducto);
        End//
Delimiter ; 


-- ------------------------------------------------Editar Producto_has_Plato--------------------------------------------
Delimiter //
	Create procedure sp_EditarProducto_has_Plato (in Productos_codProducto int, 
		in codPlato int, in codProducto int)
		Begin
			update Productos_has_Platos PH
				set 
					PH.codigoPlato = codPlato, 
                    PH.codigoProducto = codProducto
                    Where PH.Productos_codigoProducto = Productos_codProducto;
        End//
Delimiter ; 
-- -----------------------------------------------Eliminar Producto_has_plato-------------------------------------
Delimiter //
	Create procedure sp_EliminarProducto_has_Plato(in Productos_codProducto int)
		Begin
			Delete from Productos_has_Platos 
				Where Productos_codigoProducto = Productos_codProducto; 
        End//
Delimiter ; 

-- ---------------------------------------------------Listar Productos_has_Platos-------------------------------
Delimiter //
	Create procedure sp_ListarProductos_has_Platos ()
		Begin
			Select 
				PH.Productos_codigoProducto, 
				PH.codigoPlato, 
				PH.codigoProducto
				From Productos_has_Platos PH; 
        End//
Delimiter ; 
-- --------------------------------------------------Buscar Producto_has_Plato------------------------------------
Delimiter //
	Create procedure sp_BuscarProducto_has_Plato (in Producto_codProducto int)
		Begin
			Select 
				PH.Productos_codigoProducto,
				PH.codigoPlato, 
				PH.codigoProducto
				From Productos_has_Platos PH
					Where PH.Productos_codigoProducto = Producto_codProducto; 
        End//
Delimiter ; 
-- **************************************************SERVICIOS********************************************************
-- ---------------------------------------------------Agregar Servicio----------------------------------------------
Delimiter //
	Create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(150), 
		in horaServicio time , in lugarServicio varchar (150), in telefonoContacto varchar(10), 
        in codigoEmpresa int)
        Begin
			Insert into Servicios (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				Values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End//
Delimiter ; 

-- -------------------------------------------------Editar Servicio--------------------------------------------
Delimiter //
	Create procedure sp_EditarServicio (in codServicio int, in fechaServi date, in tipoServi varchar(150), 
		in horaServi time , in lugarServi varchar (150), in telefonoContac varchar(10))
        Begin
			Update Servicios S
				set
					S.fechaServicio = fechaServi, 
                    S.tipoServicio = tipoServi, 
                    S.horaServicio = horaServi, 
                    S.lugarServicio = lugarServi,
                    S.telefonoContacto = telefonoContac
                    Where S.codigoServicio = codServicio; 
        End//
Delimiter ; 
-- -----------------------------------------------Eliminar Servicio--------------------------------------
Delimiter //
	Create procedure sp_EliminarServicio (in codServicio int )
		Begin
			Delete from Servicios 
				Where codigoServicio = codServicio; 
        End//
Delimiter ; 

-- --------------------------------------------------listar Servicios-----------------------------
Delimiter //
	Create procedure sp_ListarServicios ()
		Begin
			Select 
				S.codigoServicio, 
				S.fechaServicio, 
				S.tipoServicio, 
				S.horaServicio, 
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
                From Servicios S; 
        End//
Delimiter ;
-- -------------------------------------------------Buscar Servicio-----------------------------------
Delimiter //
	Create procedure sp_BuscarServicio (in codServicio int)
		Begin
			Select
				S.codigoServicio, 
				S.fechaServicio, 
				S.tipoServicio, 
				S.horaServicio, 
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
                From Servicios S
					Where S.codigoServicio = codServicio; 
        End//
Delimiter ;
-- ****************************************************SERVICIOS_HAS_PLATOS**************************************
-- --------------------------------------------------Agregar Servicio_has_Plato-----------------------------------
Delimiter //
	Create procedure sp_AgregarServicio_has_Plato( in codigoPlato int,
		in codigoServicio int)
        Begin
			Insert into Servicios_has_Platos( codigoPlato, codigoServicio)
				Values ( codigoPlato, codigoServicio);
        End//
Delimiter ; 


-- -----------------------------------------------Editar Servicio_has_Plato----------------------------------------
Delimiter //
	Create Procedure sp_EditarServicio_has_Plato(in Servicios_codServicio int , in codPlato int,
		in codServicio int)
        Begin
			Update Servicios_has_Platos SH
				set 
					SH.codigoPlato = codPlato, 
					SH.codigoServicio = codServicio
					Where SH.Servicios_codigoServicio = Servicios_codServicio; 					
        End//
Delimiter ; 

-- -----------------------------------------------Eliminar Servicio_has_Plato----------------------------------
Delimiter //
	Create procedure sp_EliminarServicio_has_Plato(in Servicios_codServicio int)
		Begin
			Delete From Servicios_has_Platos 
				where Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ; 
-- --------------------------------------------------Listar Servicios_has_Platos---------------------------------
Delimiter //
	Create procedure sp_ListarServicios_has_Platos ()
		Begin
			Select 
				S.Servicios_codigoServicio,
                S.codigoPlato, 
                S.codigoServicio
                From Servicios_has_Platos S; 
        End//
Delimiter ;
-- --------------------------------------------------Buscar Servicio_has_Plato-------------------------------------
Delimiter //
	Create procedure sp_BuscarServicio_has_Plato (in Servicios_codServicio int)
		Begin
			Select 
				S.Servicios_codigoServicio,
                S.codigoPlato, 
                S.codigoServicio
                From Servicios_has_Platos S
					Where S.Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ;
 -- **************************************************Empleados**************************************************************
 -- ----------------------------------------Agregar Empleado ---------------------------------------------------
 Delimiter //
	Create Procedure sp_AgregarEmpleado (in numeroEmpleado int, in apellidosEmpleado varchar(150),
		in nombresEmpleado varchar(150), in direccionEmpleado varchar(150), in telefonoContacto varchar(8), 
		in gradoCocinero varchar(50), in  codigoTipoEmpleado int)
        Begin
			Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado,
				telefonoContacto, gradoCocinero, codigoTipoEmpleado)
				values (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado,
					telefonoContacto, gradoCocinero, codigoTipoEmpleado);
        End//
 Delimiter ; 

 -- ----------------------------------------------Editar Empleado----------------------------------------------------
Delimiter //
	Create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int, in apelliEmpleado varchar(150),
		in nomEmpleado varchar(150), in direccEmpleado varchar(150), in telContacto varchar(8), 
		in graCocinero varchar(50))
        Begin
			Update Empleados E
				set
                E.numeroEmpleado = numEmpleado,
                E.apellidosEmpleado = apelliEmpleado, 
                E.nombresEmpleado = nomEmpleado, 
                E.direccionEmpleado = direccEmpleado,
                E.telefonoContacto = telContacto,
                E.gradoCocinero = graCocinero
                where E.codigoEmpleado = codEmpleado; 		
        End//
 Delimiter ; 
-- ------------------------------------------Eliminar Empleado---------------------------------------------
Delimiter //
	Create procedure sp_EliminarEmpleado(in codEmpleado int)
		Begin
			Delete from Empleados 
				where codigoEmpleado = codEmpleado; 
        End//
Delimiter ; 
-- ---------------------------------------------Listar Empleados------------------------------------------------
Delimiter //
	Create procedure sp_ListarEmpleados ()
		Begin
			Select 
				E.codigoEmpleado, 
				E.numeroEmpleado, 
				E.apellidosEmpleado, 
				E.nombresEmpleado, 
				E.direccionEmpleado,
				E.telefonoContacto, 
				E.gradoCocinero, 
				E.codigoTipoEmpleado
                From Empleados E; 
        End//
Delimiter ; 
-- ---------------------------------------------------Buscar Empleado-------------------------------------------
Delimiter //
	Create procedure sp_BuscarEmpleado (in codEmpleado int)
		Begin
			Select 
				E.codigoEmpleado,
				E.numeroEmpleado, 
				E.apellidosEmpleado, 
				E.nombresEmpleado, 
				E.direccionEmpleado,
				E.telefonoContacto, 
				E.gradoCocinero, 
				E.codigoTipoEmpleado
                From Empleados E
					Where E.codigoEmpleado = codEmpleado; 
        End//
Delimiter ; 
-- -**********************************************SERVICIO_HAS_EMPLEADOS*******************************************
-- -------------------------------------------------Agregar Servicio_has_Empleado---------------------------------------
Delimiter //
	create procedure sp_AgregarServicio_has_Empleado( in codigoServicio int,
		in codigoEmpleado int , in fechaEvento date ,in  horaEvento time, in lugarEvento varchar(150))
        Begin
			Insert into Servicios_has_Empleados( codigoServicio, 
				codigoEmpleado, fechaEvento, horaEvento, lugarEvento) values 
                ( codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End//
Delimiter ; 

-- ---------------------------------------------------Editar Servicio_has_Empleado-------------------------------------------
Delimiter //
	Create procedure sp_EditarServicio_has_Empleado(in Servicios_codServicio int, in fechaEvent date,
		in  horEvento time, in lugEvento varchar(150))
        Begin
			Update Servicios_has_Empleados SE 
				set
					SE.fechaEvento = fechaEvent, 
                    SE.horaEvento = horEvento, 
                    SE.lugarEvento = lugEvento
                    Where SE.Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ; 
-- ------------------------------------------------Eliminar Servicio_has_Empleado---------------------------------------------
Delimiter //
	Create procedure sp_EliminarServicio_has_Empleado(in Servicios_codServicio int )
		Begin
			Delete from Servicios_has_Empleados 
				where Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ; 
-- --------------------------------------------------Listar Servicios_has_Empleados-------------------------------------------
Delimiter //
	Create procedure sp_ListarServicios_has_Empleados()
		Begin
			Select
            SE.Servicios_codigoServicio,
            SE.codigoServicio, 
            SE.codigoEmpleado,
            SE.fechaEvento,
            SE.horaEvento,
            SE.lugarEvento
            From Servicios_has_Empleados SE; 
				
        End//
Delimiter ; 
call sp_ListarServicios_has_Empleados; 
-- ------------------------------------------------------Buscar Servicio_has_Empleado---------------------------------------
Delimiter //
	Create procedure sp_BuscarServicio_has_Empleado(in Servicios_codServicio int)
		Begin
			Select
            SE.Servicios_codigoServicio,
            SE.codigoServicio, 
            SE.codigoEmpleado,
            SE.fechaEvento,
            SE.horaEvento,
            SE.lugarEvento
            From Servicios_has_Empleados SE
				Where SE.Servicios_codigoServicio = Servicios_codServicio; 
				
        End//
Delimiter ; 

Delimiter //
	Create procedure sp_ReporteServicio(in id int)
		Begin
			Select  DISTINCT 
				E.nombreEmpresa, 
                P.cantidadPresupuesto, 
                S.tipoServicio, 
                Em.nombresEmpleado, 
                Em.apellidosEmpleado, 
                Te.descripcion, 
                Pa.nombrePlato,
                Pa.cantidad,
                Tp.descripcionPlato, 
                Pr.nombreProducto,
                Pr.cantidadProducto
                From Empresas E 
					inner join Presupuestos P on P.codigoEmpresa = E.codigoEmpresa
						inner join Servicios S on S.codigoEmpresa = E.codigoEmpresa 
							inner join Servicios_has_Empleados Se on Se.codigoServicio = S.codigoServicio 
								inner join Empleados Em on Em.codigoEmpleado = Se.codigoEmpleado
									inner join TipoEmpleado Te on Te.codigoTipoEmpleado = Em.codigoTipoEmpleado 
										inner join Servicios_has_Platos Shp on Shp.codigoServicio = S.codigoServicio
											inner join Platos Pa on Pa.codigoPlato = Shp.codigoPlato
												inner join TipoPlato Tp on Tp.codigoTipoPlato = Pa.codigoTipoPlato 
													inner join Productos_has_Platos Php on Php.codigoPlato = Pa.codigoPlato
														inner join Productos Pr on Pr.codigoProducto = Php.codigoProducto
															Where E.codigoEmpresa = id
                                                           group by  Pr.nombreProducto ; 
			
        End//
Delimiter ; 


Delimiter //
	create procedure sp_ListarEmpleadosEmpresa(in codEmpresa int)
		Begin
			Select 
				EM.nombresEmpleado,
                EM.apellidosEmpleado, 
                Te.descripcion
                From Empleados EM 
					inner join Servicios_has_Empleados SE on EM.codigoEmpleado  = SE.codigoEmpleado
						inner join Servicios S on S.codigoServicio = SE.codigoServicio
							inner join Empresas E on E.codigoEmpresa = S.codigoEmpresa
								inner join TipoEmpleado Te on Te.codigoTipoEmpleado = Em.codigoTipoEmpleado
									where E.codigoEmpresa = codEmpresa; 
        
        End//
Delimiter ; 

Delimiter //
	create procedure sp_ReporteServiciosEmpleado( )
		Begin
			select
				Se.codigoServicio, 
				E.nombresEmpleado, 
				E.apellidosEmpleado,
				Se.fechaEvento, 
				Se.horaEvento, 
				Se.lugarEvento
				from Servicios_has_Empleados Se
                inner join Empleados E on Se.codigoEmpleado = E.codigoEmpleado;
		end// 
Delimiter ;

Delimiter //
	create procedure sp_ReporteEmpleados( )
		Begin
			Select 	
				T.descripcion,
                E.nombresEmpleado, 
                E.apellidosEmpleado, 
                E.telefonoContacto 
                from Empleados E inner join TipoEmpleado T 
                on E.codigoTipoEmpleado = T.codigoTipoEmpleado; 
		end// 
Delimiter ;


-- Empresa: 

call sp_AgregarEmpresa('Kinal', '6A av colonia Landivar', '55663322'); 
call sp_AgregarEmpresa('Señor Tornillo', '6ta av, 5ta calle zona 1', '58632104'); 
call sp_AgregarEmpresa('Bimbo', '5ta av, 2da calle zona 4', '85884400'); 
call sp_AgregarEmpresa('Intelaf', '7ma calle, 8va Av, zona 6', '63332210'); 
call sp_AgregarEmpresa('Clik', 'diagonal 6 zona 10', '84502006'); 

-- Presupuesto 
call sp_AgregarPresupuesto('2023-04-25', 500, 1);
call sp_AgregarPresupuesto('2023-02-21', 850, 2);
call sp_AgregarPresupuesto('2023-01-18', 1300, 3);
call sp_AgregarPresupuesto('2023-03-19', 1500, 4);
call sp_AgregarPresupuesto('2023-05-20', 1260, 5);

-- tipoEmpleado 
call sp_AgregarTipoEmpleado('Cocinero'); 
call sp_AgregarTipoEmpleado('Mesero'); 
call sp_AgregarTipoEmpleado('Lavador'); 
call sp_AgregarTipoEmpleado('Chef asistente'); 
call sp_AgregarTipoEmpleado('Organizador'); 

-- TipoPlato 
call sp_AgregarTipoPlato('Comida Japonesa'); 
call sp_AgregarTipoPlato('Comida China');
call sp_AgregarTipoPlato('Comida Guatemalteca');  
call sp_AgregarTipoPlato('Comida Francesa'); 
call sp_AgregarTipoPlato('Comida Italiana'); 

-- Producto 
call sp_AgregarProducto('Fideos', 25);
call sp_AgregarProducto('Pollo', 30);
call sp_AgregarProducto('Tomates', 50);
call sp_AgregarProducto('Queso', 30);
call sp_AgregarProducto('huevos', 65);
call sp_AgregarProducto('Chuleta de Cerdo', 40);
call sp_AgregarProducto('Fresco de jamaica', 50);
call sp_AgregarProducto('Cebollín', 20);
call sp_AgregarProducto('verengena', 25);
call sp_AgregarProducto('zucchini', 25);
call sp_AgregarProducto('Brocoli', 25);
call sp_AgregarProducto('Arroz', 15);
call sp_AgregarProducto('Salsa pesto', 18);
call sp_AgregarProducto('Salchicha', 26);
call sp_AgregarProducto('Longaniza', 25);
call sp_AgregarProducto('Chorizo', 27);
call sp_AgregarProducto('Aguacate', 34);
call sp_AgregarProducto('Pan', 31);

-- Platos 
call sp_AgregarPlato(35, 'Ramen', 'Plato de fideos con sopa, cebollín, huevo cocido, chuleta de cerdo y fresco', 42.50, 1);
call sp_AgregarPlato(25, 'Ratatouille', 'Estofado de diferentes vegetales, ',55, 4);
call sp_AgregarPlato(35, 'wok de vegetales', 'variedad de vegetales salteados, pollo y arroz, fresco', 35, 2);
call sp_AgregarPlato(30, 'Pasta pesto', 'fideos acompañados de una salsa pesto', 38, 5);
call sp_AgregarPlato(25, 'Shuko', 'Pan dorado en las brazas con salchicha, chorizo o longaniza, guacamole, fresco', 18, 3);

-- Productos_Has_Platos
call sp_AgregarProducto_has_Plato(1, 1 ); 
call sp_AgregarProducto_has_Plato(1, 8 ); 
call sp_AgregarProducto_has_Plato(1, 5 ); 
call sp_AgregarProducto_has_Plato(1, 6 ); 
call sp_AgregarProducto_has_Plato(1, 7 ); 


-- Servicios 
call sp_AgregarServicio('2023-05-28', 'Almuerzo', '13:30:00', '6A av colonia Landivar', '55447711', 1 ); 
call sp_AgregarServicio('2023-05-29', 'Cena', '08:00:00', '6ta av, 5ta calle zona 1', '25623001', 2 ); 
call sp_AgregarServicio('2023-06-25', 'Cena', '12:30:00', '5ta av, 2da calle zona 4', '85602014', 3 ); 
call sp_AgregarServicio('2023-06-01', 'buffet', '19:00:00', '7ma calle, 8va Av, zona 6', '55412230', 4 ); 
call sp_AgregarServicio('2023-06-05', 'A la carta', '16:00:00', '12 AV, 6ta calle zona 12', '30314171', 5 ); 

-- SERVICIOS_HAS_PLATOS
call sp_AgregarServicio_has_Plato( 1, 1);
call sp_AgregarServicio_has_Plato( 2, 3);
call sp_AgregarServicio_has_Plato( 3, 2);
call sp_AgregarServicio_has_Plato( 4, 4);
call sp_AgregarServicio_has_Plato( 5, 2);

-- Empleados 
call sp_AgregarEmpleado(1, 'Cortez', 'Carlos', '8 Avenida 3-17 zona 6', '44558833', 'Experto', 1);
call sp_AgregarEmpleado(2, 'Sac ', 'Fernando', '5ta calle A, 8va AV, zona 1', '88550120', '', 2);
call sp_AgregarEmpleado(3, 'Moralez', 'Estuardo', '2a.Avenida 6-98, zona 5', '84632501', '', 3);
call sp_AgregarEmpleado(4, 'Gonzales', 'Alex', '6A Calle 23-21, zona 21', '04785260', 'Principiante', 4);
call sp_AgregarEmpleado(5, 'Prado', 'José', '3A Calle 0−43, zona 3', '90955641', '', 5);
 
 -- SERVICIO_HAS_EMPLEADOS
call sp_AgregarServicio_has_Empleado( 1, 1, '2023-05-28', '13:00:00', '6A av colonia Landivar');
call sp_AgregarServicio_has_Empleado( 1, 2, '2023-05-28', '13:00:00', '6A av colonia Landivar');
call sp_AgregarServicio_has_Empleado( 1, 3, '2023-05-28', '13:00:00', '6A av colonia Landivar');
call sp_AgregarServicio_has_Empleado( 1, 4, '2023-05-28', '13:00:00', '6A av colonia Landivar');
call sp_AgregarServicio_has_Empleado( 1, 5, '2023-05-28', '13:00:00', '6A av colonia Landivar');

call sp_AgregarServicio_has_Empleado( 2, 1, '2023-05-29', '08:00:00', '6ta av, 5ta calle zona 1');

 
 -- CONSULTA REPORTE FINAL; 
 call sp_ReporteServicio(1); 
 call sp_ListarEmpleadosEmpresa(1); 
Select * from Usuario; 



Delimiter //
create procedure sp_contarServicios()
	Begin
		Select distinct
			 count(S.tipoServicio) as tipoServicio,
            E.nombreEmpresa
				from Servicios S inner join Empresas E on E.codigoEmpresa = S.codigoEmpresa 
                group by(E.nombreEmpresa);
	end// 
Delimiter ; 
Delimiter //
create procedure sp_contarPresupuesto()
	Begin
		Select 
			 count(P.fechaSolicitud) as cantidadPresupuesto,
            E.nombreEmpresa
				from Presupuestos P inner join Empresas E on E.codigoEmpresa = P.codigoEmpresa 
                group by(E.nombreEmpresa);
	end// 
Delimiter ; 

Delimiter //
	create procedure sp_ContarTipoServicio()
		Begin
			select
				count(S.fechaServicio) as fechaServicio, 
                S.tipoServicio, 
                E.nombreEmpresa
                from Servicios S inner join Empresas E on E.codigoEmpresa = S.codigoEmpresa 
                group by(S.tipoServicio);
		end//
Delimiter ;

Delimiter //
	create procedure sp_ContarPlatos()
		Begin
			Select 
				count(SP.Servicios_codigoServicio) as cantidad,
				P.nombrePlato 
				from Servicios_has_Platos SP inner join Platos P
					on SP.Servicios_codigoServicio = P.codigoPlato
				group by P.nombrePlato; 
		end//
Delimiter ;

Delimiter //
	create procedure sp_ContarProductos()
		Begin
			Select 
				count(SP.Productos_codigoProducto) as cantidad,
				P.nombreProducto 
				from Productos_has_Platos SP inner join Productos P
					on SP.Productos_codigoProducto = P.codigoProducto
				group by P.nombreProducto; 
		end//
Delimiter ;
call sp_ContarProductos; 


Delimiter //
	create procedure sp_ReporteProductos()
		Begin
			Select 
				P.nombreProducto, 
                P.cantidadProducto, 
                PA.nombrePlato 
                from Productos P inner join Productos_has_Platos PH on P.codigoProducto = PH.codigoProducto
                inner join Platos PA on PA.codigoPlato = PH.codigoPlato;
					
		END//
Delimiter ; 
                
call sp_ReporteProductos();

Delimiter //
	create procedure sp_Menu()
    Begin
		select 
			P.nombrePlato, 
			P.descripcionPlato, 
            P.precioPlato, 
            T.descripcionPlato as descripcion
            From Platos P inner join TipoPlato T
				on P.codigoTipoPlato = T.codigoTipoPlato;
    End//
Delimiter ; 
call sp_Menu();