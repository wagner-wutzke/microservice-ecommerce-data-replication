# microservice-ecommerce-data-replication

This maven project contains the services, repositories and configuration classes used to replicate
domain data in the e-commerce microservice projects.

This data replication is necessary when:
- to keep the pattern "one database per microservice"
- having the data from other domains without crossing domain boundaries with requests

The project has a dependency to the net.wowdev.ecommerce:domain, which provides access to entities
and DTOs.

Following services are available for replicating domain objects in single microservices
- OrderReplicationService (for Orders and OrderLines)
- CustomerReplicationService (for Customers)
- PaymentMethodReplicationService (for PaymentMethods)

# How to use it

1. Add the maven dependency to the pom.xml in the target project.
```
<dependency>
  <groupId>net.wowdev.ecommerce</groupId>
  <artifactId>data-replication</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

2. Create a Spring Configuration class for the Replication config.
```java
import net.wowdev.ecommerce.datareplication.config.ReplicationPersistenceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReplicationPersistenceConfig.class)
public class DataReplicationConfig {}
```

3. Enable current projects JPA repositories within a PersistenceConfig class.
   In this example we are working with the Payments Service.
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "net.wowdev.ecommerce.payments.repository")
public class PersistenceConfig {}
```

4. Tell Spring to scan for beans and components in the target and in the data-replication projects.
   In this example we are working with the Payments Service.
```java
@SpringBootApplication(
    scanBasePackages = {"net.wowdev.ecommerce.payments", "net.wowdev.ecommerce.datareplication"})
@EntityScan(basePackages = "net.wowdev.ecommerce.domain.entity")
public class PaymentsServiceApplication {
  public static void main(final String[] args) {
    SpringApplication.run(PaymentsServiceApplication.class, args);
  }
}
```
