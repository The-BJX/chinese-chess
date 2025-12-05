package GameSave;

import data.Position;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.zip.*;

/**
 * Saves and loads complex objects (List<MoveRecord>) with ZLib compression and
 * SHA-256 integrity check to protect against file tampering.
 */
public class ChineseChessDataSaver {

    // --- Configuration ---
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final byte SEPARATOR = '\n';

    /**
     * Converts a byte array to its hexadecimal string representation.
     */
    private static String bytesToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * Compresses a byte array using the Deflater (ZLib).
     */
    private static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * Decompresses a byte array using the Inflater (ZLib).
     */
    private static byte[] decompress(byte[] compressedData) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * Serializes the List of MoveRecord objects, saves it with compression
     * and a cryptographic integrity hash.
     * @param moveHistory The list of moves to save.
     */
    public void saveGameData(List<MoveRecord> moveHistory, String filepath) throws Exception {
        System.out.println("--- Saving Chinese Chess Move History ---");

        // 1. Serialize Complex Object to Bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(moveHistory); // Convert List<MoveRecord> into a byte array
        oos.flush();
        byte[] serializedData = bos.toByteArray();
        oos.close();
        bos.close();

        System.out.println("Original size (Serialized): " + serializedData.length + " bytes");

        // 2. Obscure Data (Compression)
        byte[] compressedData = compress(serializedData);
        System.out.println("Compressed size: " + compressedData.length + " bytes");

        // 3. Create Integrity Hash
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hashBytes = digest.digest(compressedData);
        String sha256Hash = bytesToHex(hashBytes);
        System.out.println("Calculated Hash (" + HASH_ALGORITHM + "): " + sha256Hash);

        // 4. Write to File
        // Combine the hash string, the separator, and the compressed data bytes
        byte[] hashLine = (sha256Hash + (char)SEPARATOR).getBytes(StandardCharsets.US_ASCII);
        byte[] fileContent = new byte[hashLine.length + compressedData.length];

        System.arraycopy(hashLine, 0, fileContent, 0, hashLine.length);
        System.arraycopy(compressedData, 0, fileContent, hashLine.length, compressedData.length);

        Files.write(Paths.get(filepath), fileContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Successfully saved move history to: " + filepath + "\n");
    }

    /**
     * Loads and validates the game state from the file, then deserializes the object.
     * @return The original List of MoveRecord objects, or null if tampered.
     */
    @SuppressWarnings("unchecked") // Safe cast after ObjectInputStream reads
    public List<MoveRecord> loadGameData(String filepath) throws Exception {
        System.out.println("--- Loading and Validating Chinese Chess Move History ---");
        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            System.out.println("Error: File not found at " + filepath);
            return null;
        }

        // 1. Read Data and Expected Hash
        byte[] fileContent = Files.readAllBytes(path);

        // Find the separator (the newline character)
        int separatorIndex = -1;
        for (int i = 0; i < fileContent.length; i++) {
            if (fileContent[i] == SEPARATOR) {
                separatorIndex = i;
                break;
            }
        }

        if (separatorIndex == -1) {
            System.out.println("!!! CRITICAL ERROR: File format corrupted (no hash separator) !!!");
            return null;
        }

        // Extract expected hash and compressed data
        String expectedHash = new String(fileContent, 0, separatorIndex, StandardCharsets.US_ASCII);
        byte[] compressedData = Arrays.copyOfRange(fileContent, separatorIndex + 1, fileContent.length);

        // 2. Recalculate Hash
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] recalculatedHashBytes = digest.digest(compressedData);
        String recalculatedHash = bytesToHex(recalculatedHashBytes);

        System.out.println("Expected Hash:      " + expectedHash);
        System.out.println("Recalculated Hash:  " + recalculatedHash);

        // 3. Integrity Check
        if (!expectedHash.equals(recalculatedHash)) {
            System.out.println("\n!!! SECURITY WARNING: DATA TAMPERED OR CORRUPTED !!!");
            return null;
        }

        System.out.println("Integrity Check PASSED. Data is trustworthy.");

        // 4. Decompress and Deserialize
        byte[] dataBytes = decompress(compressedData);

        ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);

        // Deserialize the object back into the List<MoveRecord>
        List<MoveRecord> moveHistory = (List<MoveRecord>) ois.readObject();
        ois.close();
        bis.close();

        System.out.println("Successfully loaded and validated data from: " + filepath);
        return moveHistory;
    }

}