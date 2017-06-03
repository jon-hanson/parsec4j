package org.funcj.codec;

import org.funcj.codec.xml.XmlCodecCore;
import org.junit.Assert;
import org.w3c.dom.Node;

import static java.lang.System.out;
import static org.funcj.codec.xml.XmlUtils.nodeToString;

public class XmlCodecTest extends TestBase {
    final static XmlCodecCore codec = new XmlCodecCore();

    @Override
    protected <T> void roundTrip(T val, Class<T> clazz) {
        codec.setNewDocument();
        final Node node = codec.encode(clazz, val, "test");
        out.println(nodeToString(codec.doc, true));

        final T val2 = codec.decode(clazz, node);

        Assert.assertEquals(val, val2);
    }
}