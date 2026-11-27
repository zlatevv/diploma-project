package com.zlatev.tournament_management_system.config;

import com.zlatev.tournament_management_system.entity.enums.user.Role;
import com.zlatev.tournament_management_system.entity.model.user.User;
import com.zlatev.tournament_management_system.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.password}")
    private String password;

    @Bean
    public CommandLineRunner seedUsers() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("Users already exist in database; skipping initial user seed.");
                return;
            }

            String encodedPassword = passwordEncoder.encode(password);

            User admin = new User();
            admin.setEmail("admin@tournament.local");
            admin.setFullName("System Admin");
            admin.setPassword(encodedPassword);
            admin.setRole(Role.ADMIN);

            User organizer = new User();
            organizer.setEmail("organizer@tournament.local");
            organizer.setFullName("Tournament Organizer");
            organizer.setPassword(encodedPassword);
            organizer.setRole(Role.ORGANIZER);

            User referee = new User();
            referee.setEmail("referee@tournament.local");
            referee.setFullName("Match Referee");
            referee.setPassword(encodedPassword);
            referee.setRole(Role.REFEREE);

            userRepository.save(admin);
            userRepository.save(organizer);
            userRepository.save(referee);

            log.info("Seeded initial ADMIN, ORGANIZER, and REFEREE users.");
        };
    }
}