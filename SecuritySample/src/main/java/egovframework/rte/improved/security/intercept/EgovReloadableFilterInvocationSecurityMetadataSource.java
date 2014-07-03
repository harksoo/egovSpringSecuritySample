package egovframework.rte.improved.security.intercept;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.RequestMatcher;

import egovframework.rte.improved.security.securedobject.EgovSecuredObjectService;

public class EgovReloadableFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	protected final Log logger = LogFactory.getLog(getClass());

	private EgovSecuredObjectService securedObjectService;
	//private RequestMatcher requestMatcher;
	
	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    public EgovReloadableFilterInvocationSecurityMetadataSource(
            LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {

        this.requestMap = requestMap;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
    
	public void setSecuredObjectService(EgovSecuredObjectService securedObjectService) {
		this.securedObjectService = securedObjectService;
	}
	
	/*
	public void setRequestMatcher(RequestMatcher requestMatcher) {
		this.requestMatcher = requestMatcher;
	}
	*/
	
	public void reload() throws Exception {

		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = securedObjectService.getRolesAndUrl();

        Iterator<Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadedMap.entrySet().iterator();

        // 이전 데이터 삭제
        requestMap.clear();

        while (iterator.hasNext()) {
        	Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
            
            requestMap.put(entry.getKey(), entry.getValue());
        }
        
        if (logger.isInfoEnabled()) {
            logger.info("Secured Url Resources - Role Mappings reloaded at Runtime!");
        }
    }
}
