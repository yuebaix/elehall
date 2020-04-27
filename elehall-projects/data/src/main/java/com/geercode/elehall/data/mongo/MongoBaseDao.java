package com.geercode.elehall.data.mongo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface MongoBaseDao<T, ID extends Serializable> extends MongoRepository<T, ID> {
    /** 基本操作*/
    MongoOperations getMongoOperations();
    Class<T> getEntityClass();

    /** 集合查询*/
    Page<T> findAll(Sort sort, Pageable pageable);
    List<T> findAll(List<String> returnFields);
    List<T> findAll(Sort sort, List<String> returnFields);
    Page<T> findAll(Pageable pageable, List<String> returnFields);
    Page<T> findAll(Sort sort, Pageable pageable, List<String> returnFields);

    /** 集合查询限制字段*/
    Page<T> findAll(Example<T> example, Sort sort, Pageable pageable);
    List<T> findAll(Example<T> example, List<String> returnFields);
    List<T> findAll(Example<T> example, Sort sort, List<String> returnFields);
    Page<T> findAll(Example<T> example, Pageable pageable, List<String> returnFields);
    Page<T> findAll(Example<T> example, Sort sort, Pageable pageable, List<String> returnFields);

    /** 更新限制字段*/
    long updateDynamic(Object id, T updateObject);
    long updateDynamic(Object id, Map<String, Object> updateFieldsMap);
    long updateDynamic(Object id, String updateField, Object updateValue);
    long updateFirstDynamic(T queryObject, T updateObject);
    long updateMultiDynamic(T queryObject, T updateObject);
    long upsertDynamic(T queryObject, T updateObject);

    /** 特殊查询*/
    List<T> findAllByField(String queryField, Iterable<ID> fieldValues);
    List<T> findAllByField(String queryField, Iterable<ID> fieldValues, List<String> fields);
}
