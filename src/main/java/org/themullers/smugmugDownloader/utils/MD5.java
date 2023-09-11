package org.themullers.smugmugDownloader.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class exposes functions that perform MD5 checksums.
 */
public class MD5 {

    /**
     * Calculate an MD5 sum for a file.
     *
     * @param file the file to checksum
     * @return the MD5 checksum in hex format
     * @throws IOException if an unexpected error occurs while reading from the file
     * @throws NoSuchAlgorithmException if the MD5 algorithm is unknown to Java
     */
    public static String checksum(File file) throws IOException, NoSuchAlgorithmException {
        try (InputStream in = new FileInputStream(file)) {
            return checksum(in);
        }
    }

    /**
     * Calculate an MD5 sum for a stream.
     *
     * @param in  the stream to read from
     * @throws IOException if an unexpected error occurs while reading from the file
     * @throws NoSuchAlgorithmException if the MD5 algorithm is unknown to Java
     * @see <a href="https://stackoverflow.com/a/5471171/22480693">stack overflow</a>
     * @see <a href="https://www.w3docs.com/snippets/java/how-can-i-generate-an-md5-hash-in-java.html">w3docs</a>
     */
    public static String checksum(InputStream in) throws IOException, NoSuchAlgorithmException {

        // create an MD5 message digest
        MessageDigest md = MessageDigest.getInstance("MD5");

        // read everything from the stream into the message digest
        try {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
        }
        finally {
            in.close();
        }

        // calculate the checksum and return it in hex format
        return ByteArray.toHex(md.digest());
    }
}
