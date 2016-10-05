package kr.or.knia.config.mybatis.typehandler;

import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class Clob2StringTypeHandler extends BaseTypeHandler<String> {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return clob2String(rs.getClob(columnName));
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return clob2String(rs.getClob(columnIndex));
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return clob2String(cs.getClob(columnIndex));
	}

	private String clob2String(Clob value) throws SQLException {
		StringBuilder result = new StringBuilder("");
		
		char[] c = new char[512];
		int i = -1;

		Reader r = value.getCharacterStream();
		try {
			while((i = r.read(c)) > -1) {
				result.append(c, 0, i);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}
}