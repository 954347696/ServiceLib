package com.keepfun.aiservice.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.ResponseData;
import com.keepfun.blankj.util.CloseUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.HttpResponse;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.utils.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpURLConnection封装基类，网络请求，设置请求协议头、发送请求
 * <p>
 * Created by hardyshi on 16/11/22.
 */
public abstract class BaseConnection {
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";

    protected static final String HTTP_REQ_PROPERTY_CHARSET = "Accept-Charset";
    protected static final String HTTP_REQ_VALUE_CHARSET = "UTF-8";
    protected static final String HTTP_REQ_PROPERTY_CONTENT_TYPE = "Content-Type";
    protected static final String HTTP_REQ_VALUE_CONTENT_TYPE = "application/x-www-form-urlencoded";
    protected static final String HTTP_REQ_VALUE_CONTENT_JSON_TYPE = "application/json";
    protected static final String HTTP_REQ_PROPERTY_CONTENT_LENGTH = "Content-Length";
    protected static final String HTTP_REQ_METHOD_GET = "GET";
    protected static final String HTTP_REQ_METHOD_POST = "POST";
    protected static final String HTTP_REQ_COOKIE = "Cookie";

    /**
     * 建立连接的超时时间
     */
    protected static final int CONNECT_TIMEOUT = 5 * 1000;
    /**
     * 建立到资源的连接后从 input 流读入时的超时时间
     */
    protected static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

    public BaseConnection() {

    }

    private void setURLConnectionCommonPara(boolean isForm, HttpBasicRequest request) {
        HttpURLConnection connection = getURLConnection();
        if (null == connection) {
            return;
        }
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
        connection.setUseCaches(false);
        connection.setRequestProperty(HTTP_REQ_PROPERTY_CHARSET, HTTP_REQ_VALUE_CHARSET);
        if (isForm) {
            connection.setRequestProperty(HTTP_REQ_PROPERTY_CONTENT_TYPE, HTTP_REQ_VALUE_CONTENT_TYPE);
        } else {
            connection.setRequestProperty(HTTP_REQ_PROPERTY_CONTENT_TYPE, HTTP_REQ_VALUE_CONTENT_JSON_TYPE);
        }
        if (request.getHeader() != null) {
            for (String key : request.getHeader().keySet()) {
                connection.setRequestProperty(key, request.getHeader().get(key).toString());
            }
        } else {
            setHeaders(connection, request);
        }
    }

    private void setURLConnectionCookie(HashMap<String, String> cookieInfo) {
        HttpURLConnection connection = getURLConnection();
        if (null == connection) {
            return;
        }
        String cookieString = connection.getRequestProperty(HTTP_REQ_COOKIE);
        if (!TextUtils.isEmpty(cookieString)) {
            cookieString = cookieString + ";";
        } else {
            cookieString = "";
        }
        for (Map.Entry<String, String> entry : cookieInfo.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                LogUtils.e("cookie inf is bad");
            } else {
                cookieString = cookieString + entry.getKey() + HttpBasicRequest.HTTP_REQ_ENTITY_MERGE + entry.getValue() + ";";
            }
        }
        connection.setRequestProperty(HTTP_REQ_COOKIE, cookieString);
    }

    public String getResponseMessage() {

        HttpURLConnection connection = getURLConnection();
        if (null == connection) {
            return "";
        } else {
            try {
                return getURLConnection().getResponseMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public int getResponseCode() {
        HttpURLConnection connection = getURLConnection();
        if (null == connection) {
            return -1;
        } else {
            try {
                return getURLConnection().getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    protected abstract HttpURLConnection getURLConnection();

    private void setHeaders(HttpURLConnection connection, HttpBasicRequest request) {
        String token = GlobalDataHelper.getInstance().getAccessToken();
        LogUtils.e("setHeaders token : " + token);
        if (!StringUtils.isEmpty(token)) {
            connection.setRequestProperty(Arguments.TOKEN, token);
            connection.setRequestProperty(Arguments.ACCESS_TOKEN, token);
        }
        String uUid = GlobalDataHelper.getInstance().getuUid();
        LogUtils.e("setHeaders token : " + token);
        if (!StringUtils.isEmpty(uUid)) {
            connection.setRequestProperty(Arguments.UUID, uUid);
        }
        connection.setRequestProperty(Arguments.TERMINAL, String.valueOf(1));
        connection.setRequestProperty("imToken",GlobalDataHelper.getInstance().getImToken());
        LogUtils.e("setHeaders imToken : " + GlobalDataHelper.getInstance().getImToken());
    }

    public synchronized HttpResponse doRequest(boolean isForm, HttpBasicRequest request) {
        if (null == getURLConnection()) {
            return new HttpResponse(-1, "URLConnection is null");
        }
        try {
            setURLConnectionCommonPara(isForm, request);
        } catch (Exception e) {
            return new HttpResponse(-1, e.getMessage());
        }
        //检查cookie
//        if (null != request.cookieInfo && request.cookieInfo.size() > 0) {
//            setURLConnectionCookie(request.cookieInfo);
//        }
//        if (HTTP_REQ_METHOD_GET.equals(request.requestMethod)) {
//            return doGetRequest();
//        } else {
//            return doPostRequest(request.getParams());
//        }
        if (HTTP_REQ_METHOD_GET.equals(getURLConnection().getRequestMethod())) {
            return doGetRequest();
        }
        return doPostRequest(request.getParams());
    }

    protected HttpResponse doGetRequest() {
        InputStream inputStream = null;
        try {
            HttpURLConnection connection = getURLConnection();
            if (null == connection) {
                return new HttpResponse(-1, "URLConnection is null");
            }
            connection.setRequestMethod(HTTP_REQ_METHOD_GET);
            //获得服务器的响应码
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                inputStream.close();
                return new HttpResponse(HttpURLConnection.HTTP_OK, os.toString(HTTP_REQ_VALUE_CHARSET));
            } else {
                return new HttpResponse(response, connection.getResponseMessage());
            }
        } catch (javax.net.ssl.SSLHandshakeException ee) {
            LogUtils.e("javax.net.ssl.SSLPeerUnverifiedException");
            return new HttpResponse(-1, ee.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(-1, e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected HttpResponse doPostRequest(HashMap<String, Object> params) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] data;
//            if (dataBlock != null) {
//                data = JsonUtil.toJSONString(dataBlock).getBytes(HTTP_REQ_VALUE_CHARSET);
//            } else {
            String paramsStr = JsonUtil.encode(params);
            data = paramsStr.getBytes(HTTP_REQ_VALUE_CHARSET);
//            }
            HttpURLConnection connection = getURLConnection();
            if (null == connection) {
                return new HttpResponse(-1, "URLConnection is null");
            }
            connection.setRequestMethod(HTTP_REQ_METHOD_POST);
            connection.setRequestProperty(HTTP_REQ_PROPERTY_CONTENT_LENGTH, String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            outputStream = connection.getOutputStream();
            outputStream.write(data);

            //获得服务器的响应码
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                inputStream.close();
                return new HttpResponse(HttpURLConnection.HTTP_OK, os.toString(HTTP_REQ_VALUE_CHARSET));
            } else {
                return new HttpResponse(response, connection.getResponseMessage());
            }
        } catch (Exception e) {
            return new HttpResponse(-1, e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized HttpResponse uploadFile(HttpBasicRequest request) {
        if (null == getURLConnection()) {
            return new HttpResponse(-1, "URLConnection is null");
        }
        try {
            setURLConnectionCommonPara(false, request);
        } catch (Exception e) {
            return new HttpResponse(-1, e.getMessage());
        }
        //检查cookie
//        if (null != request.cookieInfo && request.cookieInfo.size() > 0) {
//            setURLConnectionCookie(request.cookieInfo);
//        }
//        if (HTTP_REQ_METHOD_GET.equals(request.requestMethod)) {
//            return doGetRequest();
//        } else {
//            return doPostRequest(request.getParams());
//        }

        return uploadFile(request.getParams());
    }

    protected HttpResponse uploadFile(Map<String, Object> params) {
        String fileFormName = (String) params.get("fileFormName");
        String filePath = (String) params.get("filePath");
        String newFileName = (String) params.get("newFileName");
        File uploadFile = new File(filePath);
        if (!uploadFile.exists()) {
            return new HttpResponse(-1, "文件不存在");
        }
        if (newFileName == null || newFileName.trim().equals("")) {
            newFileName = uploadFile.getName();
        }
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            StringBuilder sb = new StringBuilder();
            /**
             * 普通的表单数据
             */
            for (String key : params.keySet()) {
                sb.append("--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + key + "\""
                        + "\r\n");
                sb.append("\r\n");
                sb.append(params.get(key) + "\r\n");
            }
            /**
             * 上传文件的头
             */
            sb.append("--" + BOUNDARY + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + fileFormName
                    + "\"; filename=\"" + newFileName + "\"" + "\r\n");
            sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
            sb.append("\r\n");

            byte[] headerInfo = sb.toString().getBytes("UTF-8");
            byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
            System.out.println(sb.toString());
            HttpURLConnection connection = getURLConnection();
            if (null == connection) {
                return new HttpResponse(-1, "URLConnection is null");
            }
            connection.setRequestMethod(HTTP_REQ_METHOD_POST);
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            connection.setRequestProperty("Content-Length", String
                    .valueOf(headerInfo.length + uploadFile.length()
                            + endInfo.length));
            connection.setDoOutput(true);

            out = connection.getOutputStream();
            InputStream in = new FileInputStream(uploadFile);
            out.write(headerInfo);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.write(endInfo);
            in.close();
            //获得服务器的响应码
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len1;
                while ((len1 = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len1);
                }
                inputStream.close();
                return new HttpResponse(HttpURLConnection.HTTP_OK, os.toString(HTTP_REQ_VALUE_CHARSET));
            } else {
                return new HttpResponse(response, connection.getResponseMessage());
            }
        } catch (Exception e) {
            return new HttpResponse(-1, e.getMessage());
        } finally {
            CloseUtils.closeIO(out, inputStream);
        }
    }

    public static Bitmap getBitmap(String imageUrl) {
        URL url = null;
        try {
            url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("charset", "UTF-8");
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
