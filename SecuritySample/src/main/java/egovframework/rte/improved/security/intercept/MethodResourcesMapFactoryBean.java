package egovframework.rte.improved.security.intercept;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import egovframework.rte.improved.security.securedobject.EgovSecuredObjectService;

public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private String resourceType;

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    private EgovSecuredObjectService securedObjectService;

    public void setSecuredObjectService(EgovSecuredObjectService securedObjectService) {
        this.securedObjectService = securedObjectService;
    }

    private LinkedHashMap<String, List<ConfigAttribute>> resourcesMap;

    public void init() throws Exception {
        if ("method".equals(resourceType)) {
            resourcesMap = securedObjectService.getRolesAndMethod();
        } else if ("pointcut".equals(resourceType)) {
            resourcesMap = securedObjectService.getRolesAndPointcut();
        } else {
            throw new Exception("resourceType must be 'method' or 'pointcut'");
        }
    }

    public LinkedHashMap<String, List<ConfigAttribute>> getObject() throws Exception {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    @SuppressWarnings("rawtypes")
	public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
