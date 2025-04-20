package com.pg.mbti.components;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
class MultipartFileImpl implements MultipartFile {
    private final InputStream inputStream;
    private final long size;
    private final String name;
    private final String contentType;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return inputStream.readAllBytes();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream output = new FileOutputStream(dest)) {
            output.write(getBytes());
        }
    }
}