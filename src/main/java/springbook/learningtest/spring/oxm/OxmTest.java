package springbook.learningtest.spring.oxm;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.sqlservice.jaxb.SqlType;
import springbook.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/OxmTest-context.xml")
public class OxmTest {

    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    public void unmarshallerSqlMap() throws XmlMappingException, IOException {
        Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();
        assertThat(sqlList.get(0).getKey(), CoreMatchers.is("userAdd"));
        assertThat(sqlList.get(1).getKey(), CoreMatchers.is("userGet"));
        assertThat(sqlList.get(2).getKey(), CoreMatchers.is("userGetAll"));
    }


}

