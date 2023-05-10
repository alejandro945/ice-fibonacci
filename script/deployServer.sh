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

echo "Ejecutamos el server"
sshpass -p swarch ssh -o StrictHostKeyChecking=no "${SERVER_ID}" "cd ${SERVER_PATH} && nohup java -jar server.jar > server.log 2>&1 &"