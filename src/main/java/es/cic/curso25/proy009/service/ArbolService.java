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

    public List<Rama> getRamasDeArbol(Long id) {
        LOGGER.info(String.format("Buscando todas las ramas del árbol con id %d", id));
        Arbol arbolBuscado = this.getArbol(id);

        return arbolBuscado.getRamas();
    }

    public Rama getRamaDeArbol(Long idArbol, Long idRama) {
        LOGGER.info(String.format("Buscando la rama con id %d del árbol con id %d", idRama, idArbol));

        List<Rama> todasLasRamas = this.getRamasDeArbol(idArbol);

        Optional<Rama> ramaEncontrada = Optional.empty();

        for (Rama rama : todasLasRamas) {
            if (rama.getId().equals(idRama)) {
                ramaEncontrada = Optional.ofNullable(rama);
            }
        }

        if (ramaEncontrada.isEmpty()) {
            throw new RamaNotFound(
                    String.format("El árbol con id %d no contiene ninguna rama con id %d", idArbol, idRama));
        }

        return ramaEncontrada.get();
    }

    public Arbol createArbol(Arbol arbol) {
        LOGGER.info("Creando un nuevo árbol");
        return arbolRepository.save(arbol);
    }

    public Arbol createRamaEnArbol(Long idArbol, Rama nuevaRama) {
        LOGGER.info(String.format("Añadiendo una nueva rama en el arbol con id %d", idArbol));

        Arbol arbolBuscado = this.getArbol(idArbol);
        List<Rama> ramasDelArbol = this.getRamasDeArbol(idArbol);
        ramasDelArbol.add(nuevaRama);

        arbolBuscado.setRamas(ramasDelArbol);

        return arbolRepository.save(arbolBuscado);
    }

    public Arbol updateArbol(Long id, Arbol arbol) {
        LOGGER.info(String.format("Actualizando el arbol con id %d", id));

        Arbol arbolBuscado = this.getArbol(id);

        arbolBuscado = arbol;

        // Por si acaso el nuevo arbol tuviera un id diferente, o no tuviera id:
        arbolBuscado.setId(id);

        return arbolRepository.save(arbol);
    }

    public Arbol updateRamaEnArbol(Long idArbol, Long idRama, Rama ramaActualizada) {
        LOGGER.info(String.format("Actualizando la rama con id %d del arbol con id %d", idRama, idArbol));

        Arbol arbolAActualizar = this.getArbol(idArbol);

        List<Rama> ramasSinActualizar = this.getRamasDeArbol(idArbol);

        // Lanza excepción si no existe la rama
        this.getRamaDeArbol(idArbol, idRama);

        // Por si la rama no tuviera id, o fuera diferente, como solo queremos cambiar
        // las características de la misma:
        ramaActualizada.setId(idRama);

        for (int i = 0; i < ramasSinActualizar.size(); i++) {
            if (ramasSinActualizar.get(i).getId().equals(idRama)) {
                ramasSinActualizar.set(i, ramaActualizada);
                break;
            }
        }

        arbolAActualizar.setRamas(ramasSinActualizar);

        return arbolRepository.save(arbolAActualizar);
    }

    public void deleteArbol(Long id) {
        LOGGER.info(String.format("Eliminando el arbol con id %d", id));
        arbolRepository.deleteById(id);
    }

    public Arbol deleteRamaDeArbol(Long idArbol, Long idRama) {
        LOGGER.info(String.format("Eliminando la rama con id %d del arbol con id %d", idRama, idArbol));

        Arbol arbolAActualizar = this.getArbol(idArbol);

        List<Rama> ramasDelArbol = this.getRamasDeArbol(idArbol);

        //Si la rama no existía en el árbol, lanzará un error
        this.getRamaDeArbol(idArbol, idRama);

        for (int i = 0; i < ramasDelArbol.size(); i++) {
            if(ramasDelArbol.get(i).getId().equals(idRama)){
                ramasDelArbol.remove(i);
            }
        }       

        arbolAActualizar.setRamas(ramasDelArbol);
        return arbolRepository.save(arbolAActualizar);
    }
}
