package kr.or.knia.config.mybatis.typehandler;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class FileTypeHandler extends BaseTypeHandler<File> {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, File parameter, JdbcType jdbcType) throws SQLException {
		if (parameter != null && parameter.getAbsolutePath() != null) {
			String path = parameter.getAbsolutePath();
			path = path.replaceAll("\\\\", "/");
	
			ps.setString(i, path);
		}
	}

	@Override
	public File getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String filePath = rs.getString(columnName);
		if(filePath == null) {
			return null;
		}

		File file = new File(filePath);
		return file == null || !file.exists() ? null : file;
	}

	@Override
	public File getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String filePath = rs.getString(columnIndex);
		if(filePath == null) {
			return null;
		}

		File file = new File(filePath);
		return file == null || !file.exists() ? null : file;
	}

	@Override
	public File getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String filePath = cs.getString(columnIndex);
		if(filePath == null) {
			return null;
		}

		File file = new File(filePath);
		return file == null || !file.exists() ? null : file;
	}
}
