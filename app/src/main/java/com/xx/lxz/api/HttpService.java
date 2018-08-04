package com.xx.lxz.api;

import android.content.Context;
import android.content.Intent;

import com.xx.lxz.activity.my.LoginActivity;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.SharedPreferencesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpService {

    /**
     * POST提交 其余请求，使用获取到的动态地址
     *
     * @param context
     * @param url
     * @param json
     * @return
     */
    public final static String httpClientPost(Context context, String url,
                                              JSONObject json) {
        SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
        String token = shareUtil.getString("token");

        url = HttpConstant.IPAddress + url;
        // 创建默认的 HttpClient 实例
        HttpClient httpClient = null;

        // 获得https请求 httpclient
        httpClient = new DefaultHttpClient();
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("token", token);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        LogUtil.d("TEST", "url=" + url);

        try {
//            urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            StringEntity se = new StringEntity(json.toString(), "UTF-8");
            se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(httpPost);
            LogUtil.d("TEST", "参数=" + json.toString());

            int requestCode = httpResponse.getStatusLine().getStatusCode();
            String requestMessage = httpResponse.getStatusLine().getReasonPhrase();
            LogUtil.d("TEST", "requestMessage=" + requestMessage);
            LogUtil.d("TEST", "requestCode=" + requestCode);
            if (requestCode == 200) {
//                resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    String result = EntityUtils.toString(httpEntity, "UTF-8");
                    LogUtil.d("TEST", "返回结果result=" + result);
                    try{
                        JSONObject jsonObject=new JSONObject(result);
                        if(jsonObject.optInt("code")==2){
                            shareUtil.setString("phone", "");
                            shareUtil.setString("cid","");
                            shareUtil.setString("token", "");
                            shareUtil.setBoolean("IsLogin",false);
                            shareUtil.setString("phone","");
                            Intent intent=new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            return null;
                        }else{
                            return result;
                        }
                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            }else{
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg("网络异常"+requestCode);
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            }
        } catch (ClientProtocolException e) {
            e.getMessage();
            RefreshModel refreshMode = new RefreshModel();
            refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
            refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
            refreshMode.setMsg(e.getMessage());
            EventBus.getDefault().post(
                    new RefreshtEvent(refreshMode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            RefreshModel refreshMode = new RefreshModel();
            refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
            refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
            refreshMode.setMsg(e.getMessage());
            EventBus.getDefault().post(
                    new RefreshtEvent(refreshMode));
        } catch (ConnectTimeoutException e) {//连接超时
            e.printStackTrace();
            RefreshModel refreshMode = new RefreshModel();
            refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
            refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
            refreshMode.setMsg(e.getMessage());
            EventBus.getDefault().post(
                    new RefreshtEvent(refreshMode));
        } catch (IOException e) {
            e.printStackTrace();
            RefreshModel refreshMode = new RefreshModel();
            refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
            refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
            refreshMode.setMsg(e.getMessage());
            EventBus.getDefault().post(
                    new RefreshtEvent(refreshMode));
        } finally {
            // 关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    /**
     * POST提交 图片上传
     *
     * @param context
     * @param url
     * @param postFile
     * @return
     */
    public final static String httpClientUpload(Context context, String url, File postFile) {

            SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
            String token = shareUtil.getString("token");
            url = HttpConstant.IPAddress + url;
            // 创建默认的 HttpClient 实例
            HttpClient httpClient = null;
            // 获得https请求 httpclient
            httpClient = new DefaultHttpClient();
            httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("token", token);

            try {
                //把文件转换成流对象FileBody
                FileBody fundFileBin = new FileBody(postFile);

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                        .create();
                multipartEntityBuilder.addPart("headimg", fundFileBin);//相当于<input type="file" name="media"/>
                HttpEntity reqEntity = multipartEntityBuilder.build();
                httpPost.setEntity(reqEntity);

                HttpResponse response = httpClient.execute(httpPost);
                int requestCode = response.getStatusLine().getStatusCode();
                LogUtil.d("TEST", "url=" + url);
                LogUtil.d("TEST", "requestCode=" + requestCode);

                if (requestCode == 200) {
//                resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        String result = EntityUtils.toString(httpEntity, "UTF-8");
                        LogUtil.d("TEST", "返回结果result=" + result);
                        try{
                            JSONObject jsonObject=new JSONObject(result);
                            if(jsonObject.optInt("code")==2){
                                shareUtil.setString("phone", "");
                                shareUtil.setString("cid","");
                                shareUtil.setString("token", "");
                                shareUtil.setBoolean("IsLogin",false);
                                shareUtil.setString("phone","");
                                Intent intent=new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                return null;
                            }else{
                                return result;
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }
                    }
                }else{
                    RefreshModel refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                    refreshMode.setMsg("网络异常"+requestCode);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));
                }
            } catch (ClientProtocolException e) {
                e.getMessage();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (ConnectTimeoutException e) {//连接超时
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (IOException e) {
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            }finally {
                httpClient.getConnectionManager().shutdown();
            }

        return null;
    }

    /**
     * POST提交 多种类型上传
     *
     * @param context
     * @param url
     * @param fileList
     * @return
     */
    public final static String httpClientUploadFile(Context context, String url, Map<String ,Object> fileList) {

            SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
            String token = shareUtil.getString("token");
            url = HttpConstant.IPAddress + url;
            // 创建默认的 HttpClient 实例
            HttpClient httpClient = null;
            // 获得https请求 httpclient
            httpClient = getNewHttpClient(context);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

            HttpPost httpPost = new HttpPost(url);
//            httpPost.addHeader(HTTP.CONTENT_TYPE, "multipart/form-data");
            httpPost.addHeader("token", token);

        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                    .create();
            for (Map.Entry<String, Object> entry : fileList.entrySet()) {
                LogUtil.d("TEST", "key=" + entry.getKey()+" and value= " + entry.getValue());
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                if(entry.getKey().equals("identity_frontfile")||entry.getKey().equals("identity_reversefile")||entry.getKey().equals("cus_photofile")||entry.getKey().equals("workcardFile")||entry.getKey().equals("zhimfvideoFile")){
                    //把文件转换成流对象FileBody
                    FileBody fileBody = new FileBody((File) entry.getValue());
                    multipartEntityBuilder.addPart(entry.getKey(), fileBody);//相当于<input type="file" name="media"/>
                }else{
                    StringBody name = new StringBody((String) entry.getValue(), Charset.forName("UTF-8"));
                    multipartEntityBuilder.addPart(entry.getKey(), name);

                }
            }

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
                int requestCode = response.getStatusLine().getStatusCode();
                LogUtil.d("TEST", "url=" + url);
                LogUtil.d("TEST", "requestCode=" + requestCode);
                if (requestCode == 200) {
//                resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        String result = EntityUtils.toString(httpEntity, "UTF-8");
                        LogUtil.d("TEST", "返回结果result=" + result);
                        try{
                            JSONObject jsonObject=new JSONObject(result);
                            if(jsonObject.optInt("code")==2){
                                shareUtil.setString("phone", "");
                                shareUtil.setString("cid","");
                                shareUtil.setString("token", "");
                                shareUtil.setBoolean("IsLogin",false);
                                shareUtil.setString("phone","");
                                Intent intent=new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                return null;
                            }else{
                                return result;
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }
                    }
                } else if (requestCode == 401) {//token验证失败

                } else {
                }
            } catch (ClientProtocolException e) {
                e.getMessage();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (ConnectTimeoutException e) {//连接超时
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            } catch (IOException e) {
                e.printStackTrace();
                RefreshModel refreshMode = new RefreshModel();
                refreshMode.setActive(GlobalConfig.ACTIVE_SHOW_EXCEPTION);
                refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_SHOW_ALL);
                refreshMode.setMsg(e.getMessage());
                EventBus.getDefault().post(
                        new RefreshtEvent(refreshMode));
            }finally {
                httpClient.getConnectionManager().shutdown();
            }
        return null;
    }

    private static final int SET_CONNECTION_TIMEOUT = 20 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;

    /**
     * 创建HttpClient
     *
     * @param context
     * @return
     */
    public static HttpClient getNewHttpClient(Context context) {
        HttpClient client = null;
        try {
            KeyStore keystore = null;
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());//使用默认证书
            keystore.load(null, null);//去掉系统默认证书
//            // 注册密匙库
            SSLSocketFactory sf = new MySSLSocketFactory(keystore);
            ;
//            // 不校验域名
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params,
                    SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setTcpNoDelay(params, true);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);


            client = new DefaultHttpClient(ccm, params);

        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
        return client;
    }

    /**
     * 所有证书都通过
     */
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
