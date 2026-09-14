\# HTTP Server - Laboratorio de Escalabilidad

\#Alumna Keyla Y Serna Illescas

\#TDSE Corte 2



\## Metáfora del sistema

Este proyecto es un servidor de "recepción secuencial": como un mostrador de atención con un solo empleado, atiende una petición a la vez, de principio a fin, antes de recibir a la siguiente persona en la fila. No hay concurrencia ni balanceo de carga: cada cliente espera su turno.



\## Arquitectura
\[Navegador / Cliente JS]

|

| HTTP (GET)

v

\[ServerSocket :35000] --- while(true) ---> accept() --> procesa 1 petición --> responde --> cierra socket

|

+--> Recursos estáticos (carpeta /public: index.html, app.js, imágenes)

+--> Servicios hardcoded: /health, /server-time, /greeting, /square





El servidor corre en una instancia EC2 de AWS (Amazon Linux 2023), escuchando en el puerto 35000, accesible desde cualquier navegador con la IP pública de la instancia.



\## Decisiones de diseño

\- Servidor implementado sobre sockets TCP crudos (`ServerSocket`/`Socket`), sin frameworks HTTP, para entender el protocolo a bajo nivel.

\- Ciclo de vida secuencial (una conexión a la vez) por requerimiento del laboratorio.

\- Puerto configurable por argumento de línea de comandos (`java -jar httpserver.jar <puerto>`), con 35000 como valor por defecto.

\- Servicios JSON hardcoded (sin base de datos) con validación de parámetros y escape de caracteres especiales en el JSON de salida.

\- Cliente asíncrono en JavaScript puro (fetch API), sin recargar la página, con manejo separado de errores de red y errores HTTP.



\## Estructura del proyecto

httpserver/

├── src/main/java/com/mycompany/httpserver/

│ └── HttpServer.java

├── public/

│ ├── index.html

│ ├── app.js

│ ├── imagen.jpg

│ └── pagina.html

├── pom.xml

└── README.md





\## Requisitos previos

\- Java 21 (JDK)

\- Maven



\## Cómo compilar y correr localmente

```bash

mvn clean package

java -jar target/httpserver.jar 35000

```

Luego abrir `http://localhost:35000/` en el navegador.



\## Servicios disponibles

\- `GET /` → página principal (cliente asíncrono)

\- `GET /health` → `{"status":"ok"}`

\- `GET /server-time` → hora actual del servidor

\- `GET /greeting?name=X` → saludo personalizado

\- `GET /square?value=N` → cuadrado de un número

\- Archivos estáticos bajo `/public` (html, js, imágenes)



\## Pruebas realizadas

\- Página de inicio, saludo válido/inválido, cuadrado válido/inválido, hora del servidor: OK

\- Recurso estático inexistente → 404

\- Método no soportado (POST) → 405

\- Intento de path traversal (`/../pom.xml`) → 400, bloqueado

\- Múltiples peticiones secuenciales seguidas → sin caídas



\## Despliegue en AWS

\- Instancia EC2 (Amazon Linux 2023, t2.micro) vía AWS Academy Learner Lab.

\- Security Group con puertos 22 (SSH) y 35000 (aplicación) abiertos.

\- Java 21 (Amazon Corretto) instalado en la instancia.

\- Aplicación (`httpserver.jar` + carpeta `public`) subida por `scp`.

\- Ejecutado en segundo plano con `nohup` para persistencia ante cierre de sesión SSH.

\- Verificado accediendo remotamente vía IP pública en el puerto 35000.



\## Evidencia

!\[Servidor local funcionando](evidencia/local-home.png)

!\[Servicio de saludo en local](evidencia/local-greeting.png)

!\[Consola con el log de peticiones](evidencia/consola-log.png)

!\[Conexión SSH a la instancia EC2](evidencia/ssh-conexion.png)

!\[Instancia EC2 en ejecución](evidencia/ec2-running.png)

!\[Servidor desplegado y funcionando en AWS](evidencia/remoto-funcionando.png)



\## Limitaciones conocidas

\- El servidor es estrictamente secuencial: una petición lenta bloquea a las demás.

\- No hay concurrencia, balanceo de carga ni contenedores (fuera de alcance del laboratorio).

\- Los servicios son valores hardcoded, sin persistencia en base de datos.





