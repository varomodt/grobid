package org.grobid.core.utilities;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.InputSource;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Created by zholudev on 15/04/15.
 * Running XQuery queries
 */
public class XQueryProcessor {
    private final StaticQueryContext sqc;
    private final DynamicQueryContext dqc;

    public XQueryProcessor(String xmlContent) throws XPathException {
        Configuration c = new Configuration();

        sqc = new StaticQueryContext(c);
        dqc = new DynamicQueryContext(c);

        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
        InputSource XMLSource = new InputSource(is);

        SAXSource SAXs = new SAXSource(XMLSource);
        DocumentInfo DI = sqc.buildDocument(SAXs);
        dqc.setContextItem(DI);
    }


    public SequenceIterator getSequenceIterator(String query) throws XPathException {
        Properties props = new Properties();

        final XQueryExpression exp = sqc.compileQuery(query);
        StringWriter stringWriter = new StringWriter();
        exp.run(dqc, new StreamResult(stringWriter), props);
        return exp.iterator(dqc);
    }

}