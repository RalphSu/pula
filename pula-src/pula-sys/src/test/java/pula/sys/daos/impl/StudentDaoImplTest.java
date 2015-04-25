/**
 * 
 */
package pula.sys.daos.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import puerta.system.vo.MapBean;
import pula.sys.daos.StudentDao;

/**
 * @author Liangfei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/root-context.xml","classpath*:/servlet-context.xml"}) 
public class StudentDaoImplTest {

    @Resource
    StudentDao studentDao;
    
    @Test
    public void testLogin() {
        Assert.notNull(studentDao);
        MapBean mb = studentDao.meta4login("JQ00010", "123456");
        Assert.notNull(mb);
    }
}
