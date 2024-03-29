package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Payment.PaymentDTO;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("payment")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/balance")
    public ResponseEntity<?> getBalanceForUser(@RequestParam Integer userID) {

        if (userID == null) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(paymentService.getUserPaymentsApiResponse(userID));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> depositAmount(@RequestBody PaymentDTO payment) {

        if (payment == null) {
            return ApiResponseUtil.errorApiResponse("Payment data is missing!\nContact support for more information!");
        }
        if (payment.getUserId() == null) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        if (payment.getAmount() == null) {
            return ApiResponseUtil.errorApiResponse("Deposit amount is missing!");
        }
        return ApiResponseUtil.handleApiResponse(paymentService.addPaymentApiResponse(payment, Constants.PAYMENT_DEPOSIT));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawAmount(@RequestBody PaymentDTO payment) {

        if (payment == null) {
            return ApiResponseUtil.errorApiResponse("Payment data is missing!\nContact support for more information!");
        }
        if (payment.getUserId() == null) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        if (payment.getAmount() == null) {
            return ApiResponseUtil.errorApiResponse("Deposit amount is missing!");
        }
        payment.setAmount(payment.getAmount().negate());
        return ApiResponseUtil.handleApiResponse(paymentService.addPaymentApiResponse(payment, Constants.PAYMENT_WITHDRAW));
    }
}
