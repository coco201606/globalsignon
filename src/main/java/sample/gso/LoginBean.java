package sample.gso;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * ログイン処理用バッキングビーン
 */
@Named
@RequestScoped
public class LoginBean {

    public static final String COOKIE_GLOBAL_SESSION_ID = "GLOBAL_JSESSIONID";
    public static final String REDIRECT_TO = "REDIRECT_TO";

    private String userName;
    private String password;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    public void login() throws IOException {
        System.out.println("LoginController.login() called. user=" + getUserName() + ", password=" + getPassword());
        // 認証処理葉パラメータの準備
        HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse httpServletResponse = (HttpServletResponse) externalContext.getResponse();
        UsernamePasswordCredential uNamePasswordCredential = new UsernamePasswordCredential(getUserName(), getPassword());
        AuthenticationParameters authenticationParameters
                = AuthenticationParameters.withParams().credential(uNamePasswordCredential);
        // 認証処理の実行
        AuthenticationStatus authenticationStatus
                = securityContext.authenticate(httpServletRequest, httpServletResponse, authenticationParameters);
        System.out.println("LoginBean.login() AuthenticationStatus = " + authenticationStatus);
        String role;
        if (securityContext.isCallerInRole("USER")) {
            role = "USER";
        } else if (securityContext.isCallerInRole("ADMIN")) {
            role = "ADMIN";
        } else {
            role = "Not Matched";
        }
        System.out.println("SecurityContext" + securityContext.toString());
        System.out.println("Auth result = " + authenticationStatus + ", role = " + role);
        //System.out.println("UserPrincipal = " + securityContext.getCallerPrincipal().getName());
        // 認証後の処理
        switch (authenticationStatus) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
                System.out.println("Login failed.");
                break;
            case SUCCESS:

                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, securityContext.getCallerPrincipal().getName() + " login succeed as role " + role, null));
                System.out.println(securityContext.getCallerPrincipal().getName() + " Login as " + role);
                break;
            case NOT_DONE:
            default:
                System.err.println("LoginBean.login() unexpected AuthenticationStatus = " + authenticationStatus.name());
        }
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
