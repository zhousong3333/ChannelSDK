/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
*/
package com.skysoul.MR.huawei;

public class GlobalParam
{
    /**
     * 联盟为应用分配的应用ID
     */
    /**
     * APP ID
     */
    public static final String APP_ID = "10637977";
    
    /**
     * 浮标密钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for buoy, the CP need to save the key value on the server for security
     */
    /**************TODO:DELETE*******************/
    public static String BUOY_SECRET = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZuv4qqGTY7FmtHu3vNnXjTZS9wlJDueGt6NLLYS5lhqHEcyQ4zZC+HWOhKneyaa4J4RUJmjKslirO8Ji6/yuJ6UKJHHwN+M0637cWHVkveZ/1SUvDqGUNKwvN8T02qWJAyNVjyUu14M/z7wRx8uR766lKRMEQQy+6QGZrJN9FPAgMBAAECgYAKW9pSmwzaSu9NUPe3cH6j4tY3M5ufF2EcFwycg2I/7XCHVsp/V6JuryVAsQ8+Ph/uq8nadgB856Wqd+MRUyIU+Ozww4bqqSOWPIZvs1Qcotz4UpMY6hQ+wNuHFYmrWnqYZYXV/UvRz+pBDyVDvjeT23Xs7cxjGo9n/Z6RoBkjEQJBAMJWs9iT/KC0omvyYnZVfw/J1Wfzzs3SphgtUnyDeSejhQQEvEbL+Z+ELBsxO4rkOCTMQ1WKT7tFkrdEW45LDPMCQQCxFiHpimizNEmKgMy5AmyKz3iKY2rYl65GpQrkyg+2aq7pvhIEjcpkmKaHOXPhIysEQlYDt8CuNjxdhOLVZhE1AkAoIfuV+EmtbmA0ZqPKyDErM4UsqH7Gpn/CcmmcaRI2C2DGyauP+0Obm5H4M5yWPDcT8lLdcWixxdZcnnNB2WHdAkEAhsvQGJNQy0g7yn/sVc/wi2EN4ll21VPwRroOpsTgXPysFCSoVmsP6q7/4+LpwDAi6GUOUFopAvMp1A35FHNTGQJAC7p7oTQfNTK3gCklNJnzrzzn6A1mQVrLZNW+FY6N9TZe/k/vl9ycUWaXa9mFRPhlZxvPsJRRoZMlllbfcD3jKA==";
    
    /**
     * 支付ID
     */
	 /**
     * Pay ID
     */
    public static final String PAY_ID = "890086000102022349";
    
    /**
     * 支付私钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for pay, the CP need to save the key value on the server for security
     */
    /**************TODO:DELETE*******************/
    public static String PAY_RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCPPFswA/Pb1ypCWQOOw27xsjR4e4Oa/+5TASzLAcOyCdpA641LNqrTQcLbfDRvQ99sKmOpuG9aeVLrX90wAg4n3uLpckMUUJx+RpgOixmf9fwb0Y9Bitzbr3xhXzn83rhh3sCCwmJK3XeWKYUg6ZUiDlQ4yRQhuG4rftaSdPsGFv+CIN1bH3viGbMcLOTDcyZhZYhLhmfaHCyNxeo+M425bwlz+7tTvXjg0HwWHudNnNCMZFLaEkNXnVpEX5ROYXnJciIR/U1DJRLkmh9V+22faltWGF+/7VRWQrylciiitXjB/pm5V4hRouzN/CMYd1Y31A/g3T2+6lq9jpy9C62DAgMBAAECggEBAIJl0SI6RvQZwvgZ71o14zeYI5qX32zpl/7q7+t0lMLto0s85inV/9hJYurL/TRaFvly8b5eEMnN78YmRMhi5p43lF4wTo8dAAsZm26KBe8RUs3EjuV9VKmO7e37cmAaJsO6tYmVC0Tar/b9n5wh6knRv6zeqjw0md9xHkjAxJJ6WdXOj/V1wQ0ve0zFQUSPH+IXaQEODxJzxyphrod3uZf1OmKVAqOb3K2eocjzH6YaB7NChh7kQTu+eqRUmyx+cpNORnJ9kxHN9vOyphVMDaf2uX2eiPKDr6rD9I27pWQXLc67Eoz2MceNIiKtZexN/vB2RHmYIYfaSc4zx8eXM4ECgYEAzfq5Qr0jwjhe7lqk46m1XMr097L0A4/VP0BZNMa5lGggb13D352yV67bq53VdLe7YJbLdzVjB3azBkxv+L1SnVzGpcsyn9s8D1mKTduZioTTRDFN1vQbVnaTwGczEWpJjAEOevI499/LCzWOkjJyHoK9G9JTxepdCeBIuI8S5sMCgYEAsgUAl54m4SwyVT8YCERViGWpOD4pLEw4run8ngRbv92YK39U6hQGueJg4CQ8R+z68mFYisPNx4I6Wnp5D2q52QgT6TCn0jF8aUqmiEpqUZUba4QQLxbClTCjmKSWxaDZChk+EcJtJ6/NZVyDg9a0/OEGtouB2xEz6R/XfFZPMkECgYApky3Jlruc/BvNKGaXvS1qKV0AkhJGlcWJGsfmqyXSGqJRWC+s9vuETzrvjNAE2wDrIgS3bwMqAn9oCg2fuzbtpEhENptlQK8CuiP8SxLWrrCl1v3LIUJTVWPFNg9MuJNXuu7dSf7NqVImVQ0N3TjDh8f70ZwvGnrk/2wuNFKwWQKBgGX7+afiobF0KW2u1Ky/wSR/4R7odZv8GqkSQXehVZKod2nL67CiQmwxZYBjIemuuOEspZXxsKzwTU/SRBJx+QB94TJBfpMraHPFoXwI7bnJK0lAAimH53eiwsZK3Flh2ZUwet+fu/r1ezzrcCiSU2Nf4wGnEICY7V1LsZ54r6xBAoGBAKGKXhH/TF378OK01NJtXKHsKDBxZQW6ZFZtzB0+CzGkr6dkk2IDfbhzNFFnR+qeGAAs0Pgy5+dsCahcdOt6MyVOC0pvGMm0RUJ7A0t6Qph3sActHDTCoiWPlxZVyqSVXm8eaig10kBFr3oWuRFNp+BtcTsNjh/RAYxQYtpkkgPY";
    
    /**
     * 支付公钥
     */
    /**
     * public key for pay
     */
    public static final String PAY_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjzxbMAPz29cqQlkDjsNu8bI0eHuDmv/uUwEsywHDsgnaQOuNSzaq00HC23w0b0PfbCpjqbhvWnlS61/dMAIOJ97i6XJDFFCcfkaYDosZn/X8G9GPQYrc2698YV85/N64Yd7AgsJiSt13limFIOmVIg5UOMkUIbhuK37WknT7Bhb/giDdWx974hmzHCzkw3MmYWWIS4Zn2hwsjcXqPjONuW8Jc/u7U7144NB8Fh7nTZzQjGRS2hJDV51aRF+UTmF5yXIiEf1NQyUS5JofVfttn2pbVhhfv+1UVkK8pXIoorV4wf6ZuVeIUaLszfwjGHdWN9QP4N09vupavY6cvQutgwIDAQAB";
    /**
     * 登录签名公钥
     */
	
	public static final String LOGIN_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKLBMs2vXosqSR2rojMzioTRVt8oc1ox2uKjyZt6bHUK0u+OpantyFYwF3w1d0U3mCF6rGUnEADzXiX/2/RgLQDEXRD22er31ep3yevtL/r0qcO8GMDzy3RJexdLB6z20voNM551yhKhB18qyFesiPhcPKBQM5dnAOdZLSaLYHzQkQKANy9fYFJlLDo11I3AxefCBuoG+g7ilti5qgpbkm6rK2lLGWOeJMrF+Hu+cxd9H2y3cXWXxkwWM1OZZTgTq3Frlsv1fgkrByJotDpRe8SwkiVuRycR0AHsFfIsuZCFwZML16EGnHqm2jLJXMKIBgkZTzL8Z+201RmOheV4AQIDAQAB";

    /*
     * 支付页面横竖屏参数：1表示竖屏，2表示横屏，默认竖屏
     */
    // portrait view for pay UI
	public static final int PAY_ORI = 1;
	// landscape view for pay UI
	public static final int PAY_ORI_LAND = 2;
    

	/**
	 * 生成签名时需要使用RSA的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the pay private key.The CP need to modify the
	 * value for the real server.
	 */
	public static final String GET_PAY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getPayPrivate";

	/**
	 * 调用浮标时需要使用浮标的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the buoy private key.The CP need to modify the
	 * value for the real server.
	 */
	public static final String GET_BUOY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getBuoyPrivate";
    
    public interface PayParams
    {
        public static final String USER_ID = "userID";
        
        public static final String APPLICATION_ID = "applicationID";
        
        public static final String AMOUNT = "amount";
        
        public static final String PRODUCT_NAME = "productName";
        
        public static final String PRODUCT_DESC = "productDesc";
        
        public static final String REQUEST_ID = "requestId";
        
        public static final String USER_NAME = "userName";
        
        public static final String SIGN = "sign";
        
        public static final String NOTIFY_URL = "notifyUrl";
        
        public static final String SERVICE_CATALOG = "serviceCatalog";
        
        public static final String SHOW_LOG = "showLog";
        
        public static final String SCREENT_ORIENT = "screentOrient";
        
        public static final String SDK_CHANNEL = "sdkChannel";
        
        public static final String URL_VER = "urlver";
    }
    
}
