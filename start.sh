#!/bin/bash

# Colores para los mensajes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Mensaje de bienvenida
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    Bienvenido a Tenpo Challenge API    ${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "${BLUE}Iniciando servicios...${NC}"
echo ""

# Verificar que Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker no está corriendo. Por favor, inicia Docker Desktop y vuelve a intentarlo.${NC}"
    exit 1
fi

# Función para mostrar el mensaje de procesando
show_processing() {
    echo -e "${BLUE}Procesando...${NC}"
    sleep 5
}

# Detener contenedores existentes si los hay
echo -e "${YELLOW}Deteniendo contenedores existentes...${NC}"
docker-compose down

# Construir y levantar los contenedores
echo -e "${YELLOW}Construyendo y levantando contenedores...${NC}"
docker-compose up --build -d

# Esperar a que los servicios estén listos
echo -e "${YELLOW}Esperando a que los servicios estén listos...${NC}"
show_processing
show_processing
show_processing
show_processing
show_processing

# Verificar que los contenedores estén corriendo
echo -e "${YELLOW}Verificando estado de los contenedores...${NC}"
docker-compose ps

# Verificar que todos los servicios estén corriendo
if docker-compose ps | grep -q "Up"; then
    echo -e "${GREEN}¡Servicios iniciados exitosamente!${NC}"
    
    # Mostrar logs de los contenedores
    echo -e "${YELLOW}Mostrando logs de los contenedores...${NC}"
    docker-compose logs --tail=20
    
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
    echo -e "${YELLOW}Presione Enter para salir...${NC}"
    read
else
    echo -e "${RED}Error: Los servicios no se iniciaron correctamente${NC}"
    echo -e "${YELLOW}Mostrando logs de error...${NC}"
    docker-compose logs
    echo -e "${YELLOW}Presione Enter para salir...${NC}"
    read
    exit 1
fi 