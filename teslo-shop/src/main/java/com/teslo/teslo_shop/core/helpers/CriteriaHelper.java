package com.teslo.teslo_shop.core.helpers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CriteriaHelper<T> {

    private CriteriaBuilder cb;
    private CriteriaQuery<T> cq;
    private Root<T> root;

    public CriteriaHelper(CriteriaBuilder cb, Class<T> entityClass) {
        this.cb = cb;
        this.cq = cb.createQuery(entityClass);
        this.root = cq.from(entityClass);
    }

    public CriteriaBuilder cb() {
        return cb;
    }

    public CriteriaQuery<T> cq() {
        return cq;
    }

    public Root<T> root() {
        return root;
    }
}
