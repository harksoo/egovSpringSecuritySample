package egovframework.rte.improved.security.userdetails.hierarchicalroles;

import org.springframework.beans.factory.FactoryBean;

import egovframework.rte.improved.security.securedobject.EgovSecuredObjectService;

public class HierarchyStringsFactoryBean implements FactoryBean<String> {

    private EgovSecuredObjectService securedObjectService;

    public void setSecuredObjectService(EgovSecuredObjectService securedObjectService) {
        this.securedObjectService = securedObjectService;
    }

    private String hierarchyStrings;

    public void init() throws Exception {
        hierarchyStrings = (String) securedObjectService.getHierarchicalRoles();
    }

    public String getObject() throws Exception {
        if (hierarchyStrings == null) {
            init();
        }
        return hierarchyStrings;
    }

    public Class<String> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

}