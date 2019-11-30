<?php

	require 'Amigos.php';

	if($_SERVER['REQUEST_METHOD']=='GET'){
		try{
			$Respuesta = Amigos::ObtenerTodosLosUsuarios();
			$UsuariosConTokens = Amigos::ObtenerTodosLosTokens();
			echo json_encode(array('resultado' => $Respuesta,'usuariosConTokens' => $UsuariosConTokens));
		}catch(PDOException $e){
			echo json_encode(array('resultado' => 'Ocurrio un error, intentelo mas tarde'));
		}
	}
?>