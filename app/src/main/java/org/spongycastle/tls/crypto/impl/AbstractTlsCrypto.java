package org.spongycastle.tls.crypto.impl;

import org.spongycastle.tls.EncryptionAlgorithm;
import org.spongycastle.tls.MACAlgorithm;
import org.spongycastle.tls.crypto.TlsCertificate;
import org.spongycastle.tls.crypto.TlsCipher;
import org.spongycastle.tls.crypto.TlsCrypto;
import org.spongycastle.tls.crypto.TlsCryptoParameters;
import org.spongycastle.tls.crypto.TlsSecret;

import java.io.IOException;

/**
 * Base class for a TlsCrypto implementation that provides some needed methods from elsewhere in the impl package.
 */
public abstract class AbstractTlsCrypto
    implements TlsCrypto
{
    /**
     * Adopt the passed in secret, creating a new copy of it..
     *
     * @param secret the secret to make a copy of.
     * @return a TlsSecret based the original secret.
     */
    public TlsSecret adoptSecret(TlsSecret secret)
    {
        // TODO[tls] Need an alternative that doesn't require AbstractTlsSecret (which holds literal data)
        if (secret instanceof AbstractTlsSecret)
        {
            AbstractTlsSecret sec = (AbstractTlsSecret)secret;

            return createSecret(sec.copyData());
        }

        throw new IllegalArgumentException("unrecognized TlsSecret - cannot copy data: " + secret.getClass().getName());
    }

    /**
     * Create a cipher for the specified encryption and MAC algorithms.
     * <p>
     * See enumeration classes {@link EncryptionAlgorithm}, {@link MACAlgorithm} for appropriate argument values.
     * </p>
     * @param cryptoParams context specific parameters.
     * @param encryptionAlgorithm the encryption algorithm to be employed by the cipher.
     * @param macAlgorithm the MAC algorithm to be employed by the cipher.
     * @return a {@link TlsCipher} implementing the encryption and MAC algorithm.
     * @throws IOException
     */
    protected abstract TlsCipher createCipher(TlsCryptoParameters cryptoParams, int encryptionAlgorithm, int macAlgorithm)
            throws IOException;

    /**
     * Return an encryptor based on the public key in certificate.
     *
     * @param certificate the certificate carrying the public key.
     * @return a TlsEncryptor based on the certificate's public key.
     */
    protected abstract TlsEncryptor createEncryptor(TlsCertificate certificate)
        throws IOException;
}
