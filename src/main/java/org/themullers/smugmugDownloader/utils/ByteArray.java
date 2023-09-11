package org.themullers.smugmugDownloader.utils;

public class ByteArray {

    /**
     * Render a byte array as a hex string.
     *
     * @param bytes  the byte array to render
     * @return  the hex string rendition of the provided byte array
     */
    protected static String toHex(byte[] bytes) {

        // allocate a buffer to write into; we'll need two chars per byte
        StringBuilder sb = new StringBuilder(2 * bytes.length);

        // render each by in hex
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        // return the hex string
        return sb.toString();
    }
}
