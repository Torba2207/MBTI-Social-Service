package com.pg.mbti.util;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
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

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return inputStream.readAllBytes();
    }

    @NotNull
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        try (FileOutputStream output = new FileOutputStream(dest)) {
            output.write(getBytes());
        }
    }
}