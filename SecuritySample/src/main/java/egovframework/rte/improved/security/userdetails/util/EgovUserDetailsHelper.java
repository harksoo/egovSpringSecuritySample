package egovframework.rte.improved.security.userdetails.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import egovframework.rte.fdl.string.EgovObjectUtil;
import egovframework.rte.improved.security.userdetails.EgovUserDetails;

public class EgovUserDetailsHelper {
	private static Log log = LogFactory.getLog(EgovUserDetailsHelper.class);

    /**
     * 인증된 사용자객체를 VO형식으로 가져온다.
     * @return 사용자 ValueObject
     */
    public static Object getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (EgovObjectUtil.isNull(authentication)) {
            log.debug("## authentication object is null!!");
            return null;
        }

        if (authentication.getPrincipal() instanceof EgovUserDetails) {
        	EgovUserDetails details = (EgovUserDetails) authentication.getPrincipal();
        	

	        log.debug("## EgovUserDetailsHelper.getAuthenticatedUser : AuthenticatedUser is " + details.getUsername());
	        
	        return details.getEgovUserVO();
        } else {
        	return authentication.getPrincipal();
        }
    }

    /**
     * 인증된 사용자의 권한 정보를 가져온다. 
     * 예) [ROLE_ADMIN, ROLE_USER, ROLE_A, ROLE_B, ROLE_RESTRICTED,
     * IS_AUTHENTICATED_FULLY,
     * IS_AUTHENTICATED_REMEMBERED,
     * IS_AUTHENTICATED_ANONYMOUSLY]
     * @return 사용자 권한정보 목록
     */
    public static List<String> getAuthorities() {
        List<String> listAuth = new ArrayList<String>();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (EgovObjectUtil.isNull(authentication)) {
            log.debug("## authentication object is null!!");
            return null;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        
        while(iter.hasNext()) {
        	GrantedAuthority auth = iter.next();
        	listAuth.add(auth.getAuthority());

            log.debug("## EgovUserDetailsHelper.getAuthorities : Authority is " + auth.getAuthority());

        }
        
        return listAuth;
    }

    /**
     * 인증된 사용자 여부를 체크한다.
     * @return 인증된 사용자 여부(TRUE / FALSE)
     */
    public static Boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (EgovObjectUtil.isNull(authentication)) {
            log.debug("## authentication object is null!!");
            return Boolean.FALSE;
        }

        String username = authentication.getName();
        if (username.equals("roleAnonymous")) {
            log.debug("## username is " + username);
            return Boolean.FALSE;
        }

        Object principal = authentication.getPrincipal();

        return (Boolean.valueOf(!EgovObjectUtil.isNull(principal)));
    }
    
    /**
     * 기본 algorithmd(SHA-256)에 대한 패스워드 얻기.
     * @param password
     * @return
     */
    public static String getHashedPassword(String password) {
    	ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		String hashed = encoder.encodePassword(password, null);
		
		return hashed;
    }
}
