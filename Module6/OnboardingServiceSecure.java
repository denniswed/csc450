import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class OnboardingServiceSecure {
    private static final Logger LOG = Logger.getLogger(OnboardingService.class.getName());
    private final DataSource ds;

    public OnboardingServiceSecure(DataSource ds) {
        this.ds = ds;
    }

    // Compliant: mask PII; use char[]; parameterized SQL; sanitized error handling
    public void createApplicant(String fullName, String ssn, char[] password) {
        String masked = maskSsn(ssn); // e.g., ***-**-1234
        LOG.info(() -> "Creating applicant with SSN: " + masked); // ✅ omit PII entirely (or at most masked)
        // If you absolutely must correlate events, log a server-generated opaque
        // requestId instead.

        String sql = "INSERT INTO applicants(full_name, ssn, pwd_hash) VALUES (?, ?, ?)";

        try (Connection c = ds.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, ssn);
            ps.setString(3, hash(password)); // hashing should be strong/adaptive in real code
            ps.executeUpdate();

            LOG.info("Applicant created"); // ✅ no sensitive data
        } catch (SQLException ex) {
            // ✅ sanitize what’s logged; no stack trace or internals
            String errorCode = "ONB-1001";
            LOG.warning(() -> "CreateApplicant failed (" + errorCode + ")");
            // ✅ rethrow without leaking file paths/SQL details
            throw new RuntimeException("Operation failed (" + errorCode + ").");
        } finally {
            // ✅ purge sensitive data promptly
            Arrays.fill(password, '\0');
        }
    }

    private static String maskSsn(String ssn) {
        if (ssn == null || ssn.length() < 4)
            return "***";
        return "***-**-" + ssn.substring(ssn.length() - 4);
    }

    private static String hash(char[] password) {
        // Placeholder. In production, use a vetted KDF (e.g., PBKDF2, bcrypt, scrypt,
        // Argon2)
        // via a reputable library, and do not keep intermediate Strings.
        int h = 0;
        for (char c : password)
            h = 31 * h + c;
        return Integer.toHexString(h);
    }
}
