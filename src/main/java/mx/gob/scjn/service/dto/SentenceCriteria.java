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
 * Criteria class for the Sentence entity. This class is used in SentenceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sentences?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SentenceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter group;

    private StringFilter country;

    private StringFilter status;

    private StringFilter emisor;

    private StringFilter facts;

    private StringFilter argumentsSummary;

    private IntegerFilter type;

    private LongFilter resourceId;

    public SentenceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getGroup() {
        return group;
    }

    public void setGroup(StringFilter group) {
        this.group = group;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getEmisor() {
        return emisor;
    }

    public void setEmisor(StringFilter emisor) {
        this.emisor = emisor;
    }

    public StringFilter getFacts() {
        return facts;
    }

    public void setFacts(StringFilter facts) {
        this.facts = facts;
    }

    public StringFilter getArgumentsSummary() {
        return argumentsSummary;
    }

    public void setArgumentsSummary(StringFilter argumentsSummary) {
        this.argumentsSummary = argumentsSummary;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public LongFilter getResourceId() {
        return resourceId;
    }

    public void setResourceId(LongFilter resourceId) {
        this.resourceId = resourceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SentenceCriteria that = (SentenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(group, that.group) &&
            Objects.equals(country, that.country) &&
            Objects.equals(status, that.status) &&
            Objects.equals(emisor, that.emisor) &&
            Objects.equals(facts, that.facts) &&
            Objects.equals(argumentsSummary, that.argumentsSummary) &&
            Objects.equals(type, that.type) &&
            Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        group,
        country,
        status,
        emisor,
        facts,
        argumentsSummary,
        type,
        resourceId
        );
    }

    @Override
    public String toString() {
        return "SentenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (group != null ? "group=" + group + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (emisor != null ? "emisor=" + emisor + ", " : "") +
                (facts != null ? "facts=" + facts + ", " : "") +
                (argumentsSummary != null ? "argumentsSummary=" + argumentsSummary + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (resourceId != null ? "resourceId=" + resourceId + ", " : "") +
            "}";
    }

}
