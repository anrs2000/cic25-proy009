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

    @GetMapping()
    public List<Arbol> getAllArboles() {
        LOGGER.info("Obteniendo todos los arboles mediante la ruta /arbol");
        return arbolService.getAllArboles();
    }

    @GetMapping("/{id}")
    public Arbol getArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo el arbol con id %d desde la ruta /arbol/%d", id, id));
        return arbolService.getArbol(id);
    }

    @GetMapping("/{id}/ramas")
    public List<Rama> getRamasDeArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Obteniendo las ramas del arbol con id %d desde la ruta /arbol/%d/ramas", id, id));
        return arbolService.getRamasDeArbol(id);
    }

    @GetMapping("/{idArbol}/{idRama}")
    public Rama getRamaDeArbol(@PathVariable Long idArbol, @PathVariable Long idRama) {
        LOGGER.info(String.format("Obteniendo una rama por iddesde la ruta /arbol/%d/%d", idArbol, idRama));
        return arbolService.getRamaDeArbol(idArbol, idRama);
    }

    @PostMapping
    public Arbol createArbol(@RequestBody Arbol nuevoArbol) {
        LOGGER.info("Creando un nuevo arbol mediante la ruta /arbol");
        return arbolService.createArbol(nuevoArbol);
    }

    @PostMapping("/{idArbol}/nuevaRama")
    public Arbol createRamaEnArbol(@PathVariable Long idArbol, @RequestBody Rama nuevaRama) {
        LOGGER.info(String.format("Creando una nueva rama en el arbol con id %d mediante la ruta /arbol/%d/nuevaRama",
                idArbol, idArbol));
        return arbolService.createRamaEnArbol(idArbol, nuevaRama);
    }

    @PutMapping("/{id}")
    public Arbol updateArbol(@PathVariable Long id, @RequestBody Arbol arbolActualizado) {
        LOGGER.info(String.format("Actualizando el arbol con id %d mediante la ruta /arbol/%d", id, id));
        return arbolService.updateArbol(id, arbolActualizado);
    }

    @PutMapping("/{idArbol}/{idRama}")
    public Arbol updateRamaEnArbol(@PathVariable Long idArbol, @PathVariable Long idRama,
            @RequestBody Rama ramaActualizada) {
        LOGGER.info(String.format("Actualizando rama del arbol con id %d mediante la ruta /arbol/%d/%d", idArbol,
                idArbol, idRama));
        return arbolService.updateRamaEnArbol(idArbol, idRama, ramaActualizada);
    }

    @DeleteMapping("/{id}")
    public void deleteArbol(@PathVariable Long id) {
        LOGGER.info(String.format("Eliminando el arbol con id %d mediante la ruta /arbol/%d", id, id));
        arbolService.deleteArbol(id);
    }

    @DeleteMapping("/{idArbol}/{idRama}")
    public Arbol deleteRamaDeArbol(@PathVariable Long idArbol, @PathVariable Long idRama) {
        LOGGER.info(String.format("Eliminando una rama del arbol con id %d mediante la ruta /arbol/%d/%d", idArbol,
                idArbol, idRama));

        return arbolService.deleteRamaDeArbol(idArbol, idRama);
    }
}
