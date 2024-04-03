package com.nhnacademy.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 공통 설정
 * @author insub
 * @version 1.0.0
 */
@Configuration
public class CommonConfig {

    /**
     * Member 생성 로직에 필요한 PasswordEncoder Bean
     * @return BCryptPasswordEncoder 객체
     * @author insub
     * @since  1.0.0
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
