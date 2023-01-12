package nl.tudelft.sem.contract.microservice.database.entities.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import nl.tudelft.sem.contract.commons.entities.utils.Dto;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself", "ConstantValue"})
    @Test
    void testEquals() {
        // Same entity -> equal
        DummyEntity entity1 = new DummyEntity(TestHelpers.getUuid(1));
        assertTrue(entity1.equals(entity1));
        // Different fields, same ID -> equal
        assertTrue(new DummyEntity(TestHelpers.getUuid(1)).equals(new DummyEntity(TestHelpers.getUuid(1))));
        // Same fields, different ID -> different
        assertFalse(new DummyEntity(TestHelpers.getUuid(1)).equals(new DummyEntity(TestHelpers.getUuid(2))));
        // One null -> different
        assertFalse(new DummyEntity(TestHelpers.getUuid(1)).equals(null));
        // One ID null -> different
        assertFalse(new DummyEntity(TestHelpers.getUuid(1)).equals(new DummyEntity(null)));
        // Both IDs null -> different
        assertFalse(new DummyEntity(null).equals(new DummyEntity(null)));
        // Different classes -> different
        assertFalse(new DummyEntity(TestHelpers.getUuid(1)).equals(new Object()));
    }

    @Test
    void testHashCode() {
        // Class hashcode implementation
        assertEquals(new DummyEntity(TestHelpers.getUuid(1)).hashCode(), new DummyEntity(TestHelpers.getUuid(2)).hashCode());
    }

    static class DummyDto implements Dto {
    }

    static class DummyEntity extends BaseEntity<DummyDto> {

        public DummyEntity(UUID id) {
            super(id);
        }

        @Override
        public DummyDto getDto() {
            return null;
        }
    }
}