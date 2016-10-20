package com.mino.jdict;

/**
 * Created by Dominik on 9/21/2015.
 */
public class MyDownloaderService extends com.google.android.vending.expansion.downloader.impl.DownloaderService {

    // You must use the public key belonging to your publisher account
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjC3dstxqfOVAOncZ8em4kl5igKhidXvVyyFl05HF/t4KrI8NK488p6KmVB1MSPs2tzq2ENbXr5FbthDp9GfaWlitUJM3OW13OelEq9s68Vor/3WhhSghc4MKq6NISeVuydxS/TvxY+4p6J6+QVJCpTbNoZyX3Xy3kxwI0ToW8cF64akFY5tjmAnhmrK1fpnyr7S0+/uFYP98t7uBJki9ais91yBjoZEouA8pvI3pBtDXEw+Md6cBLMWzSrCdFOOCukeBsZZ4nQFrWqSOib4Ei1z5W/3Sa0iWQiD/+p7Rp8LEEL0KpoM1JNh5VQApmhHMiFP6SjQf9H5P+nbcT0FurQIDAQAB";
    // You should also modify this salt
    public static final byte[] SALT = new byte[] { 1, 42, -12, -1, 54, 98,
            -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return MyReceiver.class.getName();
    }
}
