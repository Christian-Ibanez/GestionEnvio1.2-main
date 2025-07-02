package com.EcoMarket.GestionEnvio.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.EcoMarket.GestionEnvio.model.Estado;
import com.EcoMarket.GestionEnvio.model.G_Envio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.EcoMarket.GestionEnvio.controller.G_EnvioController;
import com.EcoMarket.GestionEnvio.service.G_EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(G_EnvioController.class)
public class G_EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private G_EnvioService g_EnvioService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testCrearEnvio_Correcto() throws Exception {
    
    G_Envio envioMock = new G_Envio();
    envioMock.setIdEnvio(1);
    envioMock.setEstadoEnvio(Estado.PENDIENTE);
    
    when(g_EnvioService.crearEnvio(any(G_Envio.class))).thenReturn(envioMock);

    
    G_Envio envioRequest = new G_Envio();
    envioRequest.setDireccionDestino("Calle Falsa 123");
    envioRequest.setCodigoSeguimiento("ABC123");
    envioRequest.setIdPedido(1);

    // 3. Ejecuta y verifica
    mockMvc.perform(post("/api/envios")  // ← Verifica que coincida con @RequestMapping
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(envioRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEnvio").value(1))
            .andExpect(jsonPath("$.estadoEnvio").value("PENDIENTE"));
    }  

    @Test
    void testCrearEnvio_ConDatosInvalidos() throws Exception {
    G_Envio envioRequest = new G_Envio();
    envioRequest.setDireccionDestino(""); // Dirección vacía
    envioRequest.setCodigoSeguimiento(null); // Código nulo
    envioRequest.setIdPedido(0); // ID inválido

    mockMvc.perform(post("/api/envios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(envioRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearEnvio_ServicioLanzaExcepcion() throws Exception {
    when(g_EnvioService.crearEnvio(any(G_Envio.class)))
        .thenThrow(new IllegalArgumentException("Código de seguimiento inválido"));

    G_Envio envioRequest = new G_Envio();
    envioRequest.setDireccionDestino("Calle Falsa 123");
    envioRequest.setCodigoSeguimiento("INVALIDO");

    mockMvc.perform(post("/api/envios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(envioRequest)))
            .andExpect(status().isBadRequest());
    }



    @Test
    void testObtenerTodosLosEnvios_OK() throws Exception {
    // 1. Configurar datos de prueba
    G_Envio envio1 = new G_Envio();
    envio1.setIdEnvio(1);
    envio1.setEstadoEnvio(Estado.PENDIENTE);
    
    G_Envio envio2 = new G_Envio();
    envio2.setIdEnvio(2);
    envio2.setEstadoEnvio(Estado.EN_CAMINO);

    List<G_Envio> enviosMock = Arrays.asList(envio1, envio2);

    // 2. Configurar el mock del servicio
    when(g_EnvioService.pedirEnvios()).thenReturn(enviosMock);

    // 3. Ejecutar y verificar
    mockMvc.perform(get("/api/envios") 
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].idEnvio").value(1))
            .andExpect(jsonPath("$[0].estadoEnvio").value("PENDIENTE"))
            .andExpect(jsonPath("$[1].idEnvio").value(2))
            .andExpect(jsonPath("$[1].estadoEnvio").value("EN_CAMINO"));
    }

    @Test
    void testObtenerTodosLosEnvios_Vacio() throws Exception {
    // 1. Configurar el mock para devolver lista vacía
    when(g_EnvioService.pedirEnvios()).thenReturn(Collections.emptyList());

    // 2. Ejecutar la petición y verificar
    mockMvc.perform(get("/api/envios"))
           .andExpect(status().isOk())  // Debe ser 200 aunque esté vacío
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testObtenerEnvioPorId_Existente() throws Exception {
    // 1. Configurar datos de prueba
    int idEnvioExistente = 1;
    G_Envio envioMock = new G_Envio();
    envioMock.setIdEnvio(idEnvioExistente);
    envioMock.setEstadoEnvio(Estado.PENDIENTE);
    envioMock.setCodigoSeguimiento("ABC123");

    // 2. Configurar el mock del servicio para devolver el envío cuando se busque por ID
    when(g_EnvioService.obtenerEnvioId(idEnvioExistente))
        .thenReturn(Optional.of(envioMock));

    // 3. Ejecutar la petición y verificar
    mockMvc.perform(get("/api/envios/{id}", idEnvioExistente))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.idEnvio").value(idEnvioExistente))
           .andExpect(jsonPath("$.estadoEnvio").value("PENDIENTE"))
           .andExpect(jsonPath("$.codigoSeguimiento").value("ABC123"));
    }

    @Test
    void testObtenerEnvioPorId_NoExistente() throws Exception {
        when(g_EnvioService.obtenerEnvioId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/envios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEstadoEnvio_OK() throws Exception {
    // 1. Configurar datos de prueba
    int idEnvioExistente = 1;
    Estado nuevoEstado = Estado.EN_CAMINO;
    
    // 2. Configurar el mock del servicio
    G_Envio envioActualizado = new G_Envio();
    envioActualizado.setIdEnvio(idEnvioExistente);
    envioActualizado.setEstadoEnvio(nuevoEstado);
    
    // Asegúrate de mockear correctamente el método
    when(g_EnvioService.actualizarEstadoEnvio(eq(idEnvioExistente), eq(nuevoEstado)))
        .thenReturn(envioActualizado);

    // 3. Ejecutar la petición
    mockMvc.perform(put("/api/envios/{id}/estado", idEnvioExistente)
            .param("estado", nuevoEstado.name()))  // Pasar el estado como parámetro
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEnvio").value(idEnvioExistente))
            .andExpect(jsonPath("$.estadoEnvio").value(nuevoEstado.name()));
    }

    @Test
    void testActualizarEstadoEnvio_NoExistente() throws Exception {
        when(g_EnvioService.actualizarEstadoEnvio(99, Estado.ENTREGADO)).thenReturn(null);

        mockMvc.perform(put("/envios/99/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ENTREGADO\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEstadoEnvio_BadRequest() throws Exception {
    // Caso 1: Estado nulo
    mockMvc.perform(put("/api/envios/1/estado")) // Sin parámetro estado
           .andExpect(status().isBadRequest());
    
    // Caso 2: Estado inválido (si tu aplicación no convierte automáticamente strings a enum)
    mockMvc.perform(put("/api/envios/1/estado")
           .param("estado", "ESTADO_INEXISTENTE"))
           .andExpect(status().isBadRequest());
    }
}