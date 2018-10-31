package mx.gob.scjn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Document(indexName = "resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "title")
    private String title;

    @Column(name = "path")
    private String path;

    @ManyToOne
    @JsonIgnoreProperties("resources")
    private Sentence sentence;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public Resource fileId(Long fileId) {
        this.fileId = fileId;
        return this;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public Resource title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public Resource path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public Resource sentence(Sentence sentence) {
        this.sentence = sentence;
        return this;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
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
        Resource resource = (Resource) o;
        if (resource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", fileId=" + getFileId() +
            ", title='" + getTitle() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
