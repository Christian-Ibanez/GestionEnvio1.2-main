package com.EcoMarket.GestionEnvio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EcoMarket.GestionEnvio.model.Estado;
import com.EcoMarket.GestionEnvio.model.G_Envio;
import com.EcoMarket.GestionEnvio.repository.G_EnvioRepository;

@Service
public class G_EnvioService  {
    
    @Autowired
    private G_EnvioRepository envioRepository;

    // Crear un nuevo envío
    public G_Envio crearEnvio(G_Envio envio) {
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setEstadoEnvio(Estado.PENDIENTE);
        return envioRepository.save(envio);
    }

    // Obtener todos los envíos
    public List<G_Envio> pedirEnvios() {
        return envioRepository.findAll();
    }

    // Obtener envío por ID
    public Optional<G_Envio> obtenerEnvioId(int id) {
    if (id <= 0) {
        // Opción 1: ID inválido
        return Optional.empty();
    }
    Optional<G_Envio> envioOptional = envioRepository.findById(id);
    if (envioOptional.isPresent()) {
        // Opción 2: Envío encontrado
        return envioOptional;
    } else {
        // Opción 2: Envío no encontrado
        return Optional.empty();
    }
    }

    // Actualizar estado de envío
    public G_Envio actualizarEstadoEnvio(int id, Estado nuevoEstado) {
    Optional<G_Envio> envioOptional = envioRepository.findById(id);
    
    if (envioOptional.isPresent()) {
        G_Envio envio = envioOptional.get();
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        if (nuevoEstado == envio.getEstadoEnvio()) {
            throw new IllegalArgumentException("El estado del envío ya es " + nuevoEstado);
        }
        envio.setEstadoEnvio(nuevoEstado);
        
        return envioRepository.save(envio);

    }
    return null;  
    }

    
}
