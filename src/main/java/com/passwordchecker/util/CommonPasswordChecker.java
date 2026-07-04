package com.passwordchecker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility component that loads a dictionary of commonly used passwords
 * and checks whether a given password appears in the list.
 *
 * <p>The dictionary is loaded once from {@code classpath:common-passwords.txt}
 * and stored in an unmodifiable {@link Set} for O(1) lookups.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class CommonPasswordChecker {

    private static final Logger logger = LoggerFactory.getLogger(CommonPasswordChecker.class);
    private static final String RESOURCE_PATH = "common-passwords.txt";

    private final Set<String> commonPasswords;

    /**
     * Constructs the checker and loads the common password dictionary.
     */
    public CommonPasswordChecker() {
        this.commonPasswords = loadCommonPasswords();
        logger.info("Loaded {} common passwords from dictionary", commonPasswords.size());
    }

    /**
     * Checks whether the given password is in the common password dictionary.
     *
     * @param password the password to check (case-insensitive)
     * @return {@code true} if the password is commonly used
     */
    public boolean isCommonPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return commonPasswords.contains(password.toLowerCase().trim());
    }

    /**
     * Returns the number of passwords in the loaded dictionary.
     *
     * @return the dictionary size
     */
    public int getDictionarySize() {
        return commonPasswords.size();
    }

    /**
     * Loads common passwords from the classpath resource file.
     *
     * @return an unmodifiable set of common passwords (lowercased)
     */
    private Set<String> loadCommonPasswords() {
        Set<String> passwords = new HashSet<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                logger.warn("Common passwords file not found at classpath:{}", RESOURCE_PATH);
                return getDefaultCommonPasswords();
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim().toLowerCase();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                        passwords.add(trimmed);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error loading common passwords file: {}", e.getMessage());
            return getDefaultCommonPasswords();
        }

        return Collections.unmodifiableSet(passwords);
    }

    /**
     * Returns a built-in fallback set of the most common passwords.
     *
     * @return a set of well-known weak passwords
     */
    private Set<String> getDefaultCommonPasswords() {
        Set<String> defaults = new HashSet<>();
        defaults.add("password");
        defaults.add("123456");
        defaults.add("12345678");
        defaults.add("qwerty");
        defaults.add("abc123");
        defaults.add("monkey");
        defaults.add("1234567");
        defaults.add("letmein");
        defaults.add("trustno1");
        defaults.add("dragon");
        defaults.add("baseball");
        defaults.add("iloveyou");
        defaults.add("master");
        defaults.add("sunshine");
        defaults.add("ashley");
        defaults.add("michael");
        defaults.add("password1");
        defaults.add("shadow");
        defaults.add("123123");
        defaults.add("654321");
        defaults.add("superman");
        defaults.add("qazwsx");
        defaults.add("football");
        defaults.add("password123");
        defaults.add("admin");
        defaults.add("welcome");
        defaults.add("hello");
        defaults.add("charlie");
        defaults.add("donald");
        defaults.add("login");
        defaults.add("starwars");
        defaults.add("passw0rd");
        defaults.add("master123");
        defaults.add("qwerty123");
        defaults.add("pass123");
        defaults.add("welcome1");
        defaults.add("123456789");
        defaults.add("1234567890");
        defaults.add("000000");
        defaults.add("111111");
        logger.info("Using default common passwords list ({} entries)", defaults.size());
        return Collections.unmodifiableSet(defaults);
    }
}
