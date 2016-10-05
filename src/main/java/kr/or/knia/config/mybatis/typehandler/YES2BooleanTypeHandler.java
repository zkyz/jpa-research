package kr.or.knia.config.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class YES2BooleanTypeHandler extends BaseTypeHandler<Boolean> {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter ? "Y" : "N");
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if(value == null) return false;
		else {
			return value.matches("Y|y|T|t");
		}
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String value = rs.getString(columnIndex);
		if(value == null) return false;
		else {
			return value.matches("Y|y|T|t");
		}
	}

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String value = cs.getString(columnIndex);
		if(value == null) return false;
		else {
			return value.matches("Y|y|T|t");
		}
	}
}