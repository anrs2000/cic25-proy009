package es.cic.curso25.proy009.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy009.exceptions.ArbolNotFound;
import es.cic.curso25.proy009.exceptions.RamaNotFound;
import es.cic.curso25.proy009.models.Arbol;
import es.cic.curso25.proy009.models.Rama;
import es.cic.curso25.proy009.repositories.ArbolRepository;
import es.cic.curso25.proy009.repositories.RamaRepository;

@Service
public class ArbolService {

    @Autowired
    RamaRepository ramaRepository;

    @Autowired
    ArbolRepository arbolRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArbolService.class);

    public List<Rama> getAllRamas() {
        LOGGER.info("Buscando todas las ramas de la BD");
        return ramaRepository.findAll();
    }

    public Rama getRama(Long id) {
        LOGGER.info(String.format("Buscando la rama con id %d", id));
        Optional<Rama> ramaBuscada = ramaRepository.findById(id);
        if (ramaBuscada.isEmpty()) {
            throw new RamaNotFound("No existe ninguna rama con ese id");
        }
        return ramaBuscada.get();
    }

    public Rama createRama(Rama rama) {
        LOGGER.info("Generando una nueva rama");
        return ramaRepository.save(rama);
    }

    public Rama updateRama(Long id, Rama rama) {
        LOGGER.info(String.format("Actualizando la rama con id %d", id));
        Optional<Rama> ramaBuscada = ramaRepository.findById(id);

        if (ramaBuscada.isEmpty()) {
            throw new RamaNotFound("No se puede actualizar la rama indicada porque no existe ninguna rama con ese id");
        }

        rama.setId(id);

        return ramaRepository.save(rama);
    }

    public void deleteRama(Long id) {
        LOGGER.info(String.format("Eliminando la rama con id", id));
        ramaRepository.deleteById(id);
    }

    public List<Arbol> getAllArboles() {
        LOGGER.info("Obteniendo todos los árboles");
        return arbolRepository.findAll();
    }

    public Arbol getArbol(Long id) {
        LOGGER.info(String.format("Buscando el árbol con id %d", id));
        Optional<Arbol> arbolBuscado = arbolRepository.findById(id);
        if (arbolBuscado.isEmpty()) {
            throw new ArbolNotFound("No existe ningún árbol con ese ID");
        }
        return arbolBuscado.get();
    }

    public Arbol createArbol(Arbol arbol) {
        LOGGER.info("Creando un nuevo árbol");
        return arbolRepository.save(arbol);
    }

    public Arbol updateArbol(Long id, Arbol arbol) {
        LOGGER.info(String.format("Actualizando elarbol con id %d", id));
        Optional<Arbol> arbolBuscado = arbolRepository.findById(id);

        if (arbolBuscado.isEmpty()) {
            throw new ArbolNotFound(
                    "No se puede actualizar el árbol indicado porque no existe ningun árbol con ese id");
        }

        arbol.setId(id);

        return arbolRepository.save(arbol);
    }

    public void deleteArbol(Long id) {
        LOGGER.info(String.format("Eliminando el arbol con id %d", id));
        arbolRepository.deleteById(id);
    }
}
