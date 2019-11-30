<?php
	require 'Database.php';

	class Amigos{
		function _construct(){
		}

		public static function ObtenerTodosLosUsuarios(){
			$consultar = "SELECT id,nombre FROM DatosPersonales";

			$resultado = Database::getInstance()->getDb()->prepare($consultar);

			$resultado->execute();

			return  $resultado->fetchAll(PDO::FETCH_ASSOC);

			return $tabla;

		}

		public static function ObtenerTodosLosTokens(){
			$consultar = "SELECT id FROM Token";

			$resultado = Database::getInstance()->getDb()->prepare($consultar);

			$resultado->execute();

			return  $resultado->fetchAll(PDO::FETCH_ASSOC);

			return $tabla;

		}

	}

?>