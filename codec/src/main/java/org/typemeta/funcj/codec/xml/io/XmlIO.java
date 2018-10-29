package org.typemeta.funcj.codec.xml.io;

import org.typemeta.funcj.codec.CodecException;

import javax.xml.stream.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public abstract class XmlIO {
    public static Input inputOf(XMLStreamReader rdr) {
        return new InputImpl(rdr);
    }

    public static Input inputOf(Reader rdr, String rootElemName) {
        try {
            final XMLInputFactory xmlInFact = XMLInputFactory.newFactory();
            xmlInFact.setProperty(XMLInputFactory.IS_COALESCING, true);
            final XMLStreamReader xrdr = xmlInFact.createXMLStreamReader(rdr);
            final Input in = inputOf(xrdr);
            in.startDocument();
            in.startElement(rootElemName);
            return in;
        } catch (XMLStreamException ex) {
            throw new CodecException(ex);
        }
    }

    public static Output outputOf(XMLStreamWriter wtr) {
        return new OutputImpl(wtr);
    }

    public static Output outputOf(Writer wtr, String rootElemName) {
        try {
            final XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
            xmlOutFact.setProperty("escapeCharacters", false);
            final XMLStreamWriter xwtr = xmlOutFact.createXMLStreamWriter(wtr);
            xwtr.writeStartDocument();
            xwtr.writeStartElement(rootElemName);

            return new OutputImpl(xwtr, out -> {
                xwtr.writeEndElement();
                xwtr.writeEndDocument();
            });
        } catch (XMLStreamException ex) {
            throw new CodecException(ex);
        }
    }

    public interface Input {
        enum Type {
            START_DOCUMENT,
            END_DOCUMENT,
            START_ELEMENT,
            END_ELEMENT,
            CHARACTERS,
            OTHER
        }

        public class AttributeMap {
            protected final Map<String, String> attrMap = new TreeMap<>();

            void load(XMLStreamReader rdr) {
                attrMap.clear();
                final int numAtrrs = rdr.getAttributeCount();
                if (numAtrrs > 0) {
                    for (int i = 0; i < numAtrrs; ++i) {
                        final String name = rdr.getAttributeLocalName(i);
                        final String value = rdr.getAttributeValue(i);
                        attrMap.put(name, value);
                    }
                }
            }

            void clear() {
                attrMap.clear();
            }

            public boolean hasName(String name) {
                return attrMap.containsKey(name);
            }

            public String getValue(String name) {
                return attrMap.get(name);
            }

            public boolean nameHasValue(String name, String value) {
                return attrMap.containsKey(name) &&
                        attrMap.get(name).equals(value);
            }
        }

        String location();

        boolean hasNext();
        Type next();

        Type type();

        void close();

        void startDocument();
        void endDocument();

        String startElement();
        void startElement(String name);
        AttributeMap attributeMap();
        void endElement();

        boolean readBoolean();

        String readString();
        char readChar();

        byte readByte();
        short readShort();
        int readInt();
        long readLong();
        float readFloat();
        double readDouble();
        BigDecimal readBigDecimal();
        Number readNumber();
        String readStringNumber();
    }

    public interface Output {
        Output close();

        Output startDocument();
        Output endDocument();

        Output startElement(String localName);
        Output emptyElement(String localName);
        Output attribute(String localName, String value);
        Output endElement();

        Output writeBoolean(boolean value);

        Output writeString(String value);
        Output writeChar(char value);

        Output writeByte(byte value);
        Output writeShort(short value);
        Output writeInt(int value);
        Output writeLong(long value);
        Output writeFloat(float value);
        Output writeDouble(double value);
        Output writeNumber(Number value);
        Output writeBigDecimal(BigDecimal value);
        Output writeStringNumber(String value);
    }
}
