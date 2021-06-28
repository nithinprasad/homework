package com.prasad.nithin.luminorgroup;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.Currency;
import com.prasad.nithin.luminorgroup.model.*;
import com.prasad.nithin.luminorgroup.service.PaymentCancelFeeCalculatorService;
import com.prasad.nithin.luminorgroup.service.PaymentService;
import com.prasad.nithin.luminorgroup.validator.PaymentCancelValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class LuminorgroupApplicationTests {

    private static final Set<Payment> payments = new HashSet<>();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    int randomServerPort;
    @Autowired
    PaymentCancelValidator paymentCancelValidator;

    @Autowired
    PaymentCancelFeeCalculatorService calculatorService;

    private String url;

    @BeforeEach
    public void setUp() {
        url = String.format("http://localhost:%d/", randomServerPort);
    }

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    public void veifySepaCreation() {

        ResponseEntity<Payment> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_CREATE_SEPA, HttpMethod.POST, generateRandomSepaPayment(), Payment.class);
        Assertions.assertEquals(HttpStatus.CREATED, paymentResponseEntity.getStatusCode());
        Payment payment = paymentResponseEntity.getBody();
        Assertions.assertNotNull((Payment.class.cast(payment).getPaymentId()));
        payments.add(Payment.class.cast(payment));

    }


    @Test
    @Order(3)
    public void verifyTarget2Creation() {

        ResponseEntity<Payment> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_CREATE_TARGET2, HttpMethod.POST, generateRandomTarget2Payment(), Payment.class);
        Assertions.assertEquals(HttpStatus.CREATED, paymentResponseEntity.getStatusCode());
        Payment payment = paymentResponseEntity.getBody();
        Assertions.assertNotNull(payment.getPaymentId());
        payments.add(payment);
    }

    @Test
    @Order(4)
    public void verifySwiftCreation() {

        ResponseEntity<Payment> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_CREATE_SWIFT, HttpMethod.POST, generateRandomCrossBorderPayment(), Payment.class);
        Assertions.assertEquals(HttpStatus.CREATED, paymentResponseEntity.getStatusCode());
        Payment payment=paymentResponseEntity.getBody();
        Assertions.assertNotNull(payment.getPaymentId());
        payments.add(payment);
    }

    @Test
    @Order(5)
    public void verifyPaymentCancellation() {

        ResponseEntity<Payment> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_CREATE_SEPA, HttpMethod.POST, generateRandomSepaPayment(), Payment.class);
        Assertions.assertEquals(HttpStatus.CREATED, paymentResponseEntity.getStatusCode());
        Payment payment = paymentResponseEntity.getBody();
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TemplateConstants.PAYMENT_TYPE, payment.getPaymentType());
        map.put(Constants.TemplateConstants.PAYMENT_ID, payment.getPaymentId());
        Assertions.assertNotNull(payment.getPaymentId());
        ResponseEntity<Payment> cancelledPayment = invokeEntity(Constants.TemplateConstants.GENERIC_PAYMENT, HttpMethod.DELETE, null, Payment.class, map);
        Assertions.assertNotNull(cancelledPayment.getBody());
        Assertions.assertEquals(PaymentStatus.CANCELLED, cancelledPayment.getBody().getPaymentStatus());
        payments.add(cancelledPayment.getBody());
    }

    @Test
    @Order(6)
    public void verifyAllPayments() {
        ResponseEntity<Payment[]> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_LIST, HttpMethod.GET, null, Payment[].class);
        Assertions.assertEquals(HttpStatus.OK, paymentResponseEntity.getStatusCode());
        Payment[] payment = paymentResponseEntity.getBody();
        Assertions.assertEquals(0, Arrays.asList(payment).stream().filter(e -> PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).count());
        List<String> paymentIds = payments.stream().filter(e -> !PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).map(Payment::getPaymentId).collect(Collectors.toList());
        Assertions.assertEquals(paymentIds.size(), Arrays.asList(payment).size());
    }

    @Test
    @Order(7)
    public void verifyCancelledPaymentDetails() {
        Optional<Payment> cancelledPayment = payments.stream().filter(e -> PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).findFirst();
        if (cancelledPayment.isPresent()) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.TemplateConstants.PAYMENT_TYPE, cancelledPayment.get().getPaymentType());
            map.put(Constants.TemplateConstants.PAYMENT_ID, cancelledPayment.get().getPaymentId());
            ResponseEntity<Payment> cancelledPaymentDetails = invokeEntity(Constants.TemplateConstants.GENERIC_PAYMENT, HttpMethod.GET, generateRandomCrossBorderPayment(), Payment.class, map);
            Assertions.assertNotNull(cancelledPaymentDetails.getBody().getCancellationFee());
            Assertions.assertEquals(PaymentStatus.CANCELLED, cancelledPaymentDetails.getBody().getPaymentStatus());
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(8)
    public void verifySearchByAmount() {
        Optional<Payment> randomPayment = payments.stream().filter(e -> !PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).findFirst();
        if (randomPayment.isPresent()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TemplateConstants.AMOUNT, randomPayment.get().getAmount().toString());
            ResponseEntity<Payment[]> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_LIST, HttpMethod.GET, null, Payment[].class, Collections.emptyMap(), map);
            Assertions.assertEquals(HttpStatus.OK, paymentResponseEntity.getStatusCode());
            log.info("Created List{}", payments.stream().filter(e -> randomPayment.get().getPaymentId().equals(e.getPaymentId())).collect(Collectors.toList()));
            log.info("ResponseList {}", Arrays.asList(paymentResponseEntity.getBody()));
            Assertions.assertEquals(true, Arrays.asList(paymentResponseEntity.getBody()).stream().map(Payment::getPaymentId).collect(Collectors.toList()).contains(randomPayment.get().getPaymentId()));
            Assertions.assertEquals(true, Arrays.asList(paymentResponseEntity.getBody()).stream().map(Payment::getAmount).collect(Collectors.toList()).contains(randomPayment.get().getAmount()));
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(9)
    public void verifyCancelPastDated() {

        Optional<Payment> optionalPayment = payments.stream().filter(e -> !PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).findFirst();
        if (optionalPayment.isPresent()) {
            try {
                Payment payment = optionalPayment.get();
                payment.setCreatedDateTime(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
                paymentCancelValidator.checkIfValid(payment);
            } catch (CustomExcpetion customExcpetion) {
                log.info("Response",customExcpetion);
                Assertions.assertEquals(HttpStatus.BAD_REQUEST,customExcpetion.getStatus());
                customExcpetion.getErrorBodies().forEach(e->Assertions.assertEquals(e.getCode(),"DATE_EXPIRED"));
            }
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(10)
    public void verifyCancelSameDay() {
        Optional<Payment> optionalPayment = payments.stream().filter(e -> !PaymentStatus.CANCELLED.equals(e.getPaymentStatus())).findFirst();
        if (optionalPayment.isPresent()) {
            try {
                Payment payment = optionalPayment.get();
                payment.setCreatedDateTime(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
                paymentCancelValidator.checkIfValid(payment);
                double feeCalculated=calculatorService.calculateFee(payment);
                log.info("Fee Calulcated for {} is {}",payment,feeCalculated);
                Assertions.assertNotEquals(0,feeCalculated);
            } catch (CustomExcpetion customExcpetion) {
                Assertions.fail();
            }
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(11)
    public void verifyCancelAlreadyCancelled() {
        ResponseEntity<Payment> paymentResponseEntity = invokeEntity(Constants.TemplateConstants.PAYMENT_CREATE_SEPA, HttpMethod.POST, generateRandomSepaPayment(), Payment.class);
        Assertions.assertEquals(HttpStatus.CREATED, paymentResponseEntity.getStatusCode());
        Payment payment = paymentResponseEntity.getBody();
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TemplateConstants.PAYMENT_TYPE, payment.getPaymentType());
        map.put(Constants.TemplateConstants.PAYMENT_ID, payment.getPaymentId());
        Assertions.assertNotNull(payment.getPaymentId());
        ResponseEntity<Payment> cancelledPayment = invokeEntity(Constants.TemplateConstants.GENERIC_PAYMENT, HttpMethod.DELETE, null, Payment.class, map);
        Assertions.assertNotNull(cancelledPayment.getBody());
        Assertions.assertEquals(PaymentStatus.CANCELLED, cancelledPayment.getBody().getPaymentStatus());
        ResponseEntity<Object> cancelledAgain = invokeEntity(Constants.TemplateConstants.GENERIC_PAYMENT, HttpMethod.DELETE, null, Object.class, map);
        Assertions.assertNotNull(cancelledAgain.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, cancelledAgain.getStatusCode());
    }

    private <T> ResponseEntity<T> invokeEntity(String path, HttpMethod method, Object entity, Class<T> reposeClass) {
        return invokeEntity(path, method, entity, reposeClass, Collections.emptyMap());
    }

    private <T> ResponseEntity<T> invokeEntity(String path, HttpMethod method, Object entity, Class<T> reposeClass, Map<String, Object> args) {
        return invokeEntity(path, method, entity, reposeClass, args, Collections.emptyMap());
    }

    private <T> ResponseEntity<T> invokeEntity(String path, HttpMethod method, Object entity, Class<T> reposeClass, Map<String, Object> pathTemplate, Map<String, String> queryParam) {
        HttpHeaders headers = new HttpHeaders();
        String fullURI = url + path;
        LinkedMultiValueMap<String, String> multiValuesMap = new LinkedMultiValueMap<String, String>();
        queryParam.forEach(multiValuesMap::add);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(fullURI)
                .queryParams(multiValuesMap)
                .uriVariables(pathTemplate);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<Object>(entity, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(builder.toUriString(), method, httpEntity, reposeClass, pathTemplate);
        log.info("Rest api response {}", responseEntity.toString());
        return responseEntity;
    }

    private SepaPayment generateRandomSepaPayment() {
        return SepaPayment.builder()
                .amount(randomAmount())
                .details(randomIBAN())
                .creditorIban(randomIBAN())
                .debtorIban(randomIBAN())
                .currency(Currency.EUR)
                .build();
    }

    private Target2Payment generateRandomTarget2Payment() {
        return Target2Payment.builder()
                .amount(randomAmount())
                .creditorIban(randomIBAN())
                .debtorIban(randomIBAN())
                .currency(Currency.EUR)
                .build();
    }

    private CrossBorderPayment generateRandomCrossBorderPayment() {
        return CrossBorderPayment.builder()
                .amount(randomAmount())
                .creditorIban(randomIBAN())
                .debtorIban(randomIBAN())
                .currency(Currency.EUR)
                .bic("123")
                .build();
    }

    private Number randomAmount() {
        Random r = new Random();
        return (r.nextInt(4000) / 10.0);
    }

    private String randomIBAN() {
        Random rand = new Random();
        String card = "EE";
        return "EE"+IntStream.rangeClosed(0,10)
                .boxed()
                .map(n->rand.nextInt(10) + 0+"")
                .collect(Collectors.joining());

    }


}
