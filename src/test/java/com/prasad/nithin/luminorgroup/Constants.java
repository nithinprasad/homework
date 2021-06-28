package com.prasad.nithin.luminorgroup;

import com.prasad.nithin.luminorgroup.model.PaymentProduct;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Constants {

    /**
     *
     */
    public static class TemplateConstants {

        public static final String PAYMENT_ID = "paymentId";
        public static final String PAYMENT_TYPE = "paymentType";
        private static final String BASE_URI = "payments/";
        public static final String PAYMENT_LIST = BASE_URI;
        private static final String SEPA = PaymentProduct.SEPA.getPaymentproduct();
        public static final String PAYMENT_CREATE_SEPA = BASE_URI + SEPA;
        private static final String PAYMENT_ID_TEMPLATE = "{" + PAYMENT_ID + "}";
        private static final String PAYMENT_TYPE_TEMPLATE = "{" + PAYMENT_TYPE + "}";
        public static final String GENERIC_PAYMENT = BASE_URI + PAYMENT_TYPE_TEMPLATE + "/" + PAYMENT_ID_TEMPLATE;
        private static final String TARGET2 = PaymentProduct.TARGET2.getPaymentproduct();
        public static final String PAYMENT_CREATE_TARGET2 = BASE_URI + TARGET2;
        private static final String SWIFT = PaymentProduct.SWIFT.getPaymentproduct();
        public static final String PAYMENT_CREATE_SWIFT = BASE_URI + SWIFT;
        public static final String AMOUNT = "amount";

    }

}
