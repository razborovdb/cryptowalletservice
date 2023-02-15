package com.rdbprojects.cryptowallet.dao;

import com.rdbprojects.cryptowallet.entities.Payments;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.repositories.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentsDao {
    @Autowired
    PaymentsRepository paymentsRepository;

    public Payments addPayment(Payments payment) {
        try {
            Payments savedPayments = paymentsRepository.save(payment);
            return savedPayments;
        } catch (Exception e) {
            return null;
        }

    }

    public Payments findPaymentById(Payments payment) {
        try {
            Payments findedPayment = paymentsRepository.findPaymentById(payment.getId());
            return findedPayment;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Payments> findPaymentByUserId(Users user) {
        try {
            List<Payments> findedPayments = paymentsRepository.findPaymentByUserId(user.getEmail());
            return findedPayments;
        } catch (Exception e) {
            return null;
        }

    }

    public Payments updatePayment(Payments payment) {
        try {
            Payments updatedPayment = paymentsRepository.save(payment);
            return updatedPayment;
        } catch (Exception e) {
            return null;
        }

    }

    public Payments deletePayment(Payments payment) {
        try {
            paymentsRepository.delete(payment);
            return payment;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Payments> findAll() {
        try {
            List<Payments> findedPayments = paymentsRepository.findAll();
            return findedPayments;
        } catch (Exception e) {
            return null;
        }

    }
}
