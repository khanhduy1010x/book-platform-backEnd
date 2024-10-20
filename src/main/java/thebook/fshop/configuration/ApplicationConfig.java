package thebook.fshop.configuration;

import java.util.Date;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.Account;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;
import thebook.fshop.repository.AccountsRepository;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(AccountsRepository accountsRepository) {
        return args -> {
            if (accountsRepository.findByUsername("admin").isEmpty()) {
                Account account = Account.builder()
                        .phone("9999")
                        .birth(new Date("10/10/2002"))
                        .avatar("No avatar")
                        .username("admin")
                        .email("admin@gmail.com")
                        .amount(9999999L)
                        .fullName("ADMIN")
                        .memberType(MemberType.PREMIUM)
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();
                accountsRepository.save(account);
                log.info("Tai khoan admin da duoc tao phone : 9999 password : admin");
            }
        };
    }
}
