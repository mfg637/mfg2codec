package mfg637.crypt.mfg2.codec;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MFG2Ftest extends MFG2FCodec implements TestableCodec{
    public MFG2Ftest(byte[] password) throws NoSuchAlgorithmException {
        super(password);
    }

    @Override
    public byte[] reversing(byte[] input) throws IOException {
        long file_size = input.length;
        long default_block_size = 64;
        RandomAccessFile output_file = new RandomAccessFile("/tmp/test1", "rw");
        for (
                long current_position = file_size - default_block_size, prev_position = file_size;
                ;
                prev_position=current_position,
                        current_position=Math.max(current_position - default_block_size, 0)
        ) {
            int block_size = (int) (prev_position - current_position);
            //input_file.seek(current_position);
            //byte[] src_buffer = new byte[block_size];
            //input_file.read(src_buffer, 0, block_size);
            byte[] src_buffer = Arrays.copyOfRange(input, (int) current_position, (int) prev_position);
            byte[] reversed = this.reverse(src_buffer);
            output_file.write(reversed, 0, block_size);
            if (current_position == 0)
                break;
        }
        output_file.seek(0);
        byte[] output = new byte[(int) file_size];
        output_file.read(output);
        output_file.close();
        return output;
    }

    @Override
    public byte[] encode(byte[] input) throws CloneNotSupportedException, IOException {
        RandomAccessFile input_file = new RandomAccessFile("/tmp/test2", "rw");
        input_file.write(input);
        FileOutputStream out_file_w = new FileOutputStream("/tmp/test3");
        encode(input_file, out_file_w);
        input_file.close();
        out_file_w.close();
        FileInputStream out_file_r = new FileInputStream("/tmp/test3");
        byte[] output = new byte[input.length];
        out_file_r.read(output);
        out_file_r.close();
        return output;
    }

    @Override
    public byte[] decode(byte[] input) throws CloneNotSupportedException, IOException {
        File input_file = new File("/tmp/test4");
        FileOutputStream input_file_w = new FileOutputStream(input_file);
        input_file_w.write(input);
        input_file_w.close();
        RandomAccessFile output_file = new RandomAccessFile("/tmp/test5", "rw");
        decode(input_file, output_file);
        byte[] output = new byte[input.length];
        output_file.seek(0);
        output_file.read(output);
        output_file.close();
        return output;
    }
}
