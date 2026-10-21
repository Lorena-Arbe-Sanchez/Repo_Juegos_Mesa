-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3307
-- Tiempo de generación: 15-10-2026 a las 13:55:47
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

--
-- Volcado de datos para la tabla `asociacion`
--

INSERT INTO `asociacion` (`id`, `nombre`, `razon_social`, `fecha_apertura`, `direccion`, `telefono`, `email`, `cif`) VALUES
(1, 'Meeple Vitoria', 'Asociación de Juegos Meeple Vitoria', '2022-05-15', 'Calle Dato 15, Vitoria-Gasteiz', '945123456', 'info@meeplevitoria.es', 'G01012345');

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

--
-- Volcado de datos para la tabla `invitada`
--

INSERT INTO `invitada` (`documento`, `nombre`, `apellido1`, `apellido2`, `telefono`, `edad`, `email`) VALUES
('11111111A', 'Irene', 'Ruiz', 'Sanz', '611111111', 23, 'irene@example.com'),
('22222222B', 'Jon', 'Etxeberria', NULL, '622222222', 27, 'jon@example.com'),
('33333333C', 'Leire', 'García', 'López', '633333333', 35, 'leire@example.com');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `juego`
--

INSERT INTO `juego` (`id`, `nombre`, `autor1`, `autor2`, `descripcion`, `min_jugadores`, `max_jugadores`, `duracion`, `tematica`, `competitivo`, `ubicacion`, `estado`, `asociacion_id`, `socia_documento`, `fecha_alta`) VALUES
(1, 'Monopoly', 'Charles Darrow', NULL, 'Juego de compraventa de propiedades, alquileres y gestión de dinero.', 2, 8, 120, 'Economía', 1, 'Estantería 1 (>12 años)', 'disponible', 1, NULL, '2023-01-10'),
(2, 'Trivial Pursuit', 'Chris Haney', 'Scott Abbott', 'Juego de preguntas de cultura general para conseguir los quesitos.', 2, 6, 60, 'Cultura general', 1, 'Estantería 1 (>12 años)', 'disponible', 1, NULL, '2023-02-15'),
(3, 'UNO', 'Merle Robbins', NULL, 'Juego de cartas en el que hay que quedarse sin cartas antes que los demás.', 2, 10, 20, 'Cartas', 1, 'Estantería 2 (5-11 años)', 'disponible', 1, NULL, '2023-04-20'),
(4, 'Scrabble', 'Alfred Mosher Butts', NULL, 'Juego de palabras en el que se consiguen puntos formando palabras con fichas.', 2, 4, 60, 'Palabras', 1, 'Estantería 1 (>12 años)', 'disponible', 1, NULL, '2024-01-05'),
(5, 'Parchís', 'Tradicional', NULL, 'Juego de recorrido en el que hay que llevar las cuatro fichas hasta la meta.', 2, 4, 45, 'Familiar', 1, 'Estantería 2 (5-11 años)', 'disponible', NULL, '12345678A', '2024-03-12'),
(6, 'Cluedo', 'Anthony E. Pratt', NULL, 'Juego de deducción para descubrir quién cometió el asesinato, dónde y con qué arma.', 2, 6, 60, 'Misterio', 1, 'Estantería 1 (>12 años)', 'en reparación', NULL, '23456789B', '2024-06-18'),
(7, 'Risk', 'Albert Lamorisse', NULL, 'Juego de estrategia y conquista territorial mediante ejércitos y dados.', 2, 6, 150, 'Estrategia militar', 1, 'Estantería 1 (>12 años)', 'disponible', 1, NULL, '2025-02-01'),
(8, 'Jenga', 'Leslie Scott', NULL, 'Juego de habilidad en el que se retiran bloques de una torre sin derribarla.', 2, 8, 20, 'Habilidad', 1, 'Estantería 2 (5-11 años)', 'disponible', 1, NULL, '2025-05-22');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partida`
--

INSERT INTO `partida` (`id`, `juego_id`, `fecha`, `hora`, `jugadores_deseados`, `plazas_libres`) VALUES
(1, 1, '2026-10-16', '17:00:00', 6, 2),
(2, 2, '2026-10-17', '18:00:00', 4, 1),
(3, 3, '2026-10-18', '17:30:00', 5, 2),
(4, 5, '2026-10-20', '19:00:00', 4, 1),
(5, 7, '2026-10-22', '17:00:00', 6, 2),
(6, 8, '2026-10-24', '18:30:00', 4, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_invitada`
--

CREATE TABLE `partida_invitada` (
  `partida_id` int(11) NOT NULL,
  `invitada_documento` varchar(20) NOT NULL,
  `es_ganadora` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partida_invitada`
--

INSERT INTO `partida_invitada` (`partida_id`, `invitada_documento`, `es_ganadora`) VALUES
(1, '11111111A', 0),
(2, '22222222B', 0),
(3, '33333333C', 0),
(4, '11111111A', 0),
(5, '22222222B', 0),
(6, '33333333C', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_socia`
--

CREATE TABLE `partida_socia` (
  `partida_id` int(11) NOT NULL,
  `socia_documento` varchar(20) NOT NULL,
  `es_ganadora` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partida_socia`
--

INSERT INTO `partida_socia` (`partida_id`, `socia_documento`, `es_ganadora`) VALUES
(1, '12345678A', 1),
(1, '23456789B', 0),
(1, '34567890C', 0),
(2, '45678901D', 1),
(2, '56789012E', 0),
(3, '12345678A', 0),
(3, '23456789B', 1),
(4, '34567890C', 1),
(4, '45678901D', 0),
(5, '12345678A', 0),
(5, '34567890C', 1),
(5, '56789012E', 0),
(6, '23456789B', 1),
(6, '45678901D', 0);

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
-- Volcado de datos para la tabla `socia`
--

INSERT INTO `socia` (`documento`, `nombre`, `apellido1`, `apellido2`, `telefono`, `edad`, `email`) VALUES
('12345678A', 'Pablo', 'Sáenz', 'Pérez', '600111222', 25, 'pablo@example.com'),
('23456789B', 'Ane', 'Rodríguez', 'Díaz', '600222333', 21, 'ane@example.com'),
('34567890C', 'Nerea', 'López', 'Fernández', '600333444', 37, 'nerea@example.com'),
('45678901D', 'Unai', 'González', NULL, '600444555', 58, 'unai@example.com'),
('56789012E', 'Marta', 'Martínez', 'Pérez', '600555666', 70, 'marta@example.com');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `juego`
--
ALTER TABLE `juego`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `partida`
--
ALTER TABLE `partida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

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
