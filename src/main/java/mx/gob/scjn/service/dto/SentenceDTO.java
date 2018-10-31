package mx.gob.scjn.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Sentence entity.
 */
public class SentenceDTO implements Serializable {

    private Long id;

    private String title;

    private String group;

    private String country;

    private String status;

    private String emisor;

    private String facts;

    private String argumentsSummary;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getFacts() {
        return facts;
    }

    public void setFacts(String facts) {
        this.facts = facts;
    }

    public String getArgumentsSummary() {
        return argumentsSummary;
    }

    public void setArgumentsSummary(String argumentsSummary) {
        this.argumentsSummary = argumentsSummary;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SentenceDTO sentenceDTO = (SentenceDTO) o;
        if (sentenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sentenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SentenceDTO{" +
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
