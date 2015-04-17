package puerta.system.tests;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import puerta.system.dao.InsiderDao;
import puerta.system.dao.impl.InsiderDaoImpl;
import puerta.system.po.Insider;

public class SpringTest {

	public static void main(String args[]) {
		String[] fns = new String[] {
				"file:D:\\work\\java\\puerta\\WebRoot\\WEB-INF\\root-context.xml",
				"file:D:\\work\\java\\puerta\\WebRoot\\WEB-INF\\servlet-context.xml" };

		ApplicationContext fs = new FileSystemXmlApplicationContext(fns);

		InsiderDao insiderDao = (InsiderDao) fs.getBean(InsiderDaoImpl.class);

		insiderDao.save(new Insider());
	}
}
