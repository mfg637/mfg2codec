package mfg637.crypt.mfg2.codec;

import java.io.IOException;

public interface TestableCodec {
    byte[] reversing(byte[] input) throws IOException;
    byte[] encode(byte[] input) throws CloneNotSupportedException, IOException;
    byte[] decode(byte[] input) throws CloneNotSupportedException, IOException;
}
