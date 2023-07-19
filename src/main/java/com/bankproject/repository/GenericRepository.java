package com.bankproject.repository;

import com.bankproject.model.GenericModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Абстрактный репозиторий
 * Необходим для работы абстрактного сервиса,
 * т.к. в абстрактном сервисе мы не можем использовать конкретный репозиторий,
 * а должны указывать параметризованный (GenericRepository)
 *
 * @param <T> - Сущность с которой работает репозиторий
 */

@NoRepositoryBean
public interface GenericRepository<T extends GenericModel>
        extends JpaRepository<T, Long> {
    Page<T> findAllByIsDeletedFalse(Pageable pageable);
}
