package es.cic.curso25.proy009.controllers;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso25.proy009.models.Arbol;
import es.cic.curso25.proy009.models.Rama;
import es.cic.curso25.proy009.service.ArbolService;

@RestController
@RequestMapping("/arbol")
public class ArbolController {
    @Autowired
    ArbolService arbolService;

    private final Logger LOGGER = LoggerFactory.getLogger(ArbolController.class);

    @GetMapping("/{id}")
    public Arbol getArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo el arbol con id %d desde la ruta /arbol/%d", id, id));
        return arbolService.getArbol(id);
    }

    @GetMapping("/rama/{id}")
    public Rama getRama(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo la rama con id %d desde la ruta /arbol/rama/%d", id, id));
        return arbolService.getRama(id);
    }

    @GetMapping()
    public List<Arbol> getAllArboles() {
        LOGGER.info("Obteniendo todos los arboles mediante la ruta /arbol");
        return arbolService.getAllArboles();
    }

    @GetMapping("/ramas")
    public List<Rama> getAllRamas() {
        LOGGER.info("Obteniendo todas las ramas mediante la ruta /arbol/ramas");
        return arbolService.getAllRamas();
    }

    @GetMapping("/{id}/ramas")
    public List<Rama> gettAllRamasPorIdArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo todas las ramas mediante la ruta /arbol/%d/ramas", id));
        Arbol arbol = arbolService.getArbol(id);
        return arbol.getRamas();
    }

    @PostMapping
    public Arbol createArbol(@RequestBody Arbol nuevoArbol) {
        LOGGER.info("Creando un nuevo arbol mediante la ruta /arbol");
        return arbolService.createArbol(nuevoArbol);
    }

    @PostMapping("/rama")
    public Rama createRama(@RequestBody Rama nuevaRama) {
        LOGGER.info("Creando una nueva rama mediante la ruta /arbol/rama");
        return arbolService.createRama(nuevaRama);
    }

    @PutMapping("/{id}")
    public Arbol updateArbol(@PathVariable Long id, @RequestBody Arbol arbolActualizado) {
        LOGGER.info(String.format("Actualizando el arbol con id %d mediante la ruta /arbol/%d", id, id));
        return arbolService.updateArbol(id, arbolActualizado);
    }

    @PutMapping("/rama/{id}")
    public Rama updateRama(@PathVariable Long id, @RequestBody Rama ramaActualizada) {
        LOGGER.info(String.format("Actualizando la rama con id %d mediante la ruta /arbol/rama/%d", id, id));
        return arbolService.updateRama(id, ramaActualizada);
    }

    // @PutMapping("/{id}/ramas")
    // public Arbol actualizarRamasDelArbol(@PathVariable Long id, List<Rama> ramasActualizadas) {
    //     Arbol arbolAActualizar = arbolService.getArbol(id);
    //     List<Rama> ramasActuales = arbolAActualizar.getRamas();

    //     for (int i = 0; i < ramasActuales.size(); i++) {
    //         Long idAActualizar = ramasActuales.get(i).getId();
    //         for (int j = 0; j < ramasActualizadas.size(); j++) {
    //             Long idRamaActualizada = ramasActualizadas.get(j).getId();
    //             if (idAActualizar == idRamaActualizada) {
    //                 Rama ramaActualizada = ramasActualizadas.get(j);
    //                 arbolService.updateRama(idAActualizar, ramaActualizada);
    //             }
    //         }
    //     }


    // }

    

    @DeleteMapping("/{id}")
    public void deleteArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Eliminando el arbol con id %d mediante la ruta /arbol/%d", id, id));
        arbolService.deleteArbol(id);
    }

    @DeleteMapping("/rama/{id}")
    public void deleteRama(@PathVariable Long id) {
        LOGGER.info(String.format("Eliminando la rama con id %d mediante la ruta /arbol/rama/%d", id, id));
        arbolService.deleteRama(id);
    }

}
