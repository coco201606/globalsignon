package sample.gso;

import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import static sample.gso.LoginBean.COOKIE_GLOBAL_SESSION_ID;

/**
 *
 * Indexページ用バッキングビーン
 */
@Named
@RequestScoped
public class RedirectBean {

    @Inject
    private SecurityContext securityContext;
    @Inject
    private ExternalContext externalContext;

    private String redirectTo;

    public String doRedirect() {
        // redirect
        String redirectString = redirectTo + "?faces-redirect=true";
        System.out.println("RedirectBean.doRedirect: Redirect to " + redirectString);
        return redirectString;
    }

    public void setCookie() {
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        // グローバルセッションIDの作成（サンプルコード要簡略化してます）
        String gsid = securityContext.getCallerPrincipal().getName() + UUID.randomUUID().toString();
        System.out.println("RedirectBean.doRedirect: Global Session ID = " + gsid);
        // 本来はここでGlobalSessionを作成してGloba Session DBへ保存
        // CookieにGlobal Session IDをセット
        Cookie gsidCookie = new Cookie(COOKIE_GLOBAL_SESSION_ID, gsid);
        gsidCookie.setMaxAge(60 * 60 * 24);
        gsidCookie.setHttpOnly(true);
        gsidCookie.setPath("/");
        //gsidCookie.setDomain("kiho.my.co.jp");
        response.addCookie(gsidCookie);
    }

    /**
     * @return the securityContext
     */
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @return the redirectTo
     */
    public String getRedirectTo() {
        return redirectTo;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    /**
     * @param redirectTo the redirectTo to set
     */
    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

}
