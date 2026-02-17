-- Crear la base de datos
DROP DATABASE IF EXISTS Panaderia;
CREATE DATABASE Panaderia;
USE Panaderia;

-- Tabla de Ingredientes
CREATE TABLE Ingredientes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL
);

-- Tabla Productos
CREATE TABLE Productos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    tipo ENUM("Dulce", "Salado", "Integral") NOT NULL,
    precio FLOAT NOT NULL,
    estado ENUM("Disponible", "No disponible"),
    descripcion VARCHAR(100) NOT NULL
);

-- Tabla ProductosIngredientes
CREATE TABLE ProductosIngredientes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_producto INT NOT NULL,
    FOREIGN KEY(id_producto) REFERENCES Productos(id),
    id_ingrediente INT NOT NULL,
    FOREIGN KEY(id_ingrediente) REFERENCES Ingredientes(id)
);

-- Tabla Pedidos
CREATE TABLE Pedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    estado ENUM("Pendiente", "En preparación", "Listo", "Entregado", "Cancelado", "No reclamado") NOT NULL,
    fecha DATE NOT NULL
);

-- Tabla PedidosExpress
CREATE TABLE PedidosExpress(
	id_pedido INT PRIMARY KEY,
    pin INT NOT NULL,
    folio VARCHAR(10) NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id)
);

-- Tabla PedidosProgramados
CREATE TABLE PedidosProgramados(
	id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    numero_pedido INT NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id)
);

-- Tabla DetallesPedidos
CREATE TABLE DetallesPedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nota VARCHAR(100),
    cantidad INT NOT NULL,
    total FLOAT NOT NULL,
    id_pedido INT NOT NULL,
	FOREIGN KEY(id_pedido) REFERENCES Pedidos(id)
);

-- Tabla ProductosDetallesPedidos
CREATE TABLE ProductosDetallesPedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_producto INT NOT NULL,
    FOREIGN KEY(id_producto) REFERENCES Productos(id),
    id_detalle_pedido INT NOT NULL,
    FOREIGN KEY(id_detalle_pedido) REFERENCES DetallesPedidos(id)
);

-- Tabla usuarios
CREATE TABLE Usuarios(
	id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(20) NOT NULL,
    contrasenia VARCHAR(100) NOT NULL,
    rol ENUM("Cliente", "Empleado")
);

-- Tabla Clientes
CREATE TABLE Clientes(
	id_usuario INT NOT NULL,
    FOREIGN KEY(id_usuario) REFERENCES Usuarios(id),
    nombres VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50),
    fecha_nacimiento DATE NOT NULL
);

-- Tabla Direcciones
CREATE TABLE Direcciones(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id_usuario),
    calle VARCHAR(50) NOT NULL,
    colonia VARCHAR(50) NOT NULL,
    cp INT NOT NULL,
    numero INT NOT NULL
);

-- Tabla Telefonos
CREATE TABLE Telefonos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id_usuario),
    telefono VARCHAR(15) NOT NULL,
    etiqueta VARCHAR(20)
);

-- Tabla Empleados
CREATE TABLE Empleados(
	id_cliente INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id_usuario)
);


-- Tabla Cupones
CREATE TABLE Cupones(
	id INT PRIMARY KEY AUTO_INCREMENT,
    descuento INT NOT NULL,
    fecha_vencimiento DATE,
    nombre VARCHAR(30) NOT NULL,
    numero_usos INT NOT NULL
);

-- Tabla PedidosProgramadosCupones
CREATE TABLE PedidosProgramadosCupones(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id),
    id_cupon INT NOT NULL,
    FOREIGN KEY(id_cupon) REFERENCES Cupones(id)
);