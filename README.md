# 🛡️ Password Strength & Policy Validator

A **production-ready** Spring Boot application that validates passwords against 12 comprehensive security rules, calculates a strength score, and generates actionable improvement suggestions — all wrapped in a stunning glassmorphism UI.

---

## 📸 Features

### ✅ Basic Validation
| Rule | Description |
|------|-------------|
| Minimum Length | Password must be at least 8 characters |
| Maximum Length | Password must not exceed 128 characters |
| Uppercase Letter | At least one uppercase letter (A-Z) |
| Lowercase Letter | At least one lowercase letter (a-z) |
| Digit | At least one digit (0-9) |
| Special Character | At least one special character (!@#$%^&*...) |

### 🔒 Advanced Validation
| Rule | Description |
|------|-------------|
| Username Detection | Password must not contain the username |
| Common Password | Checked against 120+ commonly used passwords |
| Sequential Characters | No runs like abc, 123, xyz |
| Repeated Characters | No runs like aaa, 111 |
| Consecutive Characters | No patterns like aabbcc |
| Whitespace Detection | No spaces or whitespace characters |

### 📊 Password Scoring & Strength
- **Score**: 0-100 based on rule compliance, length, variety, and entropy
- **Strength Levels**: Weak · Medium · Strong · Very Strong
- **Visual Meter**: Animated progress bar and score circle

### 💡 Smart Suggestions
- Actionable emoji-tagged suggestions for every failed rule
- Bonus tips for overall password improvement

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Build Tool | Maven |
| Architecture | MVC (Model-View-Controller) |
| Template Engine | Thymeleaf |
| Frontend | HTML5, CSS3, Bootstrap 5 |
| Testing | JUnit 5 |
| Design Principles | SOLID, OOP |
| DI Strategy | Constructor Injection only |

---

## 📁 Project Structure

```
Password Checker/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/passwordchecker/
    │   │   ├── PasswordCheckerApplication.java          # @SpringBootApplication
    │   │   ├── config/
    │   │   │   └── AppConfig.java                       # @Configuration (Bean definitions)
    │   │   ├── controller/
    │   │   │   └── PasswordController.java              # @Controller (MVC endpoints)
    │   │   ├── dto/
    │   │   │   ├── PasswordRequest.java                 # Form input DTO
    │   │   │   └── PasswordReport.java                  # Validation report DTO
    │   │   ├── exception/
    │   │   │   ├── PasswordValidationException.java     # Custom exception
    │   │   │   └── GlobalExceptionHandler.java          # @ControllerAdvice
    │   │   ├── model/
    │   │   │   ├── PasswordStrength.java                # Strength enum
    │   │   │   └── ValidationRule.java                  # Rule result model
    │   │   ├── service/
    │   │   │   ├── PasswordService.java                 # Service interface
    │   │   │   └── impl/
    │   │   │       └── PasswordServiceImpl.java         # @Service implementation
    │   │   └── util/
    │   │       ├── CommonPasswordChecker.java           # Dictionary checker
    │   │       ├── PasswordScorer.java                  # Scoring engine
    │   │       └── PasswordValidator.java               # Validation engine
    │   └── resources/
    │       ├── application.properties                   # Configuration
    │       ├── common-passwords.txt                     # Password dictionary
    │       ├── static/
    │       │   ├── css/
    │       │   │   └── style.css                        # Custom CSS
    │       │   └── js/
    │       │       └── app.js                           # Client-side JS
    │       └── templates/
    │           ├── index.html                           # Main page
    │           └── error.html                           # Error page
    └── test/
        └── java/com/passwordchecker/
            ├── PasswordCheckerApplicationTests.java     # Context load test
            └── service/
                └── PasswordServiceImplTest.java         # 30+ JUnit 5 tests
```

---

## 🚀 How to Run

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**

### Steps

1. **Clone / Import** the project into Spring Tool Suite (STS) or IntelliJ IDEA.

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Open your browser:**
   ```
   http://localhost:8080
   ```

### Run Tests
```bash
mvn test
```

---

## 🔄 Application Flow

```
User enters Username + Password
        ↓
PasswordController (@Controller)
        ↓
PasswordService (Interface)
        ↓
PasswordServiceImpl (@Service)
        ↓
PasswordValidator → 12 validation rules
PasswordScorer   → Score (0-100)
PasswordStrength → Weak/Medium/Strong/Very Strong
        ↓
PasswordReport (DTO)
        ↓
PasswordController → Model
        ↓
Thymeleaf Template (index.html)
        ↓
User sees results
```

---

## 🏗️ Design Principles

| Principle | Implementation |
|-----------|---------------|
| **Single Responsibility** | Each class has one clear purpose |
| **Open/Closed** | New rules can be added without modifying existing code |
| **Liskov Substitution** | PasswordServiceImpl is fully substitutable for PasswordService |
| **Interface Segregation** | Focused PasswordService interface |
| **Dependency Inversion** | Controller depends on PasswordService interface, not impl |
| **Constructor Injection** | All dependencies injected via constructors (no @Autowired fields) |
| **IoC Container** | Spring manages all bean lifecycles |

---

## 🔮 Future Enhancements

- [ ] REST API endpoint (`/api/validate`) for programmatic access
- [ ] Password breach check via Have I Been Pwned API
- [ ] Password generation with configurable rules
- [ ] Internationalisation (i18n) support
- [ ] Rate limiting to prevent brute-force abuse
- [ ] Database-backed password policy configuration
- [ ] Docker containerisation
- [ ] CI/CD pipeline with GitHub Actions

---

## 📝 License

This project is open source and available for hackathon and educational purposes.

---

> Built with ❤️ using **Spring Boot 3**, **Thymeleaf**, and **Bootstrap 5**
"# password-checker" 
