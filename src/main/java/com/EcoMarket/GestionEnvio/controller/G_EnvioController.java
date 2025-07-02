package com.EcoMarket.GestionEnvio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EcoMarket.GestionEnvio.model.Estado;
import com.EcoMarket.GestionEnvio.model.G_Envio;
import com.EcoMarket.GestionEnvio.service.G_EnvioService;

import javax.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@RestController
@RequestMapping("/api/envios")
public class G_EnvioController {
    
    @Autowired
    private G_EnvioService envioService;

    // Crear nuevo envío
    @PostMapping
    public ResponseEntity<?> crearEnvio(@Valid @RequestBody G_Envio envio, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        
        G_Envio nuevoEnvio = envioService.crearEnvio(envio);
        EntityModel<G_Envio> resource = EntityModel.of(nuevoEnvio);
        resource.add(linkTo(methodOn(G_EnvioController.class).obtenerEnvioPorId(nuevoEnvio.getIdEnvio())).withSelfRel());
        resource.add(linkTo(methodOn(G_EnvioController.class).pedirEnvios()).withRel("all-envios"));
        return ResponseEntity.ok(resource);
    }


    // Obtener todos los envíos
    @GetMapping
    public ResponseEntity<List<G_Envio>> pedirEnvios() {
    List<G_Envio> envios = envioService.pedirEnvios();
    return ResponseEntity.ok(envios);  // Siempre devuelve 200, incluso si la lista está vacía
}

    // Obtener envío por ID
    @GetMapping("/{id}")
    public ResponseEntity<G_Envio> obtenerEnvioPorId(@PathVariable int id) {
    return envioService.obtenerEnvioId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

    // Actualizar estado de envío
    @PutMapping("/{id}/estado")
    public ResponseEntity<G_Envio> actualizarEstadoEnvio(
        @PathVariable int id,
        @RequestParam Estado estado) {
    
    G_Envio envioActualizado = envioService.actualizarEstadoEnvio(id, estado);
    
    if (envioActualizado != null) {
        return ResponseEntity.ok(envioActualizado);
    }
    return ResponseEntity.notFound().build();
    }

    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcion(IllegalArgumentException ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores); 
    }


}
