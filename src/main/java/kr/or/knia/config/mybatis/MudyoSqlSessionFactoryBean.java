package kr.or.knia.config.mybatis;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MudyoSqlSessionFactoryBean extends SqlSessionFactoryBean implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MudyoSqlSessionFactoryBean.class);
	private static final String SYSQL_SQL = "SELECT * FROM MUDYO_SYSQL ORDER BY APP_ID, CREATED";

	private final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
	private final ReadLock readLocker = locker.readLock();
	private final WriteLock writeLocker = locker.writeLock();

	private DataSource dataSource;
	private String basePackage;
	private List<Interceptor> plugins = new ArrayList<Interceptor>(2);
	private List<TypeHandler<?>> typeHandlers = new ArrayList<TypeHandler<?>>(2);

	private SqlSessionFactory reloadableSqlSessionFactory;
	private DocumentBuilderFactory documentBuilderFactory = null;

	public static boolean reload = true;

	public MudyoSqlSessionFactoryBean() throws ParserConfigurationException {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		super.setDataSource(dataSource);
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setPlugins(Interceptor[] plugins) {
		Collections.addAll(this.plugins, plugins);
	}

	public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
		Collections.addAll(this.typeHandlers, typeHandlers);
	}

	public void afterPropertiesSet() throws Exception {
		try {
			reloading();

			new Thread(new Runnable() {
				public void run() {
					//noinspection InfiniteLoopStatement
					while (true) {
						try {
							reloading();
							Thread.sleep(5000);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reloading() throws Exception {
		if (reload) {
			reload = false;

			try {
				writeLocker.lock();

				if (logger.isDebugEnabled()) {
					logger.debug("Attempt load SQL Mapper!");
				}

				findMappers();
				super.afterPropertiesSet();

				reloadableSqlSessionFactory = (SqlSessionFactory) Proxy.newProxyInstance(
						SqlSessionFactory.class.getClassLoader(),
						new Class[]{SqlSessionFactory.class},
						new InvocationHandler() {
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
								SqlSessionFactory sqlSessionFactory = getParentObject();
//								Configuration config = sqlSessionFactory.getConfiguration();
//								config.setJdbcTypeForNull(JdbcType.VARCHAR);
//								config.setMapUnderscoreToCamelCase(true);

								return method.invoke(sqlSessionFactory, args);
							}
						}
				);
			} finally {
				writeLocker.unlock();
			}
		}
	}

	public SqlSessionFactory getObject() throws Exception {
		if (reloadableSqlSessionFactory == null) {
			reloading();
		}

		return reloadableSqlSessionFactory;
	}

	private SqlSessionFactory getParentObject() throws Exception {
		readLocker.lock();

		try {
			return super.getObject();
		} finally {
			readLocker.unlock();
		}
	}

	private void findMappers() throws Exception {
		Set<Resource> mappers = new HashSet<Resource>();
		PathMatchingResourcePatternResolver resourceFinder = new PathMatchingResourcePatternResolver();
		Resource[] resources = resourceFinder.getResources("classpath:" + basePackage.replaceAll("\\.", "/") + "/**/*.xml");

		for (Resource mapper : resources) {
			mapper = parseMapper(mapper, mapper.getFilename());
			mappers.add(mapper);
		}

		mapperLoadFromDatabase(mappers);

		super.setMapperLocations(mappers.toArray(new Resource[mappers.size()]));
	}

	private void mapperLoadFromDatabase(Set<Resource> mappers) throws Exception {
		String group = null;
		Document mapperDocument = null;

		for (SYSQLMapper sysql : getSYSQL()) {
			if (!sysql.getGroup().equals(group)) {
				if (mapperDocument != null) {
					mappers.add(generateMapperResource(mapperDocument, group));
				}

				group = sysql.getGroup();
				mapperDocument = createMapperXML(group);
			}

			addQueryStatement(mapperDocument, sysql);
		}

		if (mapperDocument != null) {
			mappers.add(generateMapperResource(mapperDocument, group));
		}
	}

	private Resource generateMapperResource(Document document, String group) throws Exception {
		StringWriter sw = new StringWriter();
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMImplementation domImpl = document.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("mapper",
				"-//mybatis.org//DTD Mapper 3.0//EN",
				"http://mybatis.org/dtd/mybatis-3-mapper.dtd");
		trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
		trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
		trans.transform(new DOMSource(document), new StreamResult(sw));

		Resource mapper = new InputStreamResource(new ByteArrayInputStream(sw.toString().getBytes()));
		return parseMapper(mapper, group);
	}

	private Document createMapperXML(String group) {
		try {
			Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
			Element mapper = document.createElement("mapper");
			mapper.setAttribute("namespace", group);
			document.appendChild(mapper);
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void addQueryStatement(Document doc, SYSQLMapper sysql) throws IOException, SAXException, ParserConfigurationException {
		Node mapper = doc.getFirstChild();

		String sqlmapperTagName = sysql.getType().name().toLowerCase();
		StringBuilder sqlmapper = new StringBuilder();
		sqlmapper.append("<").append(sqlmapperTagName);
		sqlmapper.append(" id=\"").append(sysql.getId()).append("\"");

		if (sysql.getResultType() != null) {
			sqlmapper.append(" resultType=\"").append(sysql.getResultType()).append("\"");
		}
		sqlmapper.append(">");
		sqlmapper.append(sysql.getQuery());
		sqlmapper.append("</").append(sqlmapperTagName).append(">");

		Document mapperQueryDoc = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(sqlmapper.toString())));
		Node mapperQueryNode = doc.importNode(mapperQueryDoc.getFirstChild(), true);
		mapper.appendChild(mapperQueryNode);
	}

	private Resource parseMapper(Resource mapper, String description) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(mapper.getInputStream()));

		StringBuilder sw = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sw.append(line).append("\n");
		}
		br.close();

		String sql = sw.toString()
				.replaceAll("<!\\-\\-.*\\-\\->", "") //XML 주석제거 <!-- ... -->
				.replaceAll("\\-\\-.*\\n", "\\\n") // SQL 인라인주석제거 -- ...
				.replaceAll("^\\s+|\\s+$", ""); //빈줄 제거

		StringBuilder parsedSql = new StringBuilder();
		boolean parse = true;
		boolean first = true;
		for (String part : sql.split("'")) {

			if (first) first = false;
			else parsedSql.append('\'');

			if (parse) {
				part = part
						.replaceAll(":([a-zA-Z_][a-zA-Z0-9_\\.]*[a-zA-Z0-9]?)((\\[([0-9]+)?\\])?)", "#{$1$2}"); //Binding 변수
			}

			parsedSql.append(part);
			parse = !parse;
		}

		return new InputStreamResource(new ByteArrayInputStream(parsedSql.toString().getBytes()), description);
	}

	private List<SYSQLMapper> getSYSQL() {
		JdbcTemplate tpl = new JdbcTemplate(this.dataSource);
		tpl.setQueryTimeout(5);

		List<SYSQLMapper> list = null;

		try {
			list = tpl.query(SYSQL_SQL, new RowMapper<SYSQLMapper>() {
				public SYSQLMapper mapRow(ResultSet rs, int rowNum) throws SQLException {
					SYSQLMapper sysql = new SYSQLMapper();
					sysql.setGroup(rs.getString("GRP"));
					sysql.setId(rs.getString("ID"));
					sysql.setType(SYSQLMapper.Type.valueOf(rs.getString("TYPE")));
					sysql.setResultType(rs.getString("RESULT_TYPE"));

					Clob query = rs.getClob("QUERY");
					StringWriter sw = new StringWriter();

					Reader queryReader = query.getCharacterStream();
					int i;
					char[] c = new char[512];
					try {
						while ((i = queryReader.read(c)) > -1) {
							sw.write(c, 0, i);
						}
						sw.close();
						sysql.setQuery(sw.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}

					return sysql;
				}
			});
		} catch (Exception e) {
			// TODO ...
			//e.printStackTrace();
			return Collections.emptyList();
		}

		return list;
	}
}
