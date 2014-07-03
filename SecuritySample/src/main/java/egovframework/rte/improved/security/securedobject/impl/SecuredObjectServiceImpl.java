package egovframework.rte.improved.security.securedobject.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import egovframework.rte.improved.security.securedobject.EgovSecuredObjectService;

public class SecuredObjectServiceImpl implements EgovSecuredObjectService {

	private SecuredObjectDAO securedObjectDAO;

	public void setSecuredObjectDAO(SecuredObjectDAO securedObjectDAO) {
		this.securedObjectDAO = securedObjectDAO;
	}

	public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception {
		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> ret = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();
		
		LinkedHashMap<Object, List<ConfigAttribute>> data = securedObjectDAO.getRolesAndUrl();
		
		Set<Object> keys = data.keySet();
		
		for (Object key : keys) {
			ret.put((AntPathRequestMatcher)key, data.get(key));
		}
		
		return ret;
		
	}

	public LinkedHashMap<String, List<ConfigAttribute>> getRolesAndMethod() throws Exception {
		LinkedHashMap<String, List<ConfigAttribute>> ret = new LinkedHashMap<String, List<ConfigAttribute>>();
		
		LinkedHashMap<Object, List<ConfigAttribute>> data = securedObjectDAO.getRolesAndMethod();
		
		Set<Object> keys = data.keySet();
		
		for (Object key : keys) {
			ret.put((String)key, data.get(key));
		}
		
		return ret;
	}

	public LinkedHashMap<String, List<ConfigAttribute>> getRolesAndPointcut() throws Exception {
		LinkedHashMap<String, List<ConfigAttribute>> ret = new LinkedHashMap<String, List<ConfigAttribute>>();
		
		LinkedHashMap<Object, List<ConfigAttribute>> data = securedObjectDAO.getRolesAndPointcut();
		
		Set<Object> keys = data.keySet();
		
		for (Object key : keys) {
			ret.put((String)key, data.get(key));
		}
		
		return ret;
	}

	public List<ConfigAttribute> getMatchedRequestMapping(String url) throws Exception {
		return securedObjectDAO.getRegexMatchedRequestMapping(url);
	}

	public String getHierarchicalRoles() throws Exception {
		return securedObjectDAO.getHierarchicalRoles();
	}
}
