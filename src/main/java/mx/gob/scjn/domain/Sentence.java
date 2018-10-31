package mx.gob.scjn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sentence.
 */
@Entity
@Table(name = "sentence")
@Document(indexName = "sentence")
public class Sentence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "jhi_group")
    private String group;

    @Column(name = "country")
    private String country;

    @Column(name = "status")
    private String status;

    @Column(name = "emisor")
    private String emisor;

    @Column(name = "facts")
    private String facts;

    @Column(name = "arguments_summary")
    private String argumentsSummary;

    @Column(name = "jhi_type")
    private Integer type;

    @OneToMany(mappedBy = "sentence")
    private Set<Resource> resources = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Sentence title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public Sentence group(String group) {
        this.group = group;
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCountry() {
        return country;
    }

    public Sentence country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public Sentence status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmisor() {
        return emisor;
    }

    public Sentence emisor(String emisor) {
        this.emisor = emisor;
        return this;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getFacts() {
        return facts;
    }

    public Sentence facts(String facts) {
        this.facts = facts;
        return this;
    }

    public void setFacts(String facts) {
        this.facts = facts;
    }

    public String getArgumentsSummary() {
        return argumentsSummary;
    }

    public Sentence argumentsSummary(String argumentsSummary) {
        this.argumentsSummary = argumentsSummary;
        return this;
    }

    public void setArgumentsSummary(String argumentsSummary) {
        this.argumentsSummary = argumentsSummary;
    }

    public Integer getType() {
        return type;
    }

    public Sentence type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Sentence resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Sentence addResource(Resource resource) {
        this.resources.add(resource);
        resource.setSentence(this);
        return this;
    }

    public Sentence removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.setSentence(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
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
        Sentence sentence = (Sentence) o;
        if (sentence.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sentence.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sentence{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", group='" + getGroup() + "'" +
            ", country='" + getCountry() + "'" +
            ", status='" + getStatus() + "'" +
            ", emisor='" + getEmisor() + "'" +
            ", facts='" + getFacts() + "'" +
            ", argumentsSummary='" + getArgumentsSummary() + "'" +
            ", type=" + getType() +
            "}";
    }
}
