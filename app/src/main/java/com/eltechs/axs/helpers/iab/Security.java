package com.eltechs.axs.helpers.iab;

import android.text.TextUtils;
import android.util.Log;
import com.eltechs.axs.helpers.Base64;
import com.eltechs.axs.helpers.Base64.DecoderException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Security {
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "IabHelper/Security";

    public static boolean verifyPurchase(String str, String str2, String str3) {
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("signedData = ");
        sb.append(str2);
        Log.i(str4, sb.toString());
        String str5 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("signature = ");
        sb2.append(str3);
        Log.i(str5, sb2.toString());
        if (TextUtils.isEmpty(str2)) {
            Log.e(TAG, "Purchase verification failed: missing data.");
            return false;
        } else if (TextUtils.isEmpty(str)) {
            Log.i(TAG, "Empty public key, assuming success validation.");
            return true;
        } else if (!TextUtils.isEmpty(str3)) {
            return verify(generatePublicKey(str), str2, str3);
        } else {
            Log.e(TAG, "Purchase verification failed: missing data signature.");
            return false;
        }
    }

    public static PublicKey generatePublicKey(String str) {
        try {
            return KeyFactory.getInstance(KEY_FACTORY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(str)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e2) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e2);
        } catch (DecoderException e3) {
            Log.e(TAG, "Base64 decoding failed.");
            throw new IllegalArgumentException(e3);
        }
    }

    public static boolean verify(PublicKey publicKey, String str, String str2) {
        try {
            Signature instance = Signature.getInstance(SIGNATURE_ALGORITHM);
            instance.initVerify(publicKey);
            instance.update(str.getBytes());
            if (instance.verify(Base64.decode(str2))) {
                return true;
            }
            Log.e(TAG, "Signature verification failed.");
            return false;
        } catch (NoSuchAlgorithmException unused) {
            Log.e(TAG, "NoSuchAlgorithmException.");
            return false;
        } catch (InvalidKeyException unused2) {
            Log.e(TAG, "Invalid key specification.");
            return false;
        } catch (SignatureException unused3) {
            Log.e(TAG, "Signature exception.");
            return false;
        } catch (DecoderException unused4) {
            Log.e(TAG, "Base64 decoding failed.");
            return false;
        }
    }
}
