<?php
	
	require 'Database.php';

	class Registro{
		function _construct(){
		}

		public static function InsertarNuevoDato($id,$nombre,$apellido,$fecha,$correo,$telefono,$genero){
			$consultar = "INSERT INTO DatosPersonales(id,nombre,apellidos,fecha_de_nacimiento,correo,telefono,genero) VALUES(?,?,?,?,?,?,?)";
			try{
				$resultado = Database::getInstance()->getDb()->prepare($consultar);
				return $resultado->execute(array($id,$nombre,$apellido,$fecha,$correo,$telefono,$genero));
			}catch(PDOException $e){
				return false;
			}
		}

		public static function InsertarEnTablaLogin($id,$password){
			$consultar = "INSERT INTO Login(id,Password) VALUES(?,?)";
			try{
				$resultado = Database::getInstance()->getDb()->prepare($consultar);
				return $resultado->execute(array($id,$password));
			}catch(PDOException $e){
				return false;
			}
		}

	}

?>