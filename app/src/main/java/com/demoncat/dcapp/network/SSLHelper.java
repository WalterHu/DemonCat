package com.demoncat.dcapp.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jsse.provider.BouncyCastleJsseProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Make ssl factory and provide host name verify.
 * @author archermind on 2017/5/7
 */
public class SSLHelper {
    private static final String TAG = SSLHelper.class.getSimpleName();

    private static final String CERT_ENTRY_ALIAS = "trust";

    /**
     * Get ssl socket factory according to correspond certificate
     * @param context
     * @param cert
     * @param factory
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactoryLocally(Context context, String cert, TrustManagerFactory factory) {
        if (context == null) {
            Log.e(TAG, "getSSLSocketFactory context is null.");
            return null;
        }
        Security.setProperty("ocsp.enable", "true");
        System.setProperty("com.sun.net.ssl.checkRevocation", "true");
        System.setProperty("com.sun.security.enableCRLDP", "true");
        System.setProperty("jdk.tls.client.enableStatusRequestExtension", "true");
        // socket connect timeout
        System.setProperty("sun.net.client.defaultConnectTimeout", String
                .valueOf(50000));// （单位：毫秒）
        System.setProperty("sun.net.client.defaultReadTimeout", String
                .valueOf(50000)); // （单位：毫秒）
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509",
                    BouncyCastleProvider.PROVIDER_NAME);
            Log.d(TAG, "getSSLSocketFactory cf: " +
                    certificateFactory + ", provider: " + certificateFactory.getProvider());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType(),
                    BouncyCastleProvider.PROVIDER_NAME);
            Log.d(TAG, "getSSLSocketFactory ks: " +
                    KeyStore.getDefaultType() + ", provider: " + keyStore.getProvider());
            keyStore.load(null, null);

            InputStream certificate = context.getAssets().open(cert);
            Certificate cer = certificateFactory.generateCertificate(certificate);
            keyStore.setCertificateEntry(CERT_ENTRY_ALIAS, cer);

            if (certificate != null) {
                certificate.close();
            }

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2",
                    BouncyCastleJsseProvider.PROVIDER_NAME);
            Log.d(TAG, "getSSLSocketFactory sslContext provider: " + sslContext.getProvider());
            factory.init(keyStore);
            Provider provider = factory.getProvider();
            Log.d(TAG, "getSSLSocketFactory trust factory provider: " + provider);
            TrustManager[] trustManagers = factory.getTrustManagers();
            for (int i = 0; i < trustManagers.length; i ++) {
                Log.d(TAG,
                        "getSSLSocketFactory trust manager " + i + ", " + trustManagers[i]);
            }
            sslContext.init(null, factory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            Log.d(TAG, "getSSLSocketFactory socket factory: " + socketFactory +
                    "provider: " + provider);
            return new Tls12SocketFactory(socketFactory);
        } catch (Exception e) {
            Log.d(TAG, "getSSLSocketFactoryFromAssets error occurs.", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get X509 Trust Manager
     * @param factory
     * @return
     */
    public static X509TrustManager getX509TrustManager(TrustManagerFactory factory) {
        if (factory != null) {
            TrustManager[] managers = factory.getTrustManagers();
            for (TrustManager manager : managers) {
                if (manager != null && manager instanceof X509TrustManager) {
                    return (X509TrustManager) manager;
                }
            }
        }
        return null;
    }

    /**
     * Get trust manager factory
     * @return
     */
    public static TrustManagerFactory getTrustManagerFactory() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance("PKIX",
                    BouncyCastleJsseProvider.PROVIDER_NAME);
            Log.d(TAG,
                    "getTrustManagerFactory algorithm: " + factory.getAlgorithm() +
                            ", provider: " + factory.getProvider());
            return factory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get host verifier for specified host urls
     * @param hostUrls
     * @return
     */
    public static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        return new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        return true;
                    }
                }
                return false;
//                for (String host : hostUrls) {
//                    boolean verified
//                            = HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session);
//                    if (verified) {
//                        return true;
//                    }
//                }
//                return false;
            }
        };
    }

    //----------Trust all https certs---------
    public static SSLSocketFactory getTrustAllSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = new Tls12SocketFactory(sc.getSocketFactory());
            // ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }

    public static class TrustAllCerts implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    /**
     * Get trust all host name verifier
     * @return
     */
    public static HostnameVerifier getTrustAllHostnameVerifier() {
        return new HostnameVerifier() {

            @SuppressLint("BadHostnameVerifier")
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    /*--------TLS1.1 & TLS1.2 Support---------*/
    public static class Tls12SocketFactory extends SSLSocketFactory {
        private static final String[] TLS_SUPPORT_VERSION = {"TLSv1.1", "TLSv1.2"};

        final SSLSocketFactory delegate;

        public Tls12SocketFactory(SSLSocketFactory base) {
            this.delegate = base;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket s) {
            Log.d(TAG, "patch Socket: " + s);
            if (s instanceof SSLSocket) {
                ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
                ((SSLSocket) s).addHandshakeCompletedListener(new HandshakeCompletedListener() {
                    @Override
                    public void handshakeCompleted(HandshakeCompletedEvent event) {
                        Log.d(TAG, "handshakeCompleted event: " + event);
                    }
                });
            }
            return s;
        }
    }
}
