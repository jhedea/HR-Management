package nl.tudelft.sem.user.microservice.database.entities.utils;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.tudelft.sem.user.commons.entities.utils.Dto;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<D extends Dto> {
    /**
     * id - random unique uuid assigned to a certain entity.
     */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    protected BaseEntity(BaseEntityBuilder<D, ?, ?> b) {
        this.id = b.idField;
    }

    /**
     * Convert the entity to a DTO.
     *
     * @return DTO for the specific entity.
     */
    public abstract Dto getDto();

    @Override
    public boolean equals(Object o) {
        if (o == null || id == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        return this == o || Objects.equals(id, ((BaseEntity<?>) o).id);
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public abstract static class BaseEntityBuilder
            <D extends Dto, C extends BaseEntity<D>, B extends BaseEntityBuilder<D, C, B>> {
        private transient UUID idField;

        public B id(UUID id) {
            this.idField = id;
            return self();
        }

        protected abstract B self();

        public abstract C build();
    }
}
