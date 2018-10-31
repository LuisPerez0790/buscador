package mx.gob.scjn.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Resource entity. This class is used in ResourceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /resources?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ResourceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fileId;

    private StringFilter title;

    private StringFilter path;

    private LongFilter sentenceId;

    public ResourceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getFileId() {
        return fileId;
    }

    public void setFileId(LongFilter fileId) {
        this.fileId = fileId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public LongFilter getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(LongFilter sentenceId) {
        this.sentenceId = sentenceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResourceCriteria that = (ResourceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileId, that.fileId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(path, that.path) &&
            Objects.equals(sentenceId, that.sentenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileId,
        title,
        path,
        sentenceId
        );
    }

    @Override
    public String toString() {
        return "ResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fileId != null ? "fileId=" + fileId + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (sentenceId != null ? "sentenceId=" + sentenceId + ", " : "") +
            "}";
    }

}
