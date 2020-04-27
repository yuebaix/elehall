package com.geercode.elehall.data.mongo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface MongoBaseService<T, ID extends Serializable> {
    //CRUD
    Optional<T> findById(ID id);
    T save(T entity);
    List<T> saveAll(Iterable<T> entities);
    T insert(T entity);
    List<T> insert(Iterable<T> entities);
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll(Iterable<? extends T> entities);
    void deleteAll();
    long updateDynamic(Object id, T entity);

    //是否存在
    boolean existsById(ID id);
    boolean exists(Example<T> example);
    //列表查询
    long count();
    List<T> findAllById(Iterable<ID> ids);
    List<T> findAll();
    List<T> findAll(Sort sort);
    Page<T> findAll(Pageable pageable);
    Page<T> findAll(Sort sort, Pageable pageable);
    List<T> findAll(List<String> fields);
    List<T> findAll(Sort sort, List<String> fields);
    Page<T> findAll(Pageable pageable, List<String> fields);
    Page<T> findAll(Sort sort, Pageable pageable, List<String> fields);

    //Example查询
    long count(Example<T> example);
    Optional<T> findOne(Example<T> example);
    List<T> findAll(Example<T> example);
    List<T> findAll(Example<T> example, Sort sort);
    Page<T> findAll(Example<T> example, Pageable pageable);
    Page<T> findAll(Example<T> example, Sort sort, Pageable pageable);
    List<T> findAll(Example<T> example, List<String> fields);
    List<T> findAll(Example<T> example, Sort sort, List<String> fields);
    Page<T> findAll(Example<T> example, Pageable pageable, List<String> fields);
    Page<T> findAll(Example<T> example, Sort sort, Pageable pageable, List<String> fields);
}
