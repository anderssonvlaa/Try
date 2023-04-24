<?php
    require("config.php");

    $datos = array();
    $accion = "";
    if (isset($_POST["accion"])) {
        $accion = $_POST["accion"];
    }

    if ($accion == "insertar") {
        $nombre = $_POST["nombre"];
        $descripcion = $_POST["descripcion"];
        $fecha = $_POST["fecha"];
        $status = $_POST["status"];

        if (insert($nombre, $descripcion, $fecha, $status) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Registro realizado correctamente";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo almacenar el contacto";
        }
    }else if ($accion == "listar") {
        $filtro = (isset($_POST["filtro"])) ? $_POST["filtro"] : "";
        $datos["estado"] = true;
        $datos["resultado"] = listInfo($filtro);
    }else if ($accion == "actualizar") {
        $id = $_POST["id_tarea"];
        $nombre = $_POST["nombre"];
        $descripcion = $_POST["descripcion"];
        $fecha = $_POST["fecha"];
        $status = $_POST["status"];

        if (update($id, $nombre, $descripcion, $fecha, $status) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Registro actualizado correctamente";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo actualizar el contacto";
        }
    }else if ($accion == "eliminar") {
        $id = $_POST["id_tarea"];

        if (deleteRow($id) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Registro eliminado correctamente";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo eliminar el contacto";
        }
    } 

    echo json_encode($datos);
?> 