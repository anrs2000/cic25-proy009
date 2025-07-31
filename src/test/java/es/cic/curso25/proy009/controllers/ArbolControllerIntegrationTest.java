package es.cic.curso25.proy009.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;

import es.cic.curso25.proy009.models.Arbol;
import es.cic.curso25.proy009.models.Rama;
import es.cic.curso25.proy009.repositories.ArbolRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ArbolControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private ArbolRepository arbolRepository;

        Rama rama1;
        Rama rama2;
        Rama rama3;
        List<Rama> ramas;
        Arbol arbol;
        String arbolJson;
        Long idArbolGuardado;
        Arbol arbolGuardado;

        @BeforeEach
        void contextoDeArboles() throws Exception {
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

                arbolJson = objectMapper.writeValueAsString(arbol);

                MvcResult mvcResult = mockMvc.perform(post("/arbol")
                                .contentType("application/json")
                                .content(arbolJson))
                                .andExpect(status().isOk())
                                .andReturn();

                arbolGuardado = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                Arbol.class);
                idArbolGuardado = arbolGuardado.getId();

        }

        @Test
        void testGetAllArboles() throws Exception {
                Arbol arbol2 = new Arbol();
                arbol2.setRamas(ramas);
                arbol2.setVersion(2L);

                String arbolJson2 = objectMapper.writeValueAsString(arbol);

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
        void testGetArbol() throws Exception {
                mockMvc.perform(get("/arbol/" + idArbolGuardado)
                                .contentType("application/json"))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String resultadoJson = result.getResponse().getContentAsString();
                                        Arbol arbolObtenido = objectMapper.readValue(resultadoJson, Arbol.class);

                                        // assertEquals(ramas, arbolObtenido.getRamas());
                                        assertEquals(1L, arbolObtenido.getVersion());
                                });
        }

        @Test
        void testGetArbolNoExistente() throws Exception {
                mockMvc.perform(get("/arbol/723664373")
                                .contentType("application/json"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGetAllRamasArbol() throws Exception {
                mockMvc.perform(get(String.format("/arbol/%s/ramas", idArbolGuardado))
                                .content("application/json"))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String respuestaJSON = result.getResponse().getContentAsString();
                                        List<Rama> ramasObtenidas = objectMapper.readValue(respuestaJSON,
                                                        new TypeReference<List<Rama>>() {

                                                        });

                                        assertTrue(ramasObtenidas.size() >= 3);
                                });
        }

        @Test
        void testGetRamaDeArbol() throws Exception {
                // Buscar rama con atributos conocidos (por ejemplo longitud y numHojas)
                Rama rama1ConId = arbolGuardado.getRamas().stream()
                                .filter(r -> r.getLongitud() == 1.2 && r.getNumHojas() == 12)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("No se encontró la rama esperada"));

                Long idRama = rama1ConId.getId();

                mockMvc.perform(get(String.format("/arbol/%d/%d", idArbolGuardado, idRama))
                                .contentType("application/json"))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String jsonResultado = result.getResponse().getContentAsString();
                                        Rama ramaResultado = objectMapper.readValue(jsonResultado, Rama.class);

                                        assertEquals(rama1.getLongitud(), ramaResultado.getLongitud());
                                        assertEquals(rama1.getNumHojas(), ramaResultado.getNumHojas());
                                });

        }

        @Test
        void testGetRamaNoExistenteDeArbol() throws Exception{
                 mockMvc.perform(get(String.format("/arbol/%d/%d", idArbolGuardado, 12143254L))
                                .contentType("application/json"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCreateArbol() throws Exception {
                mockMvc.perform(post("/arbol")
                                .contentType("application/json")
                                .content(arbolJson))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String respuesta = result.getResponse().getContentAsString();
                                        Arbol arbolCreado = objectMapper.readValue(respuesta, Arbol.class);

                                        assertTrue(arbolCreado.getId() > 0);

                                        Optional<Arbol> arbolRealmenteCreado = arbolRepository
                                                        .findById(arbolCreado.getId());

                                        assertTrue(arbolRealmenteCreado.isPresent());

                                        assertTrue(!arbolRealmenteCreado.get().getRamas().isEmpty()
                                                        && arbolRealmenteCreado.get().getRamas().size() >= 3);
                                });
        }

        @Test
        void testCreateRamaEnArbol() throws Exception {
                Rama nuevaRama = new Rama();
                nuevaRama.setLongitud(6.2);
                nuevaRama.setNumHojas(9);

                String nuevaRamaJson = objectMapper.writeValueAsString(nuevaRama);

                mockMvc.perform(post(String.format("/arbol/%d/nuevaRama", idArbolGuardado))
                                .contentType("application/json")
                                .content(nuevaRamaJson))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String resultado = result.getResponse().getContentAsString();
                                        Arbol arbolConLaNuevaRama = objectMapper.readValue(resultado, Arbol.class);

                                        boolean ramaPresente = false;

                                        for (Rama miRama : arbolConLaNuevaRama.getRamas()) {
                                                // if(miRama.getId().equals(rama3.getId()))
                                                if (miRama.getLongitud() == nuevaRama.getLongitud()
                                                                && miRama.getNumHojas() == nuevaRama.getNumHojas()) {
                                                        ramaPresente = true;
                                                        break;
                                                }
                                        }

                                        assertTrue(ramaPresente);
                                });
        }

        @Test
        void testUpdateArbol() throws Exception {
                Arbol arbolActualizado = new Arbol();

                Rama ramaActualizada = new Rama();
                ramaActualizada.setLongitud(43);
                ramaActualizada.setNumHojas(29);

                List<Rama> nuevasRamas = new ArrayList<>();
                nuevasRamas.add(ramaActualizada);

                arbolActualizado.setRamas(nuevasRamas);
                arbolActualizado.setVersion(1L);
                arbolActualizado.setId(idArbolGuardado);

                String arbolActualizadoJson = objectMapper.writeValueAsString(arbolActualizado);

                mockMvc.perform(put(String.format("/arbol/%d", idArbolGuardado))
                                .contentType("application/json")
                                .content(arbolActualizadoJson))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        String arbolResultadoString = result.getResponse().getContentAsString();
                                        Arbol arbolResultado = objectMapper.readValue(arbolResultadoString,
                                                        Arbol.class);

                                        assertTrue(arbolResultado.getRamas().size() == 1);

                                        Rama unicaRamaEsperada = arbolResultado.getRamas().get(0);
                                        assertTrue(ramaActualizada.getNumHojas() == unicaRamaEsperada.getNumHojas()
                                                        && ramaActualizada.getLongitud() == unicaRamaEsperada
                                                                        .getLongitud());
                                        // assertEquals(1L, arbolResultado.getVersion());
                                });
        }

        @Test
        void testUpdateRamaEnArbol() throws Exception {
                Rama ramaActualizada = new Rama();
                ramaActualizada.setLongitud(43);
                ramaActualizada.setNumHojas(29);

                String ramaActualizadaJson = objectMapper.writeValueAsString(ramaActualizada);

                Long idRamaAActualizar = arbolGuardado.getRamas().get(0).getId();

                mockMvc.perform(put(String.format("/arbol/%d/%d", idArbolGuardado, idRamaAActualizar))
                                .contentType("application/json")
                                .content(ramaActualizadaJson))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        Arbol arbolResultado = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(), Arbol.class);

                                        boolean ramaFueActualizada = false;
                                        for (Rama rama : arbolResultado.getRamas()) {
                                                if (rama.getLongitud() == ramaActualizada.getLongitud() && rama
                                                                .getNumHojas() == ramaActualizada.getNumHojas()
                                                                && rama.getId() == idRamaAActualizar) {
                                                        ramaFueActualizada = true;
                                                }
                                        }
                                        assertTrue(ramaFueActualizada);
                                });
        }

        @Test
        void testDeleteArbol() throws Exception {
                mockMvc.perform(delete("/arbol/" + idArbolGuardado)
                                .contentType("application/json"))
                                .andExpect(status().isOk())
                                .andExpect((result) -> {
                                        Optional<Arbol> arbolBorrado = arbolRepository.findById(idArbolGuardado);
                                        assertTrue(arbolBorrado.isEmpty());
                                });
        }

        @Test
        void testDeleteRamaDeArbol() throws Exception {
                Rama ramaABorrar = arbolGuardado.getRamas().get(0);
                Long idRamaABorrar = ramaABorrar.getId();

                mockMvc.perform(delete(String.format("/arbol/%d/%d", idArbolGuardado, idRamaABorrar))
                                .contentType("application/json"))
                                .andExpect(status().isOk())
                                .andExpect(result -> {
                                        Arbol arbolSinRama = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(), Arbol.class);

                                        boolean ramaEncontrada = false;

                                        for (Rama rama : arbolSinRama.getRamas()) {
                                                if (rama.getLongitud() == ramaABorrar.getLongitud() && rama
                                                                .getNumHojas() == ramaABorrar.getNumHojas()) {
                                                        ramaEncontrada = true;
                                                }
                                        }
                                        assertFalse(ramaEncontrada);
                                });
        }

}
