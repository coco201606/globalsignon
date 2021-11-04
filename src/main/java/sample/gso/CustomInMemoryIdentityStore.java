package sample.gso;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.EnumSet;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * サンプルコード用カスタムアイデンティティストア
 */
@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String[] ADMIN_ROLES = {"ADMIN", "USER"};
    private static final String[] USER_ROLES = {"USER"};

    @Override
    public CredentialValidationResult validate(Credential credential) {

        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;

        if (login.getCaller().equals(ADMIN_USERNAME)
                && login.getPasswordAsString().equals(ADMIN_PASSWORD)) {
            System.out.println("CustomInMemoryIdentityStore: validated as " + ADMIN_USERNAME);
            return new CredentialValidationResult(ADMIN_USERNAME, new HashSet<>(Arrays.asList(ADMIN_ROLES)));
        } else if (login.getCaller().equals(USER_USERNAME)
                && login.getPasswordAsString().equals(USER_PASSWORD)) {
            System.out.println("CustomInMemoryIdentityStore: validated as " + USER_USERNAME);
            return new CredentialValidationResult(USER_USERNAME, new HashSet<>(Arrays.asList(USER_ROLES)));
        } else {
            System.out.println("CustomInMemoryIdentityStore: invalid");
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    @Override
    public int priority() {
        return 0;
    }
    
    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS);
    }
}
