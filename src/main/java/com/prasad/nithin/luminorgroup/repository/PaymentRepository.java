package com.prasad.nithin.luminorgroup.repository;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment,String> {

    public List<Payment> findByPaymentTypeAndPaymentId(String paymentType,String paymentId);

    public List<Payment> findByPaymentStatusInAndAmount(Collection<PaymentStatus> paymentType, BigDecimal amount);

    public List<Payment> findAllByAmount( BigDecimal amount);

    public List<Payment> findByPaymentStatusIn(Collection<PaymentStatus> paymentType);
}
