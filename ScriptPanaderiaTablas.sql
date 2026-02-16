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

-- Tabla Clientes
CREATE TABLE Clientes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50),
    fecha_nacimiento DATE NOT NULL
);

-- Tabla Direcciones
CREATE TABLE Direcciones(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id),
    calle VARCHAR(50) NOT NULL,
    colonia VARCHAR(50) NOT NULL,
    cp INT NOT NULL,
    numero INT NOT NULL
);

-- Tabla Telefonos
CREATE TABLE Telefonos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id),
    telefono VARCHAR(15) NOT NULL,
    etiqueta VARCHAR(20)
);

-- Tabla Pedidos
CREATE TABLE Pedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    estado ENUM("Pendiente", "En preparación", "Listo", "Entregado", "Cancelado", "No reclamado") NOT NULL,
    fecha DATE NOT NULL
);

-- Tabla PedidosExpress
CREATE TABLE PedidosExpress(
	id INT PRIMARY KEY AUTO_INCREMENT,
    pin INT NOT NULL,
    folio VARCHAR(10) NOT NULL
);

-- Tabla PedidosProgramados
CREATE TABLE PedidosProgramados(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nota VARCHAR(100),
    numero_pedido INT NOT NULL
);

-- Tabla ProductosPedidos
CREATE TABLE ProductosPedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id),
    id_producto INT NOT NULL,
    FOREIGN KEY(id_producto) REFERENCES Productos(id)
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