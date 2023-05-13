#!/bin/bash

# Configuración del servidor
SERVER_PORT=9099

# Función para matar el proceso que utiliza el puerto 9099
function kill_port_9099 {
    PID=$(lsof -ti tcp:$SERVER_PORT)
    if [ -n "$PID" ]; then
        echo "Matando proceso $PID que utiliza el puerto $SERVER_PORT"
        kill $PID
    fi
}

echo "Matando proceso (Durara 3 segundos)"
kill_port_9099
sleep 3
echo "Proceso matado"

echo "Ejecutamos el server"
java -jar server.jar >> server.log