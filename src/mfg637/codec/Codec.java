package mfg637.codec;

import java.security.DigestException;

public interface Codec<Input, Output> {
    Output encode(Input data) throws CloneNotSupportedException;
    Input decode(Output code) throws CloneNotSupportedException;
}
