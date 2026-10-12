# 🎲 Gestión de una Asociación de Juegos de Mesa

## 📌 Descripción del proyecto

Este proyecto consiste en el desarrollo de una aplicación de consola en Java para gestionar una asociación de juegos de mesa de Vitoria-Gasteiz.

La aplicación utiliza una base de datos relacional para almacenar y gestionar la información de la asociación, sus juegos de mesa, las socias y las partidas organizadas. La conexión con la base de datos se realiza mediante JDBC, aplicando los conocimientos adquiridos en el módulo de **Acceso a Datos**.

## 🎯 Objetivos

- Gestionar los juegos de mesa disponibles en la asociación.
- Registrar y administrar las socias.
- Crear y gestionar partidas, así como las personas inscritas en ellas.
- Consultar la información y las relaciones entre los distintos elementos.
- Exportar los datos a ficheros XML y JSON.
- Consultar los metadatos de la base de datos, sus tablas, claves primarias y columnas.
- Aplicar buenas prácticas de programación y una estructura de código organizada.

## ⚙️ Tecnologías utilizadas

- **Java:** desarrollo de la aplicación.
- **JDBC:** conexión y comunicación con la base de datos.
- **MariaDB / MySQL:** sistema gestor de bases de datos.
- **IntelliJ IDEA:** entorno de desarrollo.
- **XML y JSON:** formatos de exportación de datos.
- **Docker:** posible herramienta para desplegar el servidor de base de datos.

## 🗂️ Funcionalidades principales

### 🎲 Gestión de juegos de mesa

Permite administrar la información de los juegos de la asociación, incluyendo su nombre, autores, descripción, número mínimo y máximo de jugadores, duración, temática, ubicación, estado, propiedad y fecha de alta.

### 👥 Gestión de socias

Permite registrar y consultar la información de las socias, incluyendo sus datos identificativos y de contacto. También permite consultar los juegos asociados a cada socia y las partidas en las que participa.

### 🃏 Gestión de partidas

Permite gestionar las partidas organizadas por la asociación, indicando el juego utilizado, la fecha, la hora, el número de jugadoras deseado, las plazas disponibles y las personas inscritas. También contempla el registro de las ganadoras cuando corresponda.

### 🏠 Gestión de la asociación

Permite consultar y modificar los datos identificativos y de contacto de la asociación.

### 📄 Exportación de datos

Los datos de juegos de mesa, socias y partidas pueden exportarse a ficheros XML y JSON para facilitar su almacenamiento e intercambio.

### 🗄️ Metadatos de la base de datos

La aplicación contempla la consulta de información sobre la conexión y la estructura de la base de datos, incluyendo sus tablas, claves primarias y columnas.

## 🛠️ Requisitos previos

Para ejecutar el proyecto se necesita:

- Java JDK compatible con la versión utilizada en el proyecto.
- IntelliJ IDEA o un entorno de desarrollo compatible.
- Un servidor MariaDB o MySQL accesible por red.
- La base de datos y las tablas necesarias para el funcionamiento de la aplicación.
- El conector JDBC correspondiente.
- Las dependencias adicionales necesarias para la exportación XML y JSON, si se utilizan librerías externas.

## 🚀 Puesta en marcha

1. Clonar o descargar este repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Configurar el servidor MariaDB o MySQL.
4. Ejecutar los scripts SQL incluidos en el proyecto para crear la base de datos y las tablas.
5. Configurar los parámetros de conexión JDBC para indicar la URL del servidor, el nombre de la base de datos, el usuario y la contraseña.
6. Comprobar que las dependencias necesarias están disponibles.
7. Ejecutar la clase principal de la aplicación.

**Importante:** la base de datos debe estar alojada en un servidor accesible por red, no únicamente en la máquina cliente.

## 📁 Estructura del proyecto

La estructura definitiva dependerá de la organización del código fuente. El repositorio incluirá los elementos necesarios para ejecutar y mantener la aplicación, como:

- Código fuente Java.
- Scripts SQL para la creación y preparación de la base de datos.
- Dependencias necesarias para la conexión JDBC y la exportación de datos.
- Ficheros de configuración, cuando corresponda.
- Documentación del proyecto y manual de puesta en marcha.

## 🗃️ Base de datos

El modelo de datos se organiza en torno a las siguientes entidades principales:

- **Juegos de mesa:** información y características de los juegos.
- **Socias:** datos personales y de contacto de las integrantes de la asociación.
- **Partidas:** información sobre las sesiones de juego organizadas.
- **Asociación:** datos identificativos y de contacto de la entidad.

Las relaciones entre estas entidades permiten gestionar la propiedad de los juegos, la organización de las partidas y la participación de las jugadoras, manteniendo la integridad referencial.

El modelo entidad-relación y las decisiones de diseño se documentarán en la memoria del proyecto.

## 📚 Documentación

La memoria del proyecto recogerá:

- Los requisitos y la puesta en marcha de la aplicación.
- Las principales funcionalidades implementadas.
- El modelo entidad-relación de la base de datos.
- Las decisiones de diseño adoptadas y su justificación.
- Los patrones de diseño utilizados.
- Las actas de seguimiento del desarrollo.

## 👩‍💻 Autores

- [Lorena Arbé Sánchez](https://github.com/Lorena-Arbe-Sanchez)
- [Aitor Amaury Farinango Rojas](https://github.com/AitorAmaury)

## 📅 Fecha de entrega

25 de octubre de 2026.

---

Proyecto académico de gestión de una asociación de juegos de mesa de Vitoria-Gasteiz.