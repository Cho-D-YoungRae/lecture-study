package tobyspring.hellospring;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentService {

    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {
        // 환율 가져오기 -> https://open.er-api.com/v6/latest/{기준통화}
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining());
        br.close();

        ObjectMapper mapper = new ObjectMapper();
        ExRateData exRateData = mapper.readValue(response, ExRateData.class);
        BigDecimal exRate = exRateData.rates().get("KRW");

        // 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);

        // 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);
        return new Payment(orderId, currency, foreignCurrencyAmount, exRate, convertedAmount, validUntil);
    }

    public static void main(String[] args) throws IOException {
        final PaymentService paymentService = new PaymentService();
        final Payment payment = paymentService.prepare(100L, "USD", new BigDecimal("50.7"));
        System.out.println(payment);
    }
}
