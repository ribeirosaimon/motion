package com.saimon.motion.repository;

import com.saimon.motion.domain.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Client, Long> {

    Client findByUsername(String username);
}
