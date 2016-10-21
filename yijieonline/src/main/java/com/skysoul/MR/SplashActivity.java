//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.skysoul.MR;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import com.snowfish.cn.ganga.helper.SFOnlineSplashActivity;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SplashActivity extends SFOnlineSplashActivity {
    public static final byte[] STRING_ACTIVITY_CLASS_NAME = new byte[]{(byte)122, (byte)121, (byte)95, (byte)99, (byte)108, (byte)97, (byte)115, (byte)115, (byte)95, (byte)110, (byte)97, (byte)109, (byte)101};

    public SplashActivity() {
    }

    public int getBackgroundColor() {
        return -1;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onSplashStop() {
        int id = this.getResources().getIdentifier(new String(STRING_ACTIVITY_CLASS_NAME), "string", this.getPackageName());
        String str = this.getString(id);
        Log.e("SplashActivity", "SplashActivity str :" + str);
        Intent localIntent = new Intent();
        localIntent.setClassName(this.getPackageName(), str);
        this.startActivity(localIntent);
        this.finish();
    }

    public void getSingInfo() {
        try {
            PackageInfo e = this.getPackageManager().getPackageInfo(this.getPackageName(), 64);
            Signature[] signs = e.signatures;
            Signature sign = signs[0];
            this.parseSignature(sign.toByteArray());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void parseSignature(byte[] signature) {
        try {
            CertificateFactory e = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)e.generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            Log.v("aaa", "signName:" + cert.getSigAlgName());
            Log.v("aaa", "pubKey:" + pubKey);
            Log.v("aaa", "signNumber:" + signNumber);
            Log.v("aaa", "subjectDN:" + cert.getSubjectDN().toString());
        } catch (CertificateException var6) {
            var6.printStackTrace();
        }

    }
}
