package com.EcoMarket.GestionEnvio.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.EcoMarket.GestionEnvio.model.Estado;
import com.EcoMarket.GestionEnvio.model.G_Envio;
import com.EcoMarket.GestionEnvio.repository.G_EnvioRepository;

public class G_EnvioServiceTest {
    @Mock
    private G_EnvioRepository G_EnvioRepository;

    @InjectMocks
    private G_EnvioService G_EnvioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearEnvio() {
        
        G_Envio envioTest = new G_Envio();
        envioTest.setIdEnvio(1);
        envioTest.setEstadoEnvio(Estado.PENDIENTE);
        envioTest.setFechaEnvio(LocalDateTime.now());

        when(G_EnvioRepository.save(envioTest)).thenReturn(envioTest);

        G_Envio resultado = G_EnvioService.crearEnvio(envioTest); 
        assert resultado.getIdEnvio() == 1;
        assert resultado.getEstadoEnvio() == Estado.PENDIENTE;
        assert resultado.getFechaEnvio() != null;

        verify(G_EnvioRepository).save(envioTest);

    }


    @Test
    void testPedirEnvios() {
        
        G_Envio pedirEnvioTest = new G_Envio();
        pedirEnvioTest.setIdEnvio(1);
        pedirEnvioTest.setFechaEnvio(LocalDateTime.now());
        
        when(G_EnvioRepository.findAll()).thenReturn(List.of(pedirEnvioTest));
        List<G_Envio> envios = G_EnvioService.pedirEnvios();

        assert envios != null && !envios.isEmpty();
        assert envios.get(0).getIdEnvio() == 1;
        assert envios.get(0).getFechaEnvio() != null;

        verify(G_EnvioRepository).findAll();

    }

    

    @Test
    void testObtenerEnvioId() {
        // Aquí puedes implementar el test para obtener un envío por ID
        G_Envio obtenerEnvioIdTest = new G_Envio();
        obtenerEnvioIdTest.setIdEnvio(1);
        obtenerEnvioIdTest.setFechaEnvio(LocalDateTime.now());

        when(G_EnvioRepository.findById(1)).thenReturn(java.util.Optional.of(obtenerEnvioIdTest));
        G_Envio resultado = G_EnvioService.obtenerEnvioId(1).orElse(null);

        assert resultado != null;
        assert resultado.getIdEnvio() == 1;
        assert resultado.getFechaEnvio() != null;

        verify(G_EnvioRepository).findById(1);

    }


    @Test
    void testActualizarEstadoEnvio() {
        //Actualizar el estado envio 1
        G_Envio actualizarEstadoEnvioTest = new G_Envio();
        actualizarEstadoEnvioTest.setIdEnvio(1);
        actualizarEstadoEnvioTest.setEstadoEnvio(Estado.PENDIENTE);
        

        when(G_EnvioRepository.findById(1)).thenReturn(java.util.Optional.of(actualizarEstadoEnvioTest));
        when(G_EnvioRepository.save(actualizarEstadoEnvioTest)).thenReturn(actualizarEstadoEnvioTest);

        G_Envio resultado = G_EnvioService.actualizarEstadoEnvio(1, Estado.EN_CAMINO);
        assert resultado != null;
        assert resultado.getEstadoEnvio() == Estado.EN_CAMINO;
        verify(G_EnvioRepository).save(actualizarEstadoEnvioTest);
    }

    @Test
    void testActualizarEstadoEnvio2() {

        //Actualizar el estado envio 2

        G_Envio actualizarEstadoEnvioTest2 = new G_Envio();
        actualizarEstadoEnvioTest2.setIdEnvio(1);
        actualizarEstadoEnvioTest2.setEstadoEnvio(Estado.EN_CAMINO);
        
        when(G_EnvioRepository.findById(1)).thenReturn(java.util.Optional.of(actualizarEstadoEnvioTest2));
        when(G_EnvioRepository.save(actualizarEstadoEnvioTest2)).thenReturn(actualizarEstadoEnvioTest2);

        G_Envio resultado2 = G_EnvioService.actualizarEstadoEnvio(1, Estado.ENTREGADO);
        assert resultado2.getEstadoEnvio() == Estado.ENTREGADO;

        verify(G_EnvioRepository).save(actualizarEstadoEnvioTest2);
    }

    @Test
    void testActualizarEstadoEnvio3() {

        //Actualizar el estado envio 3

        G_Envio actualizarEstadoEnvioTest3 = new G_Envio();
        actualizarEstadoEnvioTest3.setIdEnvio(1);
        actualizarEstadoEnvioTest3.setEstadoEnvio(Estado.EN_CAMINO);
        
        when(G_EnvioRepository.findById(1)).thenReturn(java.util.Optional.of(actualizarEstadoEnvioTest3));
        when(G_EnvioRepository.save(actualizarEstadoEnvioTest3)).thenReturn(actualizarEstadoEnvioTest3);   
        G_Envio resultado3 = G_EnvioService.actualizarEstadoEnvio(1, Estado.CANCELADO);
        
        assert resultado3.getEstadoEnvio() == Estado.CANCELADO;
        
        verify(G_EnvioRepository).save(actualizarEstadoEnvioTest3);
    }

}
