package com.pg.mbti.util;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * A custom implementation of the {@link MultipartFile} interface.
 * This class is used to create a {@link MultipartFile} object from an {@link InputStream},
 * typically for internal file handling (e.g., uploading default images).
 */
@AllArgsConstructor
@Slf4j // Enable logging for this class
class MultipartFileImpl implements MultipartFile {
    private final InputStream inputStream;
    private final long size;
    private final String name;
    private final String contentType;

    /**
     * Returns the name of the parameter in the multipart form.
     * In this custom implementation, it returns the provided `name`.
     *
     * @return The name of the form field.
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the original filename in the client's filesystem.
     * In this custom implementation, it returns the provided `name`.
     *
     * @return The original filename.
     */
    @Override
    public String getOriginalFilename() {
        return name;
    }

    /**
     * Returns the content type of the file.
     *
     * @return The content type (e.g., "image/png").
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * Indicates whether the uploaded file is empty.
     *
     * @return {@code true} if the file size is 0, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of the file in bytes.
     *
     * @return The file size.
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Returns the contents of the file as an array of bytes.
     *
     * @return The file contents as a byte array.
     * @throws IOException If an I/O error occurs.
     */
    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        log.debug("Getting bytes for multipart file: {}", name); // Log getting bytes
        InputStream resetableInputStream = inputStream;
        if (resetableInputStream.markSupported()) {
            resetableInputStream.reset(); // Attempt to reset if supported
        } else {
            // If reset is not supported, and this method is called multiple times,
            // the stream will be consumed. For single use, this is okay.
            log.warn("InputStream for {} does not support mark/reset. Repeated calls to getBytes() may fail if stream is consumed.", name); // Log if reset not supported
        }
        return resetableInputStream.readAllBytes();
    }

    /**
     * Returns an {@link InputStream} to read the contents of the file.
     *
     * @return An {@link InputStream}.
     */
    @NotNull
    @Override
    public InputStream getInputStream() {
        log.debug("Getting InputStream for multipart file: {}", name); // Log getting InputStream
        return inputStream;
    }

    /**
     * Transfers the received file to the given destination file.
     *
     * @param dest The destination file.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalStateException If the file has already been moved in the filesystem.
     */
    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        log.info("Transferring multipart file '{}' to destination: {}", name, dest.getAbsolutePath()); // Log file transfer
        try (FileOutputStream output = new FileOutputStream(dest)) {
            output.write(getBytes());
        }
        log.debug("File '{}' successfully transferred.", name); // Log successful transfer
    }
}