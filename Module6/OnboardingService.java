import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class OnboardingService {
    private static final Logger LOG = Logger.getLogger(OnboardingService.class.getName());
    private final DataSource ds;

    public OnboardingService(DataSource ds) {
        this.ds = ds;
    }

    // Noncompliant: password as String; logs full SSN; dynamic SQL; noisy
    // exceptions
    public void createApplicant(String fullName, String ssn, String password) {
        LOG.info(() -> "Creating applicant for SSN=" + ssn); // ❌ full PII in logs

        String sql = "INSERT INTO applicants(full_name, ssn, pwd_hash) VALUES ('"
                + fullName + "','" + ssn + "','" + hash(password) + "')"; // ❌ dynamic SQL; ❌ String password

        try (Connection c = ds.getConnection();
                Statement st = c.createStatement()) {
            st.executeUpdate(sql);
            LOG.info("Applicant created");
        } catch (Exception e) {
            // ❌ leaks internal details; often includes file paths, usernames, etc.
            LOG.severe("Create failed: " + e);
            e.printStackTrace(); // ❌ stack trace in logs
            throw new RuntimeException("Create failed: " + e.getMessage(), e); // ❌ propagates sensitive details
        }
    }

    private String hash(String password) {
        // toy; pretend hashing
        return Integer.toHexString(password.hashCode());
    }
}
