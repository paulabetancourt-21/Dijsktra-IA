# Visualizador del Algoritmo de Dijkstra (JavaFX)

Fase 0: configuración del proyecto.

## Requisitos
- JDK 21 (`java -version`)
- Maven (`mvn -version`, debe usar el mismo JDK 21)
- VS Code con Extension Pack for Java

## Comandos
```
mvn clean compile
mvn javafx:run
mvn test
```

## Estructura
Paquete base: `co.edu.uptc`

| Paquete | Contenido |
| --- | --- |
| co.edu.uptc | Main |
| presentation | Todo lo de pantalla: ventanas, eventos, animación y estilos (único que usa JavaFX) |
| business | GraphService y DijkstraAlgorithm |
| model | Clases de datos, enums, excepciones y constantes |
| data | Guardado y carga de archivos (Fase 5) |

Reglas: presentation solo habla con business; business usa model y data; data solo lee y escribe archivos; model no depende de nada.
