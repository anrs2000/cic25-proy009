package es.cic.curso25.proy009.controllers;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MvcResult;

import es.cic.curso25.proy009.models.Arbol;
import es.cic.curso25.proy009.models.Rama;
import es.cic.curso25.proy009.repositories.ArbolRepository;
import es.cic.curso25.proy009.repositories.RamaRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class ArbolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArbolRepository arbolRepository;

    @Autowired
    private RamaRepository ramaRepository;

    @Test
    @Transactional
    void testCreateArbol() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        Arbol arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        String arbolJson = objectMapper.writeValueAsString(arbol);

        mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String respuesta = result.getResponse().getContentAsString();
                    Arbol arbolCreado = objectMapper.readValue(respuesta, Arbol.class);

                    assertTrue(arbolCreado.getId() > 0);

                    Optional<Arbol> arbolRealmenteCreado = arbolRepository.findById(arbolCreado.getId());

                    assertTrue(arbolRealmenteCreado.isPresent());

                    assertTrue(!arbolRealmenteCreado.get().getRamas().isEmpty()
                            && arbolRealmenteCreado.get().getRamas().size() >= 3);
                });
    }

    @Test
    void testCreateRama() throws Exception {
        Rama rama = new Rama();
        rama.setLongitud(1.2);
        rama.setNumHojas(12);

        String ramaJson = objectMapper.writeValueAsString(rama);

        mockMvc.perform(post("/arbol/rama")
                .contentType("application/json")
                .content(ramaJson))
                .andExpect(status().isOk())
                .andExpect(resultado -> {
                    String ramaResultadoJson = resultado.getResponse().getContentAsString();
                    Rama ramaResultado = objectMapper.readValue(ramaResultadoJson, Rama.class);

                    Optional<Rama> ramaRealmenteCreada = ramaRepository.findById(ramaResultado.getId());

                    assertTrue(ramaRealmenteCreada.isPresent());
                    assertTrue(ramaRealmenteCreada.get().getId() > 0);
                    assertEquals(12, ramaRealmenteCreada.get().getNumHojas());

                });
    }

    @Test
    @Transactional
    void testDeleteArbol() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        Arbol arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        String arbolJson = objectMapper.writeValueAsString(arbol);

        MvcResult mvcResult = mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String respuesta = result.getResponse().getContentAsString();
                    Arbol arbolCreado = objectMapper.readValue(respuesta, Arbol.class);

                    assertTrue(arbolCreado.getId() > 0);

                    Optional<Arbol> arbolRealmenteCreado = arbolRepository.findById(arbolCreado.getId());

                    assertTrue(arbolRealmenteCreado.isPresent());

                    assertTrue(!arbolRealmenteCreado.get().getRamas().isEmpty()
                            && arbolRealmenteCreado.get().getRamas().size() >= 3);
                }).andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Arbol.class).getId();

        mockMvc.perform(delete("/arbol/" + id)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    Optional<Arbol> arbolBorrado = arbolRepository.findById(id);
                    assertTrue(arbolBorrado.isEmpty());
                });
    }

    @Test
    @Transactional
    void testDeleteRama() throws Exception {
        Rama rama = new Rama();
        rama.setLongitud(1.2);
        rama.setNumHojas(12);

        String ramaJson = objectMapper.writeValueAsString(rama);

        MvcResult mvcResult = mockMvc.perform(post("/arbol/rama")
                .contentType("application/json")
                .content(ramaJson))
                .andExpect(status().isOk())
                .andExpect(resultado -> {
                    String ramaResultadoJson = resultado.getResponse().getContentAsString();
                    Rama ramaResultado = objectMapper.readValue(ramaResultadoJson, Rama.class);

                    Optional<Rama> ramaRealmenteCreada = ramaRepository.findById(ramaResultado.getId());

                    assertTrue(ramaRealmenteCreada.isPresent());
                    assertTrue(ramaRealmenteCreada.get().getId() > 0);
                    assertEquals(12, ramaRealmenteCreada.get().getNumHojas());

                }).andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Rama.class).getId();

        mockMvc.perform(delete("/arbol/rama/" + id)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Optional<Rama> ramaBorrada = ramaRepository.findById(id);
                    assertTrue(ramaBorrada.isEmpty());
                });
    }

    @Test
    @Transactional
    void testGetAllArboles() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        Arbol arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        Arbol arbol2 = new Arbol();
        arbol2.setRamas(ramas);
        arbol2.setVersion(2L);

        String arbolJson = objectMapper.writeValueAsString(arbol);
        String arbolJson2 = objectMapper.writeValueAsString(arbol);

        mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson));

        mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson2));

        mockMvc.perform(get("/arbol")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultadoJson = result.getResponse().getContentAsString();
                    List<Arbol> arbolesObtenidos = objectMapper.readValue(resultadoJson,
                            new TypeReference<List<Arbol>>() {
                            });

                    assertTrue(arbolesObtenidos.size() >= 2);
                });

    }

    @Test
    @Transient
    void testGetAllRamas() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        String ramaJson = objectMapper.writeValueAsString(rama1);
        String ramaJson2 = objectMapper.writeValueAsString(rama2);
        String ramaJson3 = objectMapper.writeValueAsString(rama3);

        mockMvc.perform((post("/arbol/rama"))
                .contentType("application/json")
                .content(ramaJson));

        mockMvc.perform((post("/arbol/rama"))
                .contentType("application/json")
                .content(ramaJson2));

        mockMvc.perform((post("/arbol/rama"))
                .contentType("application/json")
                .content(ramaJson3));

        mockMvc.perform(get("/arbol/ramas")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResultado = result.getResponse().getContentAsString();
                    List<Rama> ramasObtenidas = objectMapper.readValue(jsonResultado, new TypeReference<List<Rama>>() {
                    });

                    assertTrue(ramasObtenidas.size() >= 3);
                });
    }

    @Test
    @Transactional
    void testGettAllRamasPorIdArbol() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        Arbol arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        String arbolJson = objectMapper.writeValueAsString(arbol);

        MvcResult mvcResult = mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson)).andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Arbol.class).getId();

        mockMvc.perform(get(String.format("/arbol/%s/ramas", id))
                .content("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String respuestaJSON = result.getResponse().getContentAsString();
                    List<Rama> ramasObtenidas = objectMapper.readValue(respuestaJSON, new TypeReference<List<Rama>>() {

                    });

                    assertTrue(ramasObtenidas.size() >= 3);
                });
    }

    @Test
    @Transient
    void testGetArbol() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        Rama rama2 = new Rama();
        rama2.setLongitud(5.4);
        rama2.setNumHojas(76);

        Rama rama3 = new Rama();
        rama3.setLongitud(3.22);
        rama3.setNumHojas(5);

        List<Rama> ramas = new ArrayList<>();
        ramas.add(rama1);
        ramas.add(rama2);
        ramas.add(rama3);

        Arbol arbol = new Arbol();
        arbol.setRamas(ramas);
        arbol.setVersion(1L);

        String arbolJson = objectMapper.writeValueAsString(arbol);

        MvcResult mvcResult = mockMvc.perform(post("/arbol")
                .contentType("application/json")
                .content(arbolJson)).andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Arbol.class).getId();

        mockMvc.perform(get("/arbol/" + id)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultadoJson = result.getResponse().getContentAsString();
                    Arbol arbolObtenido = objectMapper.readValue(resultadoJson, Arbol.class);

                    assertEquals(ramas, arbolObtenido.getRamas());
                    assertEquals(1L, arbolObtenido.getVersion());
                });
    }

    @Test
    @Transactional
    void testGetRama() throws Exception {
        Rama rama1 = new Rama();
        rama1.setLongitud(1.2);
        rama1.setNumHojas(12);

        String ramaJson = objectMapper.writeValueAsString(rama1);

        MvcResult mvcResult = mockMvc.perform((post("/arbol/rama"))
                .contentType("application/json")
                .content(ramaJson)).andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Rama.class).getId();

        mockMvc.perform(get("/arbol/rama/" + id)
                .contentType("application/json"))
                .andExpect(result -> {
                    String ramaObtenidaJson = result.getResponse().getContentAsString();
                    Rama ramaObtenida = objectMapper.readValue(ramaObtenidaJson, Rama.class);

                    assertEquals(1.2, ramaObtenida.getLongitud());
                    assertEquals(12, ramaObtenida.getNumHojas());
                });
    }

    @Test
    void testUpdateArbol() {

    }

    @Test
    void testUpdateRama() {

    }
}
