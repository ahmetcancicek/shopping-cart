package com.shopping;


import com.shopping.repository.CustomerRepository;
import com.shopping.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@AllArgsConstructor
@EnableSwagger2
public class ShoppingCartApplication {

    private final RoleService roleService;
    private final CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                roleService.save(Role.builder().name("USER").build());
//                roleService.save(Role.builder().name("ADMIN").build());
//
//                customerRepository.save(Customer.builder()
//                        .firstName("George")
//                        .lastName("House")
//                        .user(User.builder()
//                                .roles(Set.of(Role.builder().name("ADMIN").build()))
//                                .username("georgehouse")
//                                .password(new BCryptPasswordEncoder().encode("DHN827D9N"))
//                                .email("georgehouse@email.com")
//                                .build())
//                        .build());
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
