package com.pharma.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Medecine.
 */
@Entity
@Table(name = "medecine")
@Document(indexName = "medecine")
public class Medecine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastupdate")
    private LocalDate lastupdate;

    @ManyToOne
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Medecine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLastupdate() {
        return lastupdate;
    }

    public Medecine lastupdate(LocalDate lastupdate) {
        this.lastupdate = lastupdate;
        return this;
    }

    public void setLastupdate(LocalDate lastupdate) {
        this.lastupdate = lastupdate;
    }

    public Store getStore() {
        return store;
    }

    public Medecine store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Medecine medecine = (Medecine) o;
        if (medecine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medecine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medecine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastupdate='" + getLastupdate() + "'" +
            "}";
    }
}
