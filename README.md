# SpeedFast - Semana 8

## Descripción

Proyecto desarrollado en Java para la gestión de pedidos de SpeedFast.

En esta semana se completó la integración entre la interfaz gráfica Swing, las clases DAO y la base de datos MySQL mediante JDBC, incorporando operaciones CRUD completas para pedidos, repartidores y entregas.

## Funcionalidades

### Gestión de pedidos
- Registrar pedidos.
- Listar pedidos almacenados en MySQL.
- Editar dirección, tipo y estado.
- Eliminar pedidos.
- Mostrar repartidor asociado.
- Actualizar la tabla desde la base de datos.

### Gestión de repartidores
- Registrar repartidores.
- Listar repartidores.
- Editar nombre.
- Eliminar repartidores.
- Actualizar la tabla desde MySQL.

### Gestión de entregas
- Asignar repartidores a pedidos.
- Registrar entregas.
- Listar entregas.
- Editar la relación entre pedido y repartidor.
- Eliminar entregas.
- Registrar fecha y hora de la entrega.

## CRUD

Se implementaron las cuatro operaciones principales de persistencia:

- CREATE: registrar nuevos datos.
- READ: consultar y listar datos.
- UPDATE: modificar registros existentes.
- DELETE: eliminar registros.

Las operaciones se implementan mediante las clases:

- `PedidoDAO`
- `RepartidorDAO`
- `EntregaDAO`

## Base de datos

Base de datos utilizada:

`speedfast_db`

Tablas principales:

- `pedido`
- `repartidor`
- `entrega`

La tabla `entrega` relaciona pedidos y repartidores mediante claves foráneas.

El proyecto incluye el archivo:

`speedfast_db.sql`

Este script permite crear la base de datos y las tablas necesarias para ejecutar la aplicación.

## JDBC

La aplicación se conecta a MySQL mediante JDBC utilizando:

- `Connection`
- `DriverManager`
- `PreparedStatement`
- `ResultSet`
- `executeQuery()`
- `executeUpdate()`

La clase `ConexionBD` centraliza la conexión con la base de datos.

## Integridad referencial

Las claves foráneas de la tabla `entrega` impiden eliminar pedidos o repartidores que todavía tengan entregas asociadas.

Para eliminar esos registros primero deben eliminarse las relaciones correspondientes en la tabla `entrega`.

## Interfaz gráfica

La aplicación utiliza Java Swing e incluye:

- `JFrame`
- `JPanel`
- `JButton`
- `JTable`
- `JScrollPane`
- `JOptionPane`
- `JComboBox`
- `JTextField`

Las tablas permiten visualizar los datos almacenados en MySQL y realizar operaciones de edición, eliminación y actualización.

## Estructura principal

- `main`: punto de inicio de la aplicación.
- `modelo`: clases del dominio.
- `dao`: acceso y persistencia de datos.
- `vista`: ventanas desarrolladas con Swing.

## Tecnologías

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Workbench
- Maven
- IntelliJ IDEA
- Git
- GitHub

## Ejecución

1. Tener MySQL Server iniciado.
2. Ejecutar el archivo `speedfast_db.sql` incluido en el proyecto para crear la base de datos `speedfast_db`, sus tablas, claves foráneas y restricciones.
3. Verificar las credenciales de conexión en `ConexionBD`.
4. Ejecutar la clase:

`main.Main`

## Autor

Proyecto académico desarrollado para Desarrollo Orientado a Objetos II.