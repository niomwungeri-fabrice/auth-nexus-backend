package io.lynx.utils;

import com.google.common.hash.Hashing;
import io.lynx.exceptions.InternalServerErrorException;
import org.apache.commons.codec.binary.Base32;

import java.nio.ByteBuffer;
import java.time.Instant;

public class TotpUtil {
    private static final int TOTP_TIME_STEP_SECONDS = 30;
    private static final int TOTP_DIGITS = 6;

    public static String generateSecretKey() {
        Base32 base32 = new Base32();
        byte[] secretKeyBytes = new byte[10];
        for (int i = 0; i < secretKeyBytes.length; i++) {
            secretKeyBytes[i] = (byte) (Math.random() * 256);
        }
        return base32.encodeToString(secretKeyBytes);
    }

    public static String generateTotp(String secretKey) {
        try {
            // Decode the Base32 secret key
            Base32 base32 = new Base32();
            byte[] keyBytes = base32.decode(secretKey);

            // Get the current time step
            long currentTimeMillis = Instant.now().toEpochMilli();
            long timeStep = currentTimeMillis / (TOTP_TIME_STEP_SECONDS * 1000);

            // Generate the HMAC-SHA1 hash
            byte[] hash = Hashing.hmacSha1(keyBytes)
                    .hashBytes(ByteBuffer.allocate(8).putLong(timeStep).array())
                    .asBytes();

            // Extract dynamic binary code (last 4 bits of the hash's last byte)
            int offset = hash[hash.length - 1] & 0x0F;
            int binaryCode = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);

            // Generate the TOTP code
            return String.format("%0" + TOTP_DIGITS + "d", binaryCode % (int) Math.pow(10, TOTP_DIGITS));
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("error generating TOTP: " + e.getMessage());
        }
    }
}
