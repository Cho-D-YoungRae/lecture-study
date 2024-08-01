package tobyspring.hellospring.exrate;

import tobyspring.hellospring.api.ApiTemplate;
import tobyspring.hellospring.payment.ExRateProvider;

import java.math.BigDecimal;

public class WebApiExRateProvider implements ExRateProvider {

    private final ApiTemplate apiTemplate;

    public WebApiExRateProvider(ApiTemplate apiTemplate) {
        this.apiTemplate = apiTemplate;
    }

    // 환율 가져오기 -> https://open.er-api.com/v6/latest/{기준통화}
    @Override
    public BigDecimal getExRate(String currency) {
        String url = "https://open.er-api.com/v6/latest/" + currency;
//        return apiTemplate.getExRate(url, new SimpleApiExecutor(), new ErApiExRateExtractor());
        return apiTemplate.getExRate(url);
    }

}
