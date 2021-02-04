package mfg637.codec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public interface FilesCodec {
    void encode(RandomAccessFile input_file, FileOutputStream output_file) throws IOException, CloneNotSupportedException;
    void decode(File input_file, RandomAccessFile output_file) throws IOException, CloneNotSupportedException;
}
