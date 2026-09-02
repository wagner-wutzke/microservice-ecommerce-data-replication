package net.wowdev.ecommerce.datareplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "net.wowdev.ecommerce.datareplication.repository")
public class ReplicationPersistenceConfig {}
