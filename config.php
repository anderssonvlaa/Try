<?php
function conectarDB()
{
    $servidor = "localhost";
    $db = "miagenda";
    $user = "root";
    $password = "";
    $conexion = mysqli_connect($servidor, $user, $password, $db);
    $conexion->set_charset("utf8mb4");

    if (!$conexion) {
        $conexion = mysqli_error($conexion);
    }
    return $conexion;
}

function desconectarDB($conexion)
{
    try {
        mysqli_close($conexion);
        $estado = true;
    } catch (Exception $e) {
        $estado = false;
    }
    return $estado;
}


function insert($nombre, $descripcion, $fecha, $status)
{
    $conexion = conectarDB();
    $query = "INSERT INTO tareas (id_tarea, nombre, descripcion, fecha, status) VALUES(NULL, '$nombre', '$descripcion', '$fecha', '$status')";

    if (mysqli_query($conexion, $query)) {
        $estado = true;
    } else {
        $estado = false;
    }

    desconectarDB($conexion);
    return $estado;
}

function update($id, $nombre, $descripcion, $fecha, $status)
{
    $conexion = conectarDB();
    $query = "UPDATE tareas SET nombre = '$nombre', descripcion = '$descripcion', fecha = '$fecha', status = '$status'
     WHERE id_tarea = '$id'";

    if (mysqli_query($conexion, $query)) {
        $estado = true;
    } else {
        $estado = false;
    }

    desconectarDB($conexion);
    return $estado;
}

# funcion para actualizar datos a la base de datos
function deleteRow($id)
{
    $conexion = conectarDB();
    $query = "DELETE FROM tareas WHERE id_tarea = '$id'";

    if (mysqli_query($conexion, $query)) {
        $estado = true;
    } else {
        $estado = false;
    }

    desconectarDB($conexion);
    return $estado;
}

# funcion para listar contactos
function listInfo($filtro)
{   
    $conexion = conectarDB();
    $json = array();

    $query = "SELECT id_tarea, nombre, descripcion, fecha, status FROM tareas" ;
    $result = mysqli_query($conexion, $query);

    if (mysqli_num_rows($result) > 0) {
        while ($tarea = mysqli_fetch_array($result)) {
            $row = array();
            $row['id_tarea'] = $tarea['id_tarea'];
            $row['nombre'] = $tarea['nombre'];
            $row['descripcion'] = $tarea['descripcion'];
            $row['fecha'] = $tarea['fecha'];
            $row['status'] = $tarea['status'];
            $json[] = $row;
        }
    }

    desconectarDB($conexion);
    return array_values($json);
}