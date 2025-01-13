package com.ensono.stacks.cosmosdb.repository;

#if USE_COSMOSDB
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.data.repository.CrudRepository;
#endif
import com.ensono.stacks.core.repository.StacksPersistence;
import org.springframework.data.repository.NoRepositoryBean;

#if USE_COSMOSDB
@NoRepositoryBean
public interface StacksCosmosDBRepository<T>
        extends StacksPersistence<T>, CrudRepository<T, String>, CosmosRepository<T, String> {}
#else
// Placeholder repository interface to ensure Maven compilation when Cosmos is not selected.
// The entire Cosmos folder, including this file, is filtered out with project-builder-config.
@NoRepositoryBean
public interface StacksCosmosDBRepository<T> extends StacksPersistence<T> {
}
#endif