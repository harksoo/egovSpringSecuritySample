package egovframework.rte.cmmn.security.listener;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;

// user session이 종료 시 호출이 된다.
// session 만료, 로그아웃 처리 시...
public class SessionDestoryListener implements ApplicationListener<SessionDestroyedEvent> {

	@Override
	public void onApplicationEvent(SessionDestroyedEvent arg0) {
		// TODO Auto-generated method stub
		List<SecurityContext> contexts = arg0.getSecurityContexts();
		if (contexts.isEmpty() == false) {
			for (SecurityContext ctx : contexts) {
				
				Object obj = ctx.getAuthentication().getPrincipal();
				
				System.out.println(obj);
				
				//LoginToken loginToken = (LoginToken) ctx.getAuthentication().getPrincipal();
				// login history
				//LoginHistory loginHistory = loginToken.getHistory();
				//loginHistory.setLogoutDate(new Date());
				// update
				//loginHistoryService.modify(loginHistory);
			}
		}
		
	}
	
	

}
