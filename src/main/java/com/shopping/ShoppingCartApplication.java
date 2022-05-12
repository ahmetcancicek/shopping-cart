package com.shopping;
;
import com.shopping.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@AllArgsConstructor
public class ShoppingCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

//    private final CustomerRepository customerRepository;

    @Bean
    CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//                String hashedPassword = passwordEncoder.encode("12345");
//                HashSet<Role> roles = new HashSet<>();
//                roles.add(Role.builder().role("user").build());
//
//                Customer customer = Customer.builder()
//                        .firstName("Steve")
//                        .lastName("Clock")
//                        .user(User.builder()
//                                .email("Steveclock@email.com")
//                                .username("steve")
//                                .password(hashedPassword)
//                                .roles(roles)
//                                .active(true)
//                                .build())
//                        .build();
//
//                customerRepository.save(customer);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
