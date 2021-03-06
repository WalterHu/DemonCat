package org.spongycastle.jsse.provider;

import org.spongycastle.jsse.BCSSLConnection;
import org.spongycastle.tls.ChannelBinding;
import org.spongycastle.tls.TlsContext;

import javax.net.ssl.SSLSession;

class ProvSSLConnection
    implements BCSSLConnection
{
    protected final TlsContext tlsContext;
    protected final SSLSession session; 

    ProvSSLConnection(TlsContext tlsContext, SSLSession session)
    {
        this.tlsContext = tlsContext;
        this.session = session;
    }

    public byte[] getChannelBinding(String channelBinding)
    {
        if (channelBinding.equals("tls-unique"))
        {
            return tlsContext.exportChannelBinding(ChannelBinding.tls_unique);
        }

        throw new UnsupportedOperationException();
    }

    public SSLSession getSession()
    {
        return session;
    }
}
