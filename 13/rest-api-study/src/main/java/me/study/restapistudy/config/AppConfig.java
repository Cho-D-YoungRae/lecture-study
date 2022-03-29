package me.study.restapistudy.config;

import me.study.restapistudy.account.Account;
import me.study.restapistudy.account.AccountRepository;
import me.study.restapistudy.account.AccountRole;
import me.study.restapistudy.account.AccountService;
import me.study.restapistudy.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            private AccountService accountService;

            @Autowired
            private AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account userAccount = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRole.USER))
                        .build();
                accountService.saveAccount(userAccount);

                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                        .build();
                accountService.saveAccount(admin);
            }
        };
    }
}
