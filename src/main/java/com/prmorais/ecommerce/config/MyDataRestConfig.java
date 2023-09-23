package com.prmorais.ecommerce.config;

import com.prmorais.ecommerce.entity.Country;
import com.prmorais.ecommerce.entity.Product;
import com.prmorais.ecommerce.entity.ProductCategory;
import com.prmorais.ecommerce.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

  @Value("${allowed.origins}")
  private String[] theAllowedOrigins;
  private final EntityManager entityManager;

  public MyDataRestConfig(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
    HttpMethod[] theUnsupportedActions = {
        HttpMethod.PUT,
        HttpMethod.POST,
        HttpMethod.DELETE,
        HttpMethod.PATCH
    };
    // Disable HTTP methods for Product: PUT, POST, PATCH and DELETE
    disableHttpMethods(Product.class, config, theUnsupportedActions);

    // Disable HTTP methods for ProductCategory:PUT, POST, PATCH and DELETE
    disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

    // Disable HTTP methods for ProductCategory: PUT, POST, PATCH and DELETE
    disableHttpMethods(Country.class, config, theUnsupportedActions);

    // Disable HTTP methods for ProductCategory: PUT, POST, PATCH and DELETE
    disableHttpMethods(State.class, config, theUnsupportedActions);
    // chama método auxiliar interno
    exposeIds(config);

    // configure CORS mapping
    cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
  }

  private static void disableHttpMethods(
      Class theClass, RepositoryRestConfiguration
      config, HttpMethod[] theUnsupportedActions
  ) {
    config
        .getExposureConfiguration()
        .forDomainType(theClass)
        .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
        .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
  }

  private void exposeIds(RepositoryRestConfiguration config) {
    // expõe Ids da entidade

    //pega uma lista de todas as classes de entidade do gerenciador de entidade
    Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

    // cria um array das entidades
    List<Class> entityClasses = new ArrayList<>();

    // pega as entidades do array entities
    for (EntityType tempEntityType: entities) {
      entityClasses.add(tempEntityType.getJavaType());
    }

    // exp~e os IDs da entidade para uma matriz de tipos de entidade/domínio
    Class[] domainTypes = entityClasses.toArray(new Class[0]);
    config.exposeIdsFor(domainTypes);
  }
}
