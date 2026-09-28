# SpeedFast - Semana 7

## Descripción

Proyecto desarrollado en Java para la gestión de pedidos de SpeedFast.

En esta semana se incorporó persistencia de datos mediante MySQL y JDBC, permitiendo que la interfaz gráfica se comunique directamente con una base de datos relacional.

## Funcionalidades

- Registrar pedidos desde la interfaz gráfica.
- Registrar repartidores desde la interfaz gráfica.
- Guardar pedidos y repartidores en MySQL.
- Consultar pedidos almacenados en la base de datos.
- Mostrar los pedidos mediante JTable.
- Asignar repartidores a pedidos.
- Registrar entregas relacionando pedidos y repartidores.
- Actualizar el estado de los pedidos a EN_REPARTO.
- Mostrar el repartidor asociado a cada pedido.

## Base de datos

Base de datos utilizada:

`speedfast_db`

Tablas:

- `pedido`
- `repartidor`
- `entrega`

La tabla `entrega` relaciona los pedidos con los repartidores mediante claves foráneas.

## JDBC

La aplicación utiliza JDBC para conectar Java con MySQL mediante MySQL Connector/J.

Se implementaron las siguientes clases DAO:

- `ConexionBD`
- `PedidoDAO`
- `RepartidorDAO`
- `EntregaDAO`

Las operaciones utilizan `PreparedStatement`, `ResultSet` y manejo de excepciones SQL.

## Estructura principal

- `main`: punto de inicio de la aplicación.
- `modelo`: clases del modelo de SpeedFast.
- `vista`: ventanas desarrolladas con Java Swing.
- `dao`: clases encargadas del acceso a la base de datos.

## Interfaz gráfica

La aplicación utiliza Java Swing e incluye:

- Registro de pedidos.
- Registro de repartidores.
- Listado de pedidos mediante JTable.
- Asignación de repartidores e inicio de entregas.

## Tecnologías

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Workbench
- Maven
- IntelliJ IDEA

## Ejecución

1. Tener MySQL Server iniciado.
2. Crear la base de datos `speedfast_db` y sus tablas.
3. Configurar las credenciales de conexión en `ConexionBD`.
4. Ejecutar:

`main.Main`