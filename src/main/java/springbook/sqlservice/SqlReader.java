package springbook.sqlservice;

import javax.xml.bind.JAXBException;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry) throws JAXBException;
}
