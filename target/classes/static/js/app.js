/**
 * Password Strength & Policy Validator — Client-Side Enhancements
 *
 * Handles:
 *  - Password visibility toggle
 *  - Live character counter
 *  - Score circle animation
 *  - Form reset behaviour
 */
document.addEventListener('DOMContentLoaded', function () {

    // ==================== PASSWORD TOGGLE ====================
    const toggleBtn = document.getElementById('toggle-password');
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggle-icon');

    if (toggleBtn && passwordInput && toggleIcon) {
        toggleBtn.addEventListener('click', function () {
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';
            toggleIcon.classList.toggle('bi-eye', !isPassword);
            toggleIcon.classList.toggle('bi-eye-slash', isPassword);
        });
    }

    // ==================== LIVE CHARACTER COUNTER ====================
    const lengthDisplay = document.getElementById('password-length');

    if (passwordInput && lengthDisplay) {
        passwordInput.addEventListener('input', function () {
            lengthDisplay.textContent = passwordInput.value.length;
        });

        // Initialize on page load (for pre-filled values)
        lengthDisplay.textContent = passwordInput.value.length;
    }

    // ==================== SCORE CIRCLE ANIMATION ====================
    const scoreCircle = document.getElementById('score-circle');

    if (scoreCircle) {
        const scoreValueEl = scoreCircle.querySelector('.score-value');
        if (scoreValueEl) {
            const score = parseInt(scoreValueEl.textContent, 10) || 0;

            // Set CSS custom property for conic gradient
            scoreCircle.style.setProperty('--score-pct', score);

            // Determine color based on score
            let color;
            if (score >= 76) {
                color = '#00e676';
            } else if (score >= 51) {
                color = '#40c4ff';
            } else if (score >= 26) {
                color = '#ffab40';
            } else {
                color = '#ff5252';
            }

            scoreCircle.style.background =
                'conic-gradient(' + color + ' ' + (score * 3.6) + 'deg, rgba(255,255,255,0.08) 0deg)';
        }
    }

    // ==================== FORM RESET ====================
    const resetBtn = document.getElementById('reset-btn');
    const resultsSection = document.getElementById('results-section');

    if (resetBtn) {
        resetBtn.addEventListener('click', function () {
            // Reset length counter
            if (lengthDisplay) {
                lengthDisplay.textContent = '0';
            }

            // Reset password field to password type
            if (passwordInput) {
                passwordInput.type = 'password';
            }
            if (toggleIcon) {
                toggleIcon.classList.add('bi-eye');
                toggleIcon.classList.remove('bi-eye-slash');
            }

            // Hide results section if present
            if (resultsSection) {
                resultsSection.style.display = 'none';
            }
        });
    }

    // ==================== SMOOTH SCROLL TO RESULTS ====================
    if (resultsSection) {
        setTimeout(function () {
            resultsSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 300);
    }
});
