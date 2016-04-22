package com.janyee.bladea.Http;

import com.janyee.bladea.Tools.FileManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author kmlixh
 */
public class HttpManager {
    public static boolean Download(String httpUrl, String path) {
        boolean result;
        String savePath = path;
        File sfile = new File(savePath);
        int lgn = 0;
        long d1 = (new Date()).getTime();
        if (!sfile.exists()) {
            FileManager.PrepareDir(savePath);
            int bytesum = 0;
            int byteread = 0;
            URL url = null;
            try {
                url = new URL(httpUrl);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
                result = false;
            }

            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(60000);
                InputStream inStream = conn.getInputStream();
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1204];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    outstream.write(buffer, 0, byteread);
                }
                FileOutputStream fs = new FileOutputStream(savePath);
                fs.write(outstream.toByteArray());
                fs.flush();
                fs.close();
                inStream.close();
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = false;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            result = true;
        }
        long d2 = (new Date()).getTime();
        return result;

    }

    public static String getDataString(String pageUrl, String encoding) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(pageUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream(), encoding));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return sb.toString();
    }

    public static String postForm(Map<String, String> params,
                                  String encode, String URLS) {

        byte[] data = getRequestData(params, encode).toString().getBytes();
        try {
            URL url = new URL(URLS);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.length));
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static StringBuffer getRequestData(Map<String, String> params,
                                               String encode) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key=entry.getKey();
                String value=entry.getValue();
                if(value==null){
                    value="null";
                }
                stringBuffer.append(key).append("=")
                        .append(URLEncoder.encode(value, encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    public static void uploadFiles(List<String> filePathList, String Url) {

        try {
            String BOUNDARY = "---------" + getUUID(11);
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/8.0 (compatible; MSIE 10.0; Windows NT 8.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            int leng = filePathList.size();
            for (int i = 0; i < leng; i++) {
                String fname = filePathList.get(i);
                File file = new File(fname);
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file" + i
                        + "\";filename=\"" + file.getName() + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");

                byte[] data = sb.toString().getBytes();
                out.write(data);
                DataInputStream in = new DataInputStream(new FileInputStream(
                        file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes());
                in.close();
            }
            out.write(end_data);
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.out.println("" + e);
            e.printStackTrace();
        }
    }

    public static String formUpload(String urlStr, FormModel form) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "--------------" + getUUID(12); //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            Map<String, Object> dataMap = form.getFormArea();
            if (null != dataMap && dataMap.size() > 0) {
                Iterator iter = dataMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    Object inputValue = entry.getValue();
                    if (inputValue instanceof String) {
                        StringBuilder sb = getFormBytes(inputName, (String) inputValue, BOUNDARY);
                        out.write(sb.toString().getBytes());
                    } else {
                        ByteArrayOutputStream outputStream = getFileBytes(inputName, (File) inputValue, BOUNDARY);
                        out.write(outputStream.toByteArray());
                    }
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            throw new NTHttpException(e.getCause());
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    private static StringBuilder getFormBytes(String inputName, String inputValue, String BOUNDARY) {
        StringBuilder sb = new StringBuilder();
        if (inputValue == null) {
            return null;
        }
        sb.append("\r\n").append("--").append(BOUNDARY).append(
                "\r\n");
        sb.append("Content-Disposition: form-data; name=\""
                + inputName + "\"\r\n\r\n");
        sb.append(inputValue);
        return sb;
    }

    private static ByteArrayOutputStream getFileBytes(String inputName, File file, String BOUNDARY) throws IOException {
        if (file == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String filename = file.getName();
        String contentType = null;
        if (filename.endsWith(".png")) {
            contentType = "image/png";
        }
        if (contentType == null || contentType.equals("")) {
            contentType = "application/octet-stream";
        }

        StringBuilder strBuf = new StringBuilder();
        strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                "\r\n");
        strBuf.append("Content-Disposition: form-data; name=\""
                + inputName + "\"; filename=\"" + filename
                + "\"\r\n");
        strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

        out.write(strBuf.toString().getBytes());

        DataInputStream in = new DataInputStream(
                new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        return out;
    }

    private static String getUUID(int length) {
        String uuids = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        if (length <= 32) {
            return uuids.substring(0, length);
        } else {
            return uuids;
        }
    }
}