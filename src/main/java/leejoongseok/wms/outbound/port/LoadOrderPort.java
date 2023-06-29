package leejoongseok.wms.outbound.port;

import leejoongseok.wms.item.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문을 조회하는 포트
 * 실제로는 주문을 조회하는 API를 호출하거나, DB에서 조회하는 등의 로직이 들어감.
 */
@Component
@RequiredArgsConstructor
public class LoadOrderPort {
    private final ItemRepository itemRepository;

    public Order getBy(final Long orderId) {
        return new Order(
                orderId,
                "서울시 강남구 테헤란로 427",
                "홍길동",
                "ejoongseok@gmail.com",
                "010-1234-5678",
                "12345",
                LocalDate.now().plusDays(1),
                false,
                "출고 요청 사항",
                "배송 요청 사항",
                LocalDateTime.now(),
                List.of(
                        new OrderItem(
                                itemRepository.findById(1L).get(),
                                2,
                                BigDecimal.valueOf(1000)
                        ))

        );
    }
}
