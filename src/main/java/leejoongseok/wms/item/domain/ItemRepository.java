package leejoongseok.wms.item.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 상품 바코드로 상품을 조회한다.
     */
    @Query("select i from Item i where i.itemBarcode = :itemBarcode")
    Optional<Item> findByItemBarcode(String itemBarcode);

}
