package io.pp.arcade.v1.global.util;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

public class JpegMultipartFile implements MultipartFile {

    private final byte[] bytes;
    String name;
    String originalFilename;
    String contentType;
    boolean isEmpty;
    long size;

    public JpegMultipartFile(byte[] bytes, String name) {
        this.bytes = bytes;
        this.name = name;
        this.originalFilename = name + ".jpeg";
        this.contentType = "image/jpeg";
        this.size = bytes.length;
        this.isEmpty = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IllegalStateException {
    }
}
