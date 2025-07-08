package com.EcoMarket.GestionEnvio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EcoMarket.GestionEnvio.model.G_Envio;

@Repository
public interface G_EnvioRepository extends JpaRepository<G_Envio, Integer> {

    
}

