package com.crptm.lambdaservice.accessors.res;

import com.crptm.lambdaservice.enums.EnumCurrency;
import com.crptm.lambdaservice.enums.EnumPaymentStatus;
import com.crptm.lambdaservice.http.res.HttpBaseResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusAccessorRes extends HttpBaseResponse {
    private EnumPaymentStatus paymentStatus;
    private Double amount;
    private EnumCurrency currency;
}
