package com.geercode.elehall.data.mongo;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MongoBaseDaoImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements MongoBaseDao<T, ID> {
    private final MongoEntityInformation<T, ID> entityInformation;
    private final MongoOperations mongoOperations;
    private final Class<T> clazz;
    
    private static final String DOC_FIELD_ID_NAME = "_id";

    public MongoBaseDaoImpl(MongoEntityInformation<T, ID> entityInformation, MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
        clazz = entityInformation.getJavaType();
    }

    @Override
    public MongoOperations getMongoOperations() {
        return this.mongoOperations;
    }

    @Override
    public Class<T> getEntityClass() {
        return this.clazz;
    }

    @Override
    public Page<T> findAll(Sort sort, Pageable pageable) {
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        Long count = count();
        Query query = new Query().with(sort).with(pageable);
        List<T> list = findAll(query);

        return new PageImpl<T>(list, pageable, count);
    }

    @Override
    public List<T> findAll(List<String> returnFields) {
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query();
        MongoOperationsHelper.restrictQueryFields(query, returnFields);

        return findAll(query);
    }

    @Override
    public List<T> findAll(Sort sort, List<String> returnFields) {
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query().with(sort);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);

        return findAll(query);
    }

    @Override
    public Page<T> findAll(Pageable pageable, List<String> returnFields) {
        Assert.notNull(pageable, "Pageable must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Long count = count();
        Query query = new Query().with(pageable);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);
        List<T> list = findAll(query);

        return new PageImpl<T>(list, pageable, count);
    }

    @Override
    public Page<T> findAll(Sort sort, Pageable pageable, List<String> returnFields) {
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Long count = count();
        Query query = new Query().with(sort).with(pageable);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);
        List<T> list = findAll(query);

        return new PageImpl<T>(list, pageable, count);
    }

    @Override
    public Page<T> findAll(Example<T> example, Sort sort, Pageable pageable) {
        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        Query query = new Query(new Criteria().alike(example)).with(sort).with(pageable);
        List<T> list = findAll(query);

        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoOperations.count(query, example.getProbeType(), entityInformation.getCollectionName()));
    }

    @Override
    public List<T> findAll(Example<T> example, List<String> returnFields) {
        Assert.notNull(example, "Sample must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query(new Criteria().alike(example));
        MongoOperationsHelper.restrictQueryFields(query, returnFields);

        return findAll(query);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort, List<String> returnFields) {
        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query(new Criteria().alike(example)).with(sort);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);

        return findAll(query);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable, List<String> returnFields) {
        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query(new Criteria().alike(example)).with(pageable);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);
        List<T> list = findAll(query);

        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoOperations.count(query, example.getProbeType(), entityInformation.getCollectionName()));
    }

    @Override
    public Page<T> findAll(Example<T> example, Sort sort, Pageable pageable, List<String> returnFields) {
        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(sort, "Sort must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");
        Assert.notEmpty(returnFields, "ReturnFields must not be empty!");

        Query query = new Query(new Criteria().alike(example)).with(sort).with(pageable);
        MongoOperationsHelper.restrictQueryFields(query, returnFields);
        List<T> list = findAll(query);

        return PageableExecutionUtils.getPage(list, pageable,
                () -> mongoOperations.count(query, example.getProbeType(), entityInformation.getCollectionName()));
    }

    @Override
    public long updateDynamic(Object id, T updateObject) {
        Assert.notNull(id, "ID must not be null!");
        Assert.notNull(updateObject, "Entity must not be null!");

        Query query = new Query(Criteria.where(DOC_FIELD_ID_NAME).is(id));
        Update update = MongoOperationsHelper.getDynamicUpdateByObject(updateObject, clazz);
        return mongoOperations.updateFirst(query, update, clazz).getModifiedCount();
    }

    @Override
    public long updateDynamic(Object id, Map<String, Object> updateFieldsMap) {
        Assert.notNull(id, "ID must not be null!");
        Assert.notEmpty(updateFieldsMap, "FieldsMap must not be empty!");

        Query query = new Query(Criteria.where(DOC_FIELD_ID_NAME).is(id));
        Update update = new Update();
        for (Map.Entry entry : updateFieldsMap.entrySet()) {
            update.set((String) entry.getKey(), entry.getValue());
        }
        return mongoOperations.updateFirst(query, update, clazz).getModifiedCount();
    }

    @Override
    public long updateDynamic(Object id, String updateField, Object updateValue) {
        Assert.notNull(id, "ID must not be null!");
        Assert.isTrue(!StringUtils.isEmpty(updateField), "Field must not be empty!");

        Query query = new Query(Criteria.where(DOC_FIELD_ID_NAME).is(id));
        Update update = new Update().set(updateField, updateValue);
        return mongoOperations.updateFirst(query, update, clazz).getModifiedCount();
    }

    @Override
    public long updateFirstDynamic(T queryObject, T updateObject) {
        Assert.notNull(queryObject, "QueryObject must not be null!");
        Assert.notNull(updateObject, "UpdateObject must not be null!");

        Query query = MongoOperationsHelper.getDynamicQueryByObject(queryObject, clazz);
        Update update = MongoOperationsHelper.getDynamicUpdateByObject(updateObject, clazz);
        return mongoOperations.updateFirst(query, update, clazz).getModifiedCount();
    }

    @Override
    public long updateMultiDynamic(T queryObject, T updateObject) {
        Assert.notNull(queryObject, "QueryObject must not be null!");
        Assert.notNull(updateObject, "UpdateObject must not be null!");

        Query query = MongoOperationsHelper.getDynamicQueryByObject(queryObject, clazz);
        Update update = MongoOperationsHelper.getDynamicUpdateByObject(updateObject, clazz);
        return mongoOperations.updateMulti(query, update, clazz).getModifiedCount();
    }

    @Override
    public long upsertDynamic(T queryObject, T updateObject) {
        Assert.notNull(queryObject, "QueryObject must not be null!");
        Assert.notNull(updateObject, "UpdateObject must not be null!");

        Query query = MongoOperationsHelper.getDynamicQueryByObject(queryObject, clazz);
        Update update = MongoOperationsHelper.getDynamicUpdateByObject(updateObject, clazz);
        return mongoOperations.upsert(query, update, clazz).getModifiedCount();
    }

    @Override
    public List<T> findAllByField(String queryField, Iterable<ID> fieldValues) {
        Assert.notNull(queryField, "Field must not be null!");
        Assert.notNull(fieldValues, "FieldValues must not be null!");

        if (IterableUtils.isEmpty(fieldValues)) {
            return Collections.EMPTY_LIST;
        }

        Query query = new Query(new Criteria(queryField).in(Streamable.of(fieldValues).stream().collect(StreamUtils.toUnmodifiableList())));

        return findAll(query);
    }

    @Override
    public List<T> findAllByField(String queryField, Iterable<ID> fieldValues, List<String> fields) {
        Assert.notNull(queryField, "Field must not be null!");
        Assert.notNull(fieldValues, "FieldValues must not be null!");
        Assert.notEmpty(fields, "Fields must not be empty!");

        if (IterableUtils.isEmpty(fieldValues)) {
            return Collections.EMPTY_LIST;
        }

        Query query = new Query(new Criteria(queryField).in(Streamable.of(fieldValues).stream().collect(StreamUtils.toUnmodifiableList())));

        MongoOperationsHelper.restrictQueryFields(query, fields);
        return findAll(query);
    }

    private List<T> findAll(Query query) {
        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }
}
