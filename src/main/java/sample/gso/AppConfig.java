package sample.gso;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;


@CustomFormAuthenticationMechanismDefinition( 
    loginToContinue = @LoginToContinue( 
        loginPage="/login.xhtml", 
        errorPage="" ,
        useForwardToLogin = false
    ) 
) 
// 今回はCuustomInMemoryIdentityStoreを使うのでコメントアウト
/*
@DatabaseIdentityStoreDefinition( 
        dataSourceLookup = "jdbc/sample", 
        callerQuery = "select password from javaee_security_sample_users where user_name = ?", 
        groupsQuery = "select group_name from javaee_security_sample_groups where user_name = ?" 
) 
*/
@FacesConfig
@ApplicationScoped
public class AppConfig {
}
