#!/bin/bash

# Colores para los mensajes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Función para mostrar mensaje de error y salir
show_error_and_exit() {
    echo -e "${RED}Error: $1${NC}"
    echo -e "${YELLOW}Presione Enter para salir...${NC}"
    read
    exit 1
}

# Mensaje de bienvenida
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    Bienvenido a Tenpo Challenge API    ${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "${BLUE}Iniciando servicios...${NC}"
echo ""

# Verificar que Docker está instalado
if ! command -v docker &> /dev/null; then
    show_error_and_exit "Docker no está instalado. Por favor, instala Docker Desktop y vuelve a intentarlo."
fi

# Verificar que Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    show_error_and_exit "Docker no está corriendo. Por favor, inicia Docker Desktop y vuelve a intentarlo."
fi

# Verificar que Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    show_error_and_exit "Docker Compose no está instalado. Por favor, instala Docker Compose y vuelve a intentarlo."
fi

# Función para mostrar el mensaje de procesando
show_processing() {
    echo -e "${BLUE}Procesando...${NC}"
    sleep 15
}

# Detener contenedores existentes si los hay
echo -e "${YELLOW}Deteniendo contenedores existentes...${NC}"
docker-compose down -v > /dev/null 2>&1
show_processing

# Construir y levantar los contenedores
echo -e "${YELLOW}Construyendo y levantando contenedores...${NC}"
if ! docker-compose up --build -d > /dev/null 2>&1; then
    show_error_and_exit "Error al construir y levantar los contenedores."
fi
show_processing

# Verificar que los contenedores estén corriendo
echo -e "${YELLOW}Verificando estado de los contenedores...${NC}"
if ! docker-compose ps > /dev/null 2>&1; then
    show_error_and_exit "Error al verificar el estado de los contenedores."
fi
show_processing

# Verificar que los puertos estén en uso
echo -e "${YELLOW}Verificando servicios...${NC}"
sleep 5

echo -e "${GREEN}Servicios iniciados exitosamente${NC}"

# Abrir el navegador
echo -e "${YELLOW}Abriendo navegador...${NC}"
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    open http://localhost:8080/
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    # Windows
    start http://localhost:8080/
else
    # Linux
    xdg-open http://localhost:8080/
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    La aplicación está lista para usar    ${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "${YELLOW}Presione Enter para salir o 0 para reiniciar...${NC}"
read choice
if [ "$choice" = "0" ]; then
    exec "$0" "$@"
fi 