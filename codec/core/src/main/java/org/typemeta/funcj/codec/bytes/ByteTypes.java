package org.typemeta.funcj.codec.bytes;

import org.typemeta.funcj.codec.CodecConfig;
import org.typemeta.funcj.codec.bytes.io.*;
import org.typemeta.funcj.codec.stream.StreamCodecFormat;

import java.io.*;

public abstract class ByteTypes {

    /**
     * Interface for classes which provide configuration information
     * for {@link ByteCodecCore} implementations.
     */
    public interface Config extends CodecConfig {
    }

    /**
     * Interface for classes which implement an input stream of bytes
     */
    public interface InStream extends StreamCodecFormat.Input<InStream> {
    }

    /**
     * Interface for classes which implement an output stream of bytes
     */
    public interface OutStream extends StreamCodecFormat.Output<OutStream> {
    }

    public static InStream inputOf(InputStream is) {
        return new InputImpl(new DataInputStream(is));
    }

    public static InStream inputOf(DataInput input) {
        return new InputImpl(input);
    }

    public static OutStream outputOf(OutputStream os) {
        return new OutputImpl(new DataOutputStream(os));
    }

    public static OutStream outputOf(DataOutput output) {
        return new OutputImpl(output);
    }
}
