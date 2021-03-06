package org.spongycastle.tls.crypto.impl.bc;

import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.DHPrivateKeyParameters;
import org.spongycastle.crypto.params.DHPublicKeyParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.tls.Certificate;
import org.spongycastle.tls.TlsCredentialedAgreement;
import org.spongycastle.tls.crypto.TlsCertificate;
import org.spongycastle.tls.crypto.TlsSecret;

import java.io.IOException;

/**
 * Credentialed class generating agreed secrets from a peer's public key for our end of the TLS connection using the BC light-weight API.
 */
public class BcDefaultTlsCredentialedAgreement
    implements TlsCredentialedAgreement
{
    protected TlsCredentialedAgreement agreementCredentials;

    public BcDefaultTlsCredentialedAgreement(BcTlsCrypto crypto, Certificate certificate, AsymmetricKeyParameter privateKey)
    {
        if (crypto == null)
        {
            throw new IllegalArgumentException("'crypto' cannot be null");
        }
        if (certificate == null)
        {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty())
        {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (privateKey == null)
        {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (!privateKey.isPrivate())
        {
            throw new IllegalArgumentException("'privateKey' must be private");
        }

        if (privateKey instanceof DHPrivateKeyParameters)
        {
            agreementCredentials = new DHCredentialedAgreement(crypto, certificate, (DHPrivateKeyParameters)privateKey);
        }
        else if (privateKey instanceof ECPrivateKeyParameters)
        {
            agreementCredentials = new ECCredentialedAgreement(crypto, certificate, (ECPrivateKeyParameters)privateKey);
        }
        else
        {
            throw new IllegalArgumentException("'privateKey' type not supported: "
                + privateKey.getClass().getName());
        }
    }

    public Certificate getCertificate()
    {
        return agreementCredentials.getCertificate();
    }

    public TlsSecret generateAgreement(TlsCertificate peerCertificate)
        throws IOException
    {
        return agreementCredentials.generateAgreement(peerCertificate);
    }

    private class DHCredentialedAgreement
        implements TlsCredentialedAgreement
    {
        final BcTlsCrypto crypto;
        final Certificate certificate;
        final DHPrivateKeyParameters privateKey;

        DHCredentialedAgreement(BcTlsCrypto crypto, Certificate certificate, DHPrivateKeyParameters privateKey)
        {
            this.crypto = crypto;
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        public TlsSecret generateAgreement(TlsCertificate peerCertificate) throws IOException
        {
            DHPublicKeyParameters peerPublicKey = BcTlsCertificate.convert(crypto, peerCertificate).getPubKeyDH();
            return crypto.adoptLocalSecret(BcTlsDHDomain.calculateBasicAgreement(privateKey, peerPublicKey));
        }

        public Certificate getCertificate()
        {
            return certificate;
        }
    }

    private class ECCredentialedAgreement
        implements TlsCredentialedAgreement
    {
        final BcTlsCrypto crypto;
        final Certificate certificate;
        final ECPrivateKeyParameters privateKey;

        ECCredentialedAgreement(BcTlsCrypto crypto, Certificate certificate, ECPrivateKeyParameters privateKey)
        {
            this.crypto = crypto;
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        public TlsSecret generateAgreement(TlsCertificate peerCertificate) throws IOException
        {
            ECPublicKeyParameters peerPublicKey = BcTlsCertificate.convert(crypto, peerCertificate).getPubKeyEC();
            return crypto.adoptLocalSecret(BcTlsECDomain.calculateBasicAgreement(privateKey, peerPublicKey));
        }

        public Certificate getCertificate()
        {
            return certificate;
        }
    }
}
