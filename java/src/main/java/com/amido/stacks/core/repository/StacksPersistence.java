package com.amido.stacks.core.repository;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings(
        "java:S2326") // "T is not used in the interface". This Interface is used by other modules in
// other stacks repos.
public interface StacksPersistence<T> {}
