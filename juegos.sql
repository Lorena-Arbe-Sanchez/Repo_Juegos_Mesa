-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-10-2026 a las 21:51:49
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `juegos`
--
CREATE DATABASE IF NOT EXISTS `juegos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `juegos`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asociacion`
--

CREATE TABLE `asociacion` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `razon_social` varchar(150) DEFAULT NULL,
  `fecha_apertura` date DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `cif` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `invitada`
--

CREATE TABLE `invitada` (
  `documento` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido1` varchar(50) NOT NULL,
  `apellido2` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `juego`
--

CREATE TABLE `juego` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `autor1` varchar(100) NOT NULL,
  `autor2` varchar(100) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `min_jugadores` int(11) NOT NULL,
  `max_jugadores` int(11) NOT NULL,
  `duracion` int(11) NOT NULL,
  `tematica` varchar(100) DEFAULT NULL,
  `competitivo` tinyint(1) NOT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  `estado` enum('disponible','prestado','en reparación') NOT NULL DEFAULT 'disponible',
  `asociacion_id` int(11) DEFAULT NULL,
  `socia_documento` varchar(20) DEFAULT NULL,
  `fecha_alta` date DEFAULT NULL
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida`
--

CREATE TABLE `partida` (
  `id` int(11) NOT NULL,
  `juego_id` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `jugadores_deseados` int(11) NOT NULL,
  `plazas_libres` int(11) NOT NULL
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_invitada`
--

CREATE TABLE `partida_invitada` (
  `partida_id` int(11) NOT NULL,
  `invitada_documento` varchar(20) NOT NULL,
  `es_ganadora` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_socia`
--

CREATE TABLE `partida_socia` (
  `partida_id` int(11) NOT NULL,
  `socia_documento` varchar(20) NOT NULL,
  `es_ganadora` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socia`
--

CREATE TABLE `socia` (
  `documento` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido1` varchar(50) NOT NULL,
  `apellido2` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asociacion`
--
ALTER TABLE `asociacion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cif` (`cif`);

--
-- Indices de la tabla `invitada`
--
ALTER TABLE `invitada`
  ADD PRIMARY KEY (`documento`);

--
-- Indices de la tabla `juego`
--
ALTER TABLE `juego`
  ADD PRIMARY KEY (`id`),
  ADD KEY `asociacion_id` (`asociacion_id`),
  ADD KEY `socia_documento` (`socia_documento`);

--
-- Indices de la tabla `partida`
--
ALTER TABLE `partida`
  ADD PRIMARY KEY (`id`),
  ADD KEY `juego_id` (`juego_id`);

--
-- Indices de la tabla `partida_invitada`
--
ALTER TABLE `partida_invitada`
  ADD PRIMARY KEY (`partida_id`,`invitada_documento`),
  ADD KEY `invitada_documento` (`invitada_documento`);

--
-- Indices de la tabla `partida_socia`
--
ALTER TABLE `partida_socia`
  ADD PRIMARY KEY (`partida_id`,`socia_documento`),
  ADD KEY `socia_documento` (`socia_documento`);

--
-- Indices de la tabla `socia`
--
ALTER TABLE `socia`
  ADD PRIMARY KEY (`documento`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asociacion`
--
ALTER TABLE `asociacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `juego`
--
ALTER TABLE `juego`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `partida`
--
ALTER TABLE `partida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `juego`
--
ALTER TABLE `juego`
  ADD CONSTRAINT `juego_ibfk_1` FOREIGN KEY (`asociacion_id`) REFERENCES `asociacion` (`id`),
  ADD CONSTRAINT `juego_ibfk_2` FOREIGN KEY (`socia_documento`) REFERENCES `socia` (`documento`);

--
-- Filtros para la tabla `partida`
--
ALTER TABLE `partida`
  ADD CONSTRAINT `partida_ibfk_1` FOREIGN KEY (`juego_id`) REFERENCES `juego` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `partida_invitada`
--
ALTER TABLE `partida_invitada`
  ADD CONSTRAINT `partida_invitada_ibfk_1` FOREIGN KEY (`partida_id`) REFERENCES `partida` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `partida_invitada_ibfk_2` FOREIGN KEY (`invitada_documento`) REFERENCES `invitada` (`documento`);

--
-- Filtros para la tabla `partida_socia`
--
ALTER TABLE `partida_socia`
  ADD CONSTRAINT `partida_socia_ibfk_1` FOREIGN KEY (`partida_id`) REFERENCES `partida` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `partida_socia_ibfk_2` FOREIGN KEY (`socia_documento`) REFERENCES `socia` (`documento`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
