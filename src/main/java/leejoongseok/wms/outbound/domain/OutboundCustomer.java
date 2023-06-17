package leejoongseok.wms.outbound.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 출고 주문의 고객 정보
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OutboundCustomer {
    private String customerAddress;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerZipCode;

    public OutboundCustomer(
            final String customerAddress,
            final String customerName,
            final String customerEmail,
            final String customerPhoneNumber,
            final String customerZipCode) {
        validateConstructor(
                customerAddress,
                customerName,
                customerEmail,
                customerPhoneNumber,
                customerZipCode);
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerZipCode = customerZipCode;
    }

    private void validateConstructor(
            final String customerAddress,
            final String customerName,
            final String customerEmail,
            final String customerPhoneNumber,
            final String customerZipCode) {
        Assert.hasText(customerAddress, "고객 주소는 필수입니다.");
        Assert.hasText(customerName, "고객 이름은 필수입니다.");
        Assert.hasText(customerEmail, "고객 이메일은 필수입니다.");
        Assert.hasText(customerPhoneNumber, "고객 전화번호는 필수입니다.");
        Assert.hasText(customerZipCode, "고객 우편번호는 필수입니다.");
    }
}
