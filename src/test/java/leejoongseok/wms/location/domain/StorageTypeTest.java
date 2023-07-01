package leejoongseok.wms.location.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTypeTest {

    @Test
    @DisplayName("보관 유형이 호환 가능한지 확인한다.")
    void isCompatibleWith() {
        final StorageType cell = StorageType.CELL;
        final StorageType tote = StorageType.TOTE;

        assertTrue(cell.isCompatibleWith(cell));
        assertFalse(cell.isCompatibleWith(tote));
    }
}