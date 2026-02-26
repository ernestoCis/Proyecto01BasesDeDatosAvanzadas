-- Crear la base de datos
DROP DATABASE IF EXISTS Panaderia;
CREATE DATABASE Panaderia;
USE Panaderia;

-- Tabla Productos
CREATE TABLE Productos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    tipo ENUM("Dulce", "Salado", "Integral") NOT NULL,
    precio FLOAT NOT NULL,
    estado ENUM("Disponible", "No disponible"),
    descripcion VARCHAR(100) NOT NULL
);
INSERT INTO Productos(nombre, tipo, precio, estado, descripcion) values("Concha", "Dulce", 18, "Disponible", "Cocncha de vainilla");

-- Tabla Cupones
CREATE TABLE Cupones(
	id INT PRIMARY KEY AUTO_INCREMENT,
    descuento INT NOT NULL,
    fecha_vencimiento DATE,
    fecha_inicio DATE NOT NULL,
    nombre VARCHAR(30) UNIQUE NOT NULL,
    numero_usos INT NOT NULL DEFAULT 0,
    tope_usos INT NOT NULL
);
INSERT INTO Cupones(descuento, fecha_vencimiento, fecha_inicio, nombre, tope_usos) VALUES(10, "2026-03-15", "2026-01-01", "PAN10", 10);

-- Tabla usuarios
CREATE TABLE Usuarios(
	id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(20) NOT NULL,
    contrasenia VARCHAR(100) NOT NULL,
    rol ENUM("Cliente", "Empleado") NOT NULL
);

-- Tabla Clientes
CREATE TABLE Clientes(
	id_usuario INT NOT NULL,
    FOREIGN KEY(id_usuario) REFERENCES Usuarios(id),
    nombres VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50),
    fecha_nacimiento DATE NOT NULL,
    estado ENUM("Activo", "Inactivo") NOT NULL DEFAULT "Activo"
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
	id_usuario INT NOT NULL,
    FOREIGN KEY(id_usuario) REFERENCES Usuarios(id)
);

-- Tabla Pedidos
CREATE TABLE Pedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    estado ENUM("Pendiente", "Listo", "Entregado", "Cancelado", "No reclamado") NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_entrega DATETIME,
    metodo_pago ENUM("Efectivo", "Credito", "Debito") NOT NULL,
    total FLOAT NOT NULL,
    numero_pedido INT UNIQUE NOT NULL,
    id_cliente INT,
    FOREIGN KEY(id_cliente) REFERENCES Clientes(id_usuario) 
);

-- Tabla PedidosExpress
CREATE TABLE PedidosExpress(
	id_pedido INT PRIMARY KEY,
    pin VARCHAR(100) NOT NULL,
    folio VARCHAR(10) NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id)
);

-- Tabla PedidosProgramados
CREATE TABLE PedidosProgramados(
	id_pedido INT PRIMARY KEY,
    id_cupon INT,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id),
    FOREIGN KEY(id_cupon) REFERENCES Cupones(id)
);

-- Tabla DetallesPedidos
CREATE TABLE DetallesPedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nota VARCHAR(100),
    cantidad INT NOT NULL,
    precio FLOAT NOT NULL,
    total FLOAT NOT NULL,
    id_pedido INT NOT NULL,
	FOREIGN KEY(id_pedido) REFERENCES Pedidos(id),
    id_producto INT NOT NULL,
    FOREIGN KEY(id_producto) REFERENCES Productos(id)
);

-- Tabla HistorialCambios
CREATE TABLE HistorialCambios(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    FOREIGN KEY(id_pedido) REFERENCES Pedidos(id),
    id_usuario INT NOT NULL,
    FOREIGN KEY(id_usuario) REFERENCES Usuarios(id),
    estado ENUM("Pendiente", "Listo", "Entregado", "Cancelado", "No reclamado"),
    fecha_cambio DATETIME NOT NULL DEFAULT NOW()
);

DELIMITER $$

CREATE TRIGGER trg_pedidos_set_fecha_entrega
BEFORE UPDATE ON Pedidos
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Entregado' AND OLD.estado <> 'Entregado' THEN
        SET NEW.fecha_entrega = CURRENT_DATE();
    END IF;
END$$

DELIMITER ;
