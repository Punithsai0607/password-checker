package com.passwordchecker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.passwordchecker.util.CommonPasswordChecker;
import com.passwordchecker.util.PasswordScorer;
import com.passwordchecker.util.PasswordValidator;

/**
 * Application configuration class that declares Spring beans for the
 * password validation pipeline.
 *
 * <p>Externalises password policy parameters from {@code application.properties}
 * and wires them into the utility components via constructor injection.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
@Configuration
public class AppConfig {

    @Value("${password.policy.min-length:8}")
    private int minLength;

    @Value("${password.policy.max-length:128}")
    private int maxLength;

    @Value("${password.policy.max-repeated-chars:3}")
    private int maxRepeatedChars;

    @Value("${password.policy.max-sequential-chars:3}")
    private int maxSequentialChars;

    /**
     * Creates the {@link CommonPasswordChecker} bean.
     *
     * @return a configured CommonPasswordChecker instance
     */
    @Bean
    public CommonPasswordChecker commonPasswordChecker() {
        return new CommonPasswordChecker();
    }

    /**
     * Creates the {@link PasswordValidator} bean with policy parameters.
     *
     * @param commonPasswordChecker the common password checker dependency
     * @return a configured PasswordValidator instance
     */
    @Bean
    public PasswordValidator passwordValidator(CommonPasswordChecker commonPasswordChecker) {
        return new PasswordValidator(minLength, maxLength, maxRepeatedChars,
                maxSequentialChars, commonPasswordChecker);
    }

    /**
     * Creates the {@link PasswordScorer} bean.
     *
     * @return a configured PasswordScorer instance
     */
    @Bean
    public PasswordScorer passwordScorer() {
        return new PasswordScorer();
    }
}
