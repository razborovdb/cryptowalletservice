package com.rdbprojects.cryptowallet.controllers;

import com.rdbprojects.cryptowallet.dao.PaymentsDao;
import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.entities.Payments;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class PaymentsController {
    @Autowired
    PaymentsDao paymentsDao;

    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/payments")
    public ResponseEntity<List<Payments>> getPaymentsByUserId(@RequestHeader(value="${json.token}") String token, @RequestBody Users findUserPayments) {
        if (findUserPayments == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (findUserPayments.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(findUserPayments.getEmail()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        List<Payments> payments = paymentsDao.findPaymentByUserId(findUserPayments);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/allpayments")
    public ResponseEntity<List<Payments>> getAllPayments(@RequestHeader(value="${json.token}") String token) {
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!user.getIsAdmin()) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        List<Payments> payments = paymentsDao.findAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/payment")
    public ResponseEntity<Payments> getPaymentByPaymentId(@RequestHeader(value="${json.token}") String token, @RequestBody Payments findPayment) {
        // check request
        if (findPayment == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (findPayment.getId() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(findPayment.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        // try to find payment
        Payments findedPayment = paymentsDao.findPaymentById(findPayment);
        if (findedPayment == null) {
            return ResponseEntity.status(400).eTag("Payment didn't find ...").body(null);
        }
        return ResponseEntity.ok(findedPayment);
    }

    @PostMapping("/payment")
    public ResponseEntity<Payments> addPayment(@RequestHeader(value="${json.token}") String token, @RequestBody Payments paymentDetails) {
        // check request
        if (paymentDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (paymentDetails.getId() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(paymentDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Payments findedPayment = paymentsDao.findPaymentById(paymentDetails);
        if (findedPayment == null) {
            Payments addedPayment = paymentsDao.addPayment(paymentDetails);
            return ResponseEntity.ok(addedPayment);
        }
        return ResponseEntity.status(400).eTag("Payment already exist ...").body(null);
    }

    @PutMapping("/payment")
    public ResponseEntity<Payments> updatePayment(@RequestHeader(value="${json.token}") String token, @RequestBody Payments paymentDetails) {
        // check request
        if (paymentDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (paymentDetails.getId() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(paymentDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Payments findedPayment = paymentsDao.findPaymentById(paymentDetails);
        if (findedPayment != null) {
            findedPayment.setPaymentDescription(paymentDetails.getPaymentDescription());

            Payments updatedPayment = paymentsDao.updatePayment(findedPayment);

            return ResponseEntity.ok(updatedPayment);
        }
        return ResponseEntity.status(400).eTag("Payment didn't find ...").body(null);

    }

    @DeleteMapping("/payment")
    public ResponseEntity<String> deletePayment(@RequestHeader(value="${json.token}") String token, @RequestBody Payments paymentDetails) {

        // check request
        if (paymentDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        if (paymentDetails.getId() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(paymentDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        //
        Payments deletedPayment = paymentsDao.deletePayment(paymentDetails);
        if (deletedPayment != null) {
            return ResponseEntity.ok("Payment deleted successfully");
        }
        return ResponseEntity.status(400).eTag("Payment didn't find ...").body("Payment didn't find ...");


    }
}
