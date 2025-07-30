# Proyecto Arbol-Rama

Este proyecto es una API REST desarrollada en Java con Spring Boot que gestiona entidades `Arbol` y `Rama`. Un `Arbol` puede tener múltiples `Rama`s asociadas, representando una estructura jerárquica básica.

## Tecnologías

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 / PostgreSQL / MySQL (configurable)
- Jackson (serialización JSON)
- JUnit + MockMvc (pruebas)

## Entidades

### Arbol

```java
class Arbol {
    Long id;
    Long version;
    List<Rama> ramas;
}
```

### Rama

```java
class Rama {
    Long id;
    double longitud;
    int numHojas;
}
```


## Autores
- **Ángela** — *Desarrollador inicial* [arsoto@formacion.cic.es](mailto:arsoto@formacion.cic.es)