package com.amido.stacks.dynamodb.repository;

#if USE_DYNAMODB
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
#endif
import com.amido.stacks.core.repository.StacksPersistence;
import org.springframework.data.repository.NoRepositoryBean;


#if USE_DYNAMODB
@NoRepositoryBean
public interface StacksDynamoDBRepository<T>
        extends StacksPersistence<T>,
        CrudRepository<T, String>,
        PagingAndSortingRepository<T, String> {}
#else
// Placeholder repository interface to ensure Maven compilation when Dynamo is not selected.
// The entire Dynamo folder, including this file, is filtered out with project-builder-config.
@NoRepositoryBean
public interface StacksDynamoDBRepository<T> extends StacksPersistence<T> {
}
#endif