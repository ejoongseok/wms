package leejoongseok.wms.item.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.item.domain.Category;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemRepository;
import leejoongseok.wms.item.domain.ItemSize;
import leejoongseok.wms.item.domain.TemperatureZone;
import leejoongseok.wms.item.exception.ItemAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateItem {
    private final ItemRepository itemRepository;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    void request(@RequestBody @Valid final Request request) {
        itemRepository.findByItemBarcode(request.itemBarcode).ifPresent(item -> {
            throw new ItemAlreadyExistsException(request.itemBarcode);
        });

        final Item item = request.toEntity();
        itemRepository.save(item);
    }

    public record Request(
            @NotBlank(message = "상품명은 필수입니다.")
            String itemName,
            @NotBlank(message = "상품 바코드는 필수입니다.")
            String itemBarcode,
            @NotBlank(message = "상품 설명은 필수입니다.")
            String description,
            @NotBlank(message = "브랜드명은 필수입니다.")
            String brandName,
            @NotBlank(message = "제조사명은 필수입니다.")
            String makerName,
            @Min(value = 1, message = "상품의 가로 길이는 1mm 이상이어야 합니다.")
            Integer widthMillimeter,
            @Min(value = 1, message = "상품의 세로 길이는 1mm 이상이어야 합니다.")
            Integer lengthMillimeter,
            @Min(value = 1, message = "상품의 높이는 1mm 이상이어야 합니다.")
            Integer heightMillimeter,
            @Min(value = 1, message = "상품의 무게는 1g 이상이어야 합니다.")
            Integer weightInGrams,
            @NotNull(message = "상품의 보관 온도는 필수입니다.")
            TemperatureZone temperatureZone,
            @NotNull(message = "상품의 카테고리는 필수입니다.")
            Category category
    ) {
        private Item toEntity() {
            return new Item(
                    itemName,
                    itemBarcode,
                    description,
                    brandName,
                    makerName,
                    new ItemSize(widthMillimeter, lengthMillimeter, heightMillimeter),
                    weightInGrams,
                    temperatureZone,
                    category
            );
        }
    }
}
