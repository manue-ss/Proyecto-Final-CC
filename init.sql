-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         12.3.2-MariaDB - MariaDB Server
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.17.0.7270
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para autorescate_bd
CREATE DATABASE IF NOT EXISTS `autorescate_bd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `autorescate_bd`;

-- Volcando estructura para tabla autorescate_bd.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(50) NOT NULL COMMENT 'particular, empresa de transporte, aseguradora',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.clientes: ~3 rows (aproximadamente)
INSERT INTO `clientes` (`id`, `nombre`, `tipo`) VALUES
	(1, 'Transportes Macarena', 'empresa de transporte'),
	(2, 'Seguros Salazar S.A.', 'aseguradora'),
	(3, 'Andrés Castro', 'particular');

-- Volcando estructura para tabla autorescate_bd.historialoperaciones
CREATE TABLE IF NOT EXISTS `historialoperaciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operacion` varchar(100) NOT NULL COMMENT 'asignación, cambio estado unidad, etc',
  `entidad_afectada` varchar(50) NOT NULL COMMENT 'unidadesservicio, tecnicos, solicitudes',
  `id_afectado` varchar(36) NOT NULL,
  `estado_anterior` varchar(255) DEFAULT NULL,
  `estado_nuevo` varchar(255) DEFAULT NULL,
  `fecha_hora` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.historialoperaciones: ~2 rows (aproximadamente)
INSERT INTO `historialoperaciones` (`id`, `operacion`, `entidad_afectada`, `id_afectado`, `estado_anterior`, `estado_nuevo`, `fecha_hora`) VALUES
	(1, 'ASIGNACION_RECURSOS', 'solicitudes', '2', 'pendiente', 'en ejecución', '2026-06-01 02:14:30'),
	(2, 'CAMBIO_ESTADO', 'unidadesservicio', '33333333-4444-5555-6666-777777777777', 'disponible', 'en mantenimiento', '2026-06-01 02:14:30');

-- Volcando estructura para tabla autorescate_bd.kits_repuestos
CREATE TABLE IF NOT EXISTS `kits_repuestos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `tipo` varchar(50) NOT NULL COMMENT 'kit de atención rápida o repuesto',
  `estado` varchar(50) NOT NULL COMMENT 'en circulación, en revisión o listo',
  `fecha_ingreso` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.kits_repuestos: ~4 rows (aproximadamente)
INSERT INTO `kits_repuestos` (`id`, `codigo`, `tipo`, `estado`, `fecha_ingreso`) VALUES
	(1, 'KIT-001', 'kit de atención rápida', 'listo', '2026-06-01 02:14:30'),
	(2, 'KIT-002', 'kit de atención rápida', 'en revisión', '2026-06-01 02:14:30'),
	(3, 'REP-BATER-01', 'repuesto', 'en circulación', '2026-06-01 02:14:30'),
	(4, 'REP-LLANTA-01', 'repuesto', 'listo', '2026-06-01 02:14:30');

-- Volcando estructura para tabla autorescate_bd.solicitudes
CREATE TABLE IF NOT EXISTS `solicitudes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `uuid_us` varchar(36) DEFAULT NULL,
  `tecnico_id` int(11) DEFAULT NULL,
  `descripcion` text NOT NULL,
  `tipo` varchar(100) NOT NULL COMMENT 'vehículo varado, cambio de llanta, paso de corriente, envío de grúa, apertura de puertas, suministro de combustible o revisión mecánica básica en sitio.',
  `criticidad` int(11) NOT NULL,
  `estado` varchar(50) NOT NULL COMMENT 'pendiente, en ejecución o atendida',
  `fecha_hora` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_solicitudes_clientes` (`cliente_id`),
  KEY `FK_solicitudes_us` (`uuid_us`),
  KEY `FK_solicitudes_tecnicos` (`tecnico_id`),
  CONSTRAINT `FK_solicitudes_clientes` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_solicitudes_tecnicos` FOREIGN KEY (`tecnico_id`) REFERENCES `tecnicos` (`id`),
  CONSTRAINT `FK_solicitudes_us` FOREIGN KEY (`uuid_us`) REFERENCES `unidadesservicio` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.solicitudes: ~3 rows (aproximadamente)
INSERT INTO `solicitudes` (`id`, `cliente_id`, `uuid_us`, `tecnico_id`, `descripcion`, `tipo`, `criticidad`, `estado`, `fecha_hora`) VALUES
	(1, 1, NULL, NULL, 'Bus intermunicipal averiado con pasajeros', 'vehículo varado', 5, 'pendiente', '2026-06-01 02:14:30'),
	(2, 2, '22222222-3333-4444-5555-666666666666', 2, 'Batería descargada en sótano', 'paso de corriente', 1, 'en ejecución', '2026-06-01 02:14:30'),
	(3, 3, '11111111-2222-3333-4444-555555555555', 1, 'Se quedaron las llaves adentro del carro', 'apertura de puertas', 4, 'atendida', '2026-06-01 02:14:30');

-- Volcando estructura para tabla autorescate_bd.tecnicos
CREATE TABLE IF NOT EXISTS `tecnicos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `especialidad` varchar(100) NOT NULL,
  `estado` varchar(50) NOT NULL,
  `zona` varchar(50) NOT NULL,
  `disponibilidad` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1=Disponible, 0=Asignada',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.tecnicos: ~3 rows (aproximadamente)
INSERT INTO `tecnicos` (`id`, `especialidad`, `estado`, `zona`, `disponibilidad`) VALUES
	(1, 'Mecánica General', 'activo', 'Norte', 1),
	(2, 'Electricidad Automotriz', 'activo', 'Centro', 0),
	(3, 'Operador de Grúa', 'en descanso', 'Sur', 1);

-- Volcando estructura para tabla autorescate_bd.unidadesservicio
CREATE TABLE IF NOT EXISTS `unidadesservicio` (
  `uuid` varchar(36) NOT NULL,
  `tipo` varchar(50) NOT NULL COMMENT 'grúa, moto de apoyo, camioneta de asistencia o vehículo liviano',
  `estado` varchar(50) NOT NULL COMMENT 'disponible, ocupada o en mantenimiento',
  `zona` varchar(50) NOT NULL,
  `disponibilidad` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1=Disponible, 0=Asignada',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla autorescate_bd.unidadesservicio: ~3 rows (aproximadamente)
INSERT INTO `unidadesservicio` (`uuid`, `tipo`, `estado`, `zona`, `disponibilidad`) VALUES
	('11111111-2222-3333-4444-555555555555', 'grúa', 'disponible', 'Norte', 1),
	('22222222-3333-4444-5555-666666666666', 'moto de apoyo', 'ocupada', 'Centro', 0),
	('33333333-4444-5555-6666-777777777777', 'camioneta de asistencia', 'en mantenimiento', 'Sur', 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
