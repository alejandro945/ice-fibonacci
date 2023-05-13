#!/bin/bash

export PASSWORD="swarch"
LOG_FILE="client.log"
path="GabrielSuarez-AlejandroVarela-CallBack"

echo "Ejecutando Jar"

IFS=',' read -ra array_client <<< "$1"
IFS=',' read -ra array_numbers <<< "$2"

paramsJar=""
for number in "${array_numbers[@]}"; do
    paramsJar="$paramsJar $number"
done

output=""
for client in "${array_client[@]}"; do
    client_id="swarch@xhgrid$client"
    sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $client_id "cd $path; nohup java -jar client.jar $paramsJar > output.txt 2>&1" &
done

wait

# Descarga los archivos de salida de cada cliente
for client in "${array_client[@]}"; do
    sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no swarch@xhgrid$client:$path/output.txt ./output_$client.txt
done

# Concatena todos los resultados en un solo archivo
cat output_*.txt > $LOG_FILE

# Borra los archivos de salida individuales
rm output_*.txt

echo "Resultado Final guardado en $LOG_FILE"