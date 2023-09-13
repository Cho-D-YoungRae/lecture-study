package com.example.oauth2client;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/client-registrations/{registrationId}")
    public ClientRegistration getClientRegistration(@PathVariable String registrationId) {
        return clientRegistrationRepository.findByRegistrationId(registrationId);
    }
}
