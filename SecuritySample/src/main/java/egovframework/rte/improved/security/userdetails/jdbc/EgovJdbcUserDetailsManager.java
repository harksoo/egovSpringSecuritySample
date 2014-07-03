package egovframework.rte.improved.security.userdetails.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import egovframework.rte.fdl.string.EgovObjectUtil;
import egovframework.rte.improved.security.userdetails.EgovUserDetails;

public class EgovJdbcUserDetailsManager extends JdbcUserDetailsManager {
	private EgovUserDetails userDetails = null;
    private EgovUsersByUsernameMapping usersByUsernameMapping;

    private String mapClass;
    private RoleHierarchy roleHierarchy = null;

    public void setMapClass(String mapClass) {
        this.mapClass = mapClass;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
	protected void initDao() throws ApplicationContextException {
		super.initDao();

		try {
			initMappingSqlQueries();
		} catch (Exception e) {
			logger.error("EgovJdbcUserDetailsManager.initDao.Exception : " + e.toString(), e);
		}

    }

    /**
     * jdbc-user-service의 usersByUsernameQuery 사용자조회 쿼리와 
     * authoritiesByUsernameQuery 권한조회 쿼리를 이용하여 정보를 저장한다.
     */
    private void initMappingSqlQueries() throws InvocationTargetException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, ClassNotFoundException, Exception {

        logger.debug("## EgovJdbcUserDetailsManager query : " + getUsersByUsernameQuery());
        
        Class<?> clazz = EgovObjectUtil.loadClass(this.mapClass);
        Constructor<?> constructor = clazz.getConstructor(new Class[] {DataSource.class, String.class });
        Object[] params = new Object[] { getDataSource(), getUsersByUsernameQuery() };

        this.usersByUsernameMapping = (EgovUsersByUsernameMapping) constructor.newInstance(params);
        logger.info(this.usersByUsernameMapping);
    }

    /**
     * JdbcDaoImpl 클래스의 loadUsersByUsername 메소드 재정의.
     * 사용자명(또는 ID)로 UserDetails 정보를 조회하여 리스트 형식으로 저장한다.
     */
    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        List<EgovUserDetails> list = usersByUsernameMapping.execute(username);
        
        ArrayList<UserDetails> newList = new ArrayList<UserDetails>();
        
        for (EgovUserDetails user : list) {
        	newList.add(user);
        }
        
        return newList;
    }

    /**
     * JdbcDaoImpl 클래스의 loadUsersByUsername 메소드 재정의.
     * 사용자명(또는 ID)로 EgovUserDetails의 정보를 조회한다.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    @Override
    public EgovUserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {

        List<UserDetails> users = loadUsersByUsername(username);

        if (users.size() == 0) {
            logger.debug("Query returned no results for user '" + username + "'");

            throw new UsernameNotFoundException(
            		messages.getMessage("EgovJdbcUserDetailsManager.notFound", new Object[]{username}, "Username {0} not found"));
        }

        UserDetails obj = users.get(0);
        this.userDetails = (EgovUserDetails) obj;

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

        // if (enableAuthorities) {
        dbAuthsSet.addAll(loadUserAuthorities(this.userDetails.getUsername()));
        // }

        List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

        addCustomAuthorities(this.userDetails.getUsername(), dbAuths);

        if (dbAuths.size() == 0) {
            throw new UsernameNotFoundException(messages.getMessage(
                "EgovJdbcUserDetailsManager.noAuthority",
                new Object[] {username }, "User {0} has no GrantedAuthority"));
        }

        // RoleHierarchyImpl 에서 저장한 Role Hierarchy 정보가 저장된다.
        Collection<? extends GrantedAuthority> authorities =
            roleHierarchy.getReachableGrantedAuthorities(dbAuths);

        // JdbcDaoImpl 클래스의 createUserDetails 메소드 재정의
        return new EgovUserDetails(this.userDetails.getUsername(),
            this.userDetails.getPassword(), this.userDetails.isEnabled(), true,
            true, true, authorities, this.userDetails.getEgovUserVO());
    }

    /**
     * 인증된 사용자 이름으로 사용자정보(EgovUserDetails)를 가져온다.
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    public EgovUserDetails getAuthenticatedUser()
            throws UsernameNotFoundException, DataAccessException {

        return loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
