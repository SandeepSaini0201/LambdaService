package com.crptm.lambdaservice.service.interfaces;

import com.crptm.lambdaservice.enums.EnumLambdaStatus;
import org.hibernate.SessionFactory;

public interface IPaymentStatusService {
    EnumLambdaStatus updatePaymentStatus();
}
