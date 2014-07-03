package egovframework.rte.improved.security.userdetails.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import egovframework.rte.improved.security.userdetails.EgovUserDetails;

public abstract class EgovUsersByUsernameMapping extends MappingSqlQuery<EgovUserDetails> {

    /**
     * 사용자정보를 테이블에서 조회하여 사용자객체에 매핑한다.
     * @param ds
     * @param usersByUsernameQuery
     */
    public EgovUsersByUsernameMapping(DataSource ds, String usersByUsernameQuery) {
        super(ds, usersByUsernameQuery);
        declareParameter(new SqlParameter(Types.VARCHAR));
        compile();
    }

    @Override
    protected abstract EgovUserDetails mapRow(ResultSet rs, int rownum) throws SQLException;
}