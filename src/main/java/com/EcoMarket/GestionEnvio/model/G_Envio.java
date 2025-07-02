package com.EcoMarket.GestionEnvio.model;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "G_Envio")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class G_Envio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio", nullable = false, unique = true)
    @Min(value = 1, message = "El ID del envío debe ser mayor que 0")
    private int idEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", nullable = false)
    @Min(value = 1, message = "El estado del envío no puede estar vacío")
    private Estado estadoEnvio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "America/Santiago")
    private LocalDateTime fechaEnvio;
    
    @Column(nullable = false)
    @Min(value = 1, message = "La dirección de destino no puede estar vacía")
    private String direccionDestino;

    
    @Column(nullable = false, unique = true) // Código único en la base de datos
    @Min(value = 1, message = "El código de seguimiento no puede estar vacío")
    @Size(min = 1, max = 10, message = "El código de seguimiento debe tener entre 1 y 10 caracteres")
    private String codigoSeguimiento;

    @Min(value = 1, message = "El ID del pedido debe ser mayor que 0")
    @Column(name = "id_pedido", nullable = false, unique = true)
    private int idPedido; 
}
