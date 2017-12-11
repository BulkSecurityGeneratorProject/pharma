package com.pharma.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Store.
 */
@Entity
@Table(name = "store")
@Document(indexName = "store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Lob
    @Column(name = "jhi_datafile")
    private byte[] datafile;

    @Column(name = "jhi_datafile_content_type")
    private String datafileContentType;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private Set<Medecine> medecines = new HashSet<>();

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

    public Store name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public Store country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public Store city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Store latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Store longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public byte[] getDatafile() {
        return datafile;
    }

    public Store datafile(byte[] datafile) {
        this.datafile = datafile;
        return this;
    }

    public void setDatafile(byte[] datafile) {
        this.datafile = datafile;
    }

    public String getDatafileContentType() {
        return datafileContentType;
    }

    public Store datafileContentType(String datafileContentType) {
        this.datafileContentType = datafileContentType;
        return this;
    }

    public void setDatafileContentType(String datafileContentType) {
        this.datafileContentType = datafileContentType;
    }

    public Set<Medecine> getMedecines() {
        return medecines;
    }

    public Store medecines(Set<Medecine> medecines) {
        this.medecines = medecines;
        return this;
    }

    public Store addMedecine(Medecine medecine) {
        this.medecines.add(medecine);
        medecine.setStore(this);
        return this;
    }

    public Store removeMedecine(Medecine medecine) {
        this.medecines.remove(medecine);
        medecine.setStore(null);
        return this;
    }

    public void setMedecines(Set<Medecine> medecines) {
        this.medecines = medecines;
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
        Store store = (Store) o;
        if (store.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", datafile='" + getDatafile() + "'" +
            ", datafileContentType='" + datafileContentType + "'" +
            "}";
    }
}
