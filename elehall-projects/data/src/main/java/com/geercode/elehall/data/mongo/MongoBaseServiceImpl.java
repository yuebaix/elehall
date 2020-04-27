package com.geercode.elehall.data.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class MongoBaseServiceImpl<T, ID extends Serializable> implements MongoBaseService<T, ID> {
    @Autowired
    MongoBaseDao<T, ID> mongoBaseDao;

    @Override
    public Optional<T> findById(ID id) {
        return mongoBaseDao.findById(id);
    }

    @Override
    public T save(T entity) {
        return mongoBaseDao.save(entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return mongoBaseDao.saveAll(entities);
    }

    @Override
    public T insert(T entity) {
        return mongoBaseDao.insert(entity);
    }

    @Override
    public List<T> insert(Iterable<T> entities) {
        return mongoBaseDao.insert(entities);
    }

    @Override
    public void deleteById(ID id) {
        mongoBaseDao.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        mongoBaseDao.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        mongoBaseDao.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        mongoBaseDao.deleteAll();
    }

    @Override
    public long updateDynamic(Object id, T entity) {
        return mongoBaseDao.updateDynamic(id, entity);
    }

    @Override
    public boolean existsById(ID id) {
        return mongoBaseDao.existsById(id);
    }

    @Override
    public boolean exists(Example<T> example) {
        return mongoBaseDao.exists(example);
    }

    @Override
    public long count() {
        return mongoBaseDao.count();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return (List<T>) mongoBaseDao.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return mongoBaseDao.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return mongoBaseDao.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return mongoBaseDao.findAll(pageable);
    }

    @Override
    public Page<T> findAll(Sort sort, Pageable pageable) {
        return mongoBaseDao.findAll(sort, pageable);
    }

    @Override
    public List<T> findAll(List<String> fields) {
        return mongoBaseDao.findAll(fields);
    }

    @Override
    public List<T> findAll(Sort sort, List<String> fields) {
        return mongoBaseDao.findAll(sort, fields);
    }

    @Override
    public Page<T> findAll(Pageable pageable, List<String> fields) {
        return mongoBaseDao.findAll(pageable, fields);
    }

    @Override
    public Page<T> findAll(Sort sort, Pageable pageable, List<String> fields) {
        return mongoBaseDao.findAll(sort, pageable, fields);
    }

    @Override
    public long count(Example<T> example) {
        return mongoBaseDao.count(example);
    }

    @Override
    public Optional<T> findOne(Example<T> example) {
        return mongoBaseDao.findOne(example);
    }

    @Override
    public List<T> findAll(Example<T> example) {
        return mongoBaseDao.findAll(example);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort) {
        return mongoBaseDao.findAll(example, sort);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return mongoBaseDao.findAll(example, pageable);
    }

    @Override
    public Page<T> findAll(Example<T> example, Sort sort, Pageable pageable) {
        return mongoBaseDao.findAll(example, sort, pageable);
    }

    @Override
    public List<T> findAll(Example<T> example, List<String> fields) {
        return mongoBaseDao.findAll(example, fields);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort, List<String> fields) {
        return mongoBaseDao.findAll(example, sort, fields);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable, List<String> fields) {
        return mongoBaseDao.findAll(example, pageable, fields);
    }

    @Override
    public Page<T> findAll(Example<T> example, Sort sort, Pageable pageable, List<String> fields) {
        return mongoBaseDao.findAll(example, sort, pageable, fields);
    }
}
