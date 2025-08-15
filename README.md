# Simulador de Gestión de Procesos en Memoria

## 📌 Descripción
Este proyecto es un simulador sencillo de gestión de procesos en un sistema operativo con memoria RAM limitada.  
Permite crear procesos, administrarlos según la memoria disponible, ejecutar varios a la vez y mantener una cola de espera para los que no puedan iniciar inmediatamente.

## ⚙️ Especificaciones
- **RAM total:** 1 GB (1024 MB)
- **Ejecución concurrente:** Sí (múltiples procesos mientras haya memoria)
- **Cola de espera:** FIFO (primero en entrar, primero en salir)
- **Liberación de memoria:** Automática al finalizar cada proceso

Cada proceso cuenta con:
- **PID:** Identificador único
- **Nombre:** Puede ser definido o generado automáticamente
- **Memoria requerida:** En MB
- **Duración:** Tiempo de ejecución en segundos

## 🚀 Tecnologías
- **Lenguaje:** Java 17+
- **IDE recomendado:** Visual Studio Code o IntelliJ IDEA
- **Librerías externas:** Ninguna

## 📥 Instalación
1. Instalar **Java JDK 17 o superior**  
   - [Descargar JDK](https://adoptium.net/)
2. Clonar el repositorio:
   ```bash
  

