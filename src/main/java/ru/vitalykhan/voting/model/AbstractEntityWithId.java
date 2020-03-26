package ru.vitalykhan.voting.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntityWithId implements Persistable<Integer> {
    public static final int START_SEQ = 100_000;

    @Id
    @SequenceGenerator(name = "seq_generator", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")

//  See https://hibernate.atlassian.net/browse/HHH-3718 and https://hibernate.atlassian.net/browse/HHH-12034
//  Proxy initialization when accessing its identifier managed now by JPA_PROXY_COMPLIANCE setting
    protected Integer id;

    protected AbstractEntityWithId() {
    }

    protected AbstractEntityWithId(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(/*Hibernate.getClass(o)*/o.getClass())) {
            return false;
        }
        AbstractEntityWithId that = (AbstractEntityWithId) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}