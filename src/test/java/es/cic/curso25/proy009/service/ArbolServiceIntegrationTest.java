package es.cic.curso25.proy009.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso25.proy009.exceptions.ArbolNotFound;
import es.cic.curso25.proy009.models.Arbol;
import es.cic.curso25.proy009.models.Rama;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
@Transactional
public class ArbolServiceIntegrationTest {

    @Autowired
    ArbolService arbolService;

    private Rama rama1;
    private Rama rama2;
    private Rama rama3;
    private Arbol arbol;
    private Arbol arbolCreado;

    @BeforeEach
    void contextoDeArboles() {
        rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        arbolCreado = arbolService.createArbol(arbol);
    }

    @Test
    void testCreateArbol() {
        assertNotNull(arbolCreado.getId());
        assertNotNull(arbolCreado.getRamas());
        assertTrue(arbolCreado.getRamas().size() >= 3);
    }

    @Test
    void testCreateRamaEnArbol() {
        int numInicial = arbolCreado.getRamas().size();

        Rama nuevaRama = new Rama();
        nuevaRama.setLongitud(7.0);
        nuevaRama.setNumHojas(99);

        Arbol actualizado = arbolService.createRamaEnArbol(arbolCreado.getId(), nuevaRama);

        assertNotNull(actualizado.getRamas());
        assertTrue(actualizado.getRamas().size() == numInicial + 1);
    }

    @Test
    void testDeleteArbol() {
        Arbol creado = arbolService.createArbol(arbol);
        Long id = creado.getId();

        arbolService.deleteArbol(id);

        assertThrows(ArbolNotFound.class, () -> arbolService.getArbol(id));
    }

    @Test
    void testDeleteRamaDeArbol() {
        Arbol creado = arbolService.createArbol(arbol);
        Long idArbol = creado.getId();
        Long idRama = creado.getRamas().get(0).getId();
        int totalAntes = creado.getRamas().size();

        Arbol actualizado = arbolService.deleteRamaDeArbol(idArbol, idRama);

        assertTrue(actualizado.getRamas().size() == totalAntes - 1);
    }

    @Test
    void testGetAllArboles() {
        int inicial = arbolService.getAllArboles().size();

        arbolService.createArbol(new Arbol());

        List<Arbol> arboles = arbolService.getAllArboles();

        assertTrue(arboles.size() == inicial + 1);
    }

    @Test
    void testGetArbol() {
        Arbol arbolObtenido = arbolService.getArbol(arbolCreado.getId());

        assertEquals(arbolCreado.getId(), arbolObtenido.getId());
    }

    @Test
    void testGetRamaDeArbol() {
        Rama primera = arbolCreado.getRamas().get(0);

        Rama ramaObtenida = arbolService.getRamaDeArbol(arbolCreado.getId(), primera.getId());

        assertEquals(primera.getId(), ramaObtenida.getId());
    }

    @Test
    void testGetRamasDeArbol() {
        int existentes = arbolService.getRamasDeArbol(arbolCreado.getId()).size();

        arbolService.createRamaEnArbol(arbolCreado.getId(), new Rama());

        List<Rama> ramas = arbolService.getRamasDeArbol(arbolCreado.getId());

        assertTrue(ramas.size() >= existentes + 1);
    }

    @Test
    void testUpdateArbol() {
        Long id = arbolCreado.getId();

        Arbol nuevo = new Arbol();
        nuevo.setRamas(new ArrayList<>());
        nuevo.setVersion(1L);

        Arbol arbolActualizado = arbolService.updateArbol(id, nuevo);

        assertEquals(id, arbolActualizado.getId());
        assertNotNull(arbolActualizado.getRamas());
    }

    @Test
    void testUpdateRamaEnArbol() {
        Long idArbol = arbolCreado.getId();
        Long idRama = arbolCreado.getRamas().get(0).getId();

        Rama actualizada = new Rama();
        actualizada.setLongitud(9.9);
        actualizada.setNumHojas(123);

        Arbol resultado = arbolService.updateRamaEnArbol(idArbol, idRama, actualizada);

        boolean ramaEncontrada = false;
        for (Rama rama : resultado.getRamas()) {
            if (rama.getId().equals(idRama) && rama.getNumHojas() == 123) {
                ramaEncontrada = true;
                break;
            }
        }
        assertTrue(ramaEncontrada);
    }
}