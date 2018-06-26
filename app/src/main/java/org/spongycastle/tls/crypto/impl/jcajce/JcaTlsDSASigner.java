package org.spongycastle.tls.crypto.impl.jcajce;

import org.spongycastle.tls.SignatureAlgorithm;

import java.security.PrivateKey;

/**
 * Implementation class for generation of the raw DSA signature type using the JCA.
 */
public class JcaTlsDSASigner
    extends JcaTlsDSSSigner
{
    public JcaTlsDSASigner(JcaTlsCrypto crypto, PrivateKey privateKey)
    {
        super(crypto, privateKey, SignatureAlgorithm.dsa, "NoneWithDSA");
    }
}
