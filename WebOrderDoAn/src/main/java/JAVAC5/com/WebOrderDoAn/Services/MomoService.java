package JAVAC5.com.WebOrderDoAn.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoService {

    private static final String MOMO_ENDPOINT = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
    private static final String PARTNER_CODE = "MOMO5RGX20191128";
    private static final String ACCESS_KEY = "M8brj9K6E22vXoDB";
    private static final String SECRET_KEY = "nqQiVSgDMy809JoPF6OzP5OdBUB550Y4";
    private static final String RETURN_URL = "http://localhost:8080/cart";
    private static final String NOTIFY_URL = "http://localhost:8080/cart";


    public boolean processPayment(double amount) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Create request body
            String requestBody = createMomoRequestBody(amount);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(MOMO_ENDPOINT, HttpMethod.POST, entity, String.class);

            // Handle response from Momo
            if (response.getStatusCode().is2xxSuccessful()) {
                return handleMomoResponse(response.getBody());
            } else {
                // Handle error calling Momo API, e.g., log error and return false
                System.err.println("Failed to call Momo API: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createMomoRequestBody(double amount) {
        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();

        // Raw signature string
        String rawSignature = String.format(
                "accessKey=%s&amount=%.2f&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=captureWallet",
                ACCESS_KEY, amount, NOTIFY_URL, orderId, "Payment with MoMo", PARTNER_CODE, RETURN_URL, requestId);

        // Generate the signature
        String signature = signHmacSHA256(rawSignature, SECRET_KEY);

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", PARTNER_CODE);
        requestBody.put("accessKey", ACCESS_KEY);
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", "Payment with MoMo");
        requestBody.put("returnUrl", RETURN_URL);
        requestBody.put("notifyUrl", NOTIFY_URL);
        requestBody.put("extraData", "");
        requestBody.put("requestType", "captureWallet");
        requestBody.put("signature", signature);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating JSON request body for Momo API", e);
        }
    }

    private String signHmacSHA256(String data, String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hmac = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmac);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private boolean handleMomoResponse(String responseBody) {
        // Handle response from Momo API
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

            // Check payment status from Momo
            if ("0".equals(responseMap.get("errorCode").toString())) {
                // Payment successful
                return true;
            } else {
                // Payment failed, handle other cases
                System.err.println("Momo payment failed: " + responseMap.get("message"));
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
