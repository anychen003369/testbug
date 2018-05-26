package com.testbug;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBug {
    public static void main(String[] args) {
        String path = "https://api.github.com/repos/anychen003369/testbug/issues/4/comments";

        String path0 = "https://github.com/login/oauth/authorize?" +
                "client_id=5f34db2eb5ec624df912" +
                "&scope=public_repo" +
                "&redirect_uri=http://www.baidu.com";

        String path1 = "https://github.com/login/oauth/access_token?" +
                "client_id=5f34db2eb5ec624df912" +
                "&redirect_uri=http://www.baidu.com" +
                "&code=778c90dae846fbb0a3a5" +
                "&client_secret=6ef4e517ac8c405de784680f8f1da4ec6767fcf2";
        String path2 = "https://api.github.com/user?" +
                "access_token=893507055e819535aebe07fc4fd4e7d1a87b063c";

        String s = sendGet(path0);
        System.out.println(s);

    }

    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "token 893507055e819535aebe07fc4fd4e7d1a87b063c");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String postDownloadJson(String path, HashMap<String, Object> postDataParams) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
//            BASE64Encoder enc = new sun.misc.BASE64Encoder();
//            String userpassword = "anychen003369" + ":" + "anychen003369**";
//            String encodedAuthorization = enc.encode(userpassword.getBytes());
//            httpURLConnection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            httpURLConnection.setRequestProperty("Authorization", "token " + "0bda479aa52fa7326c7bd028f9d5862301993811");
//            httpURLConnection.setRequestProperty("Accept", "application/vnd.github.golden-comet-preview+json");
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            System.out.println(bos.toString("utf-8"));
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void httpUrlConnectionPost(String path, String post) {
        {
            try {
                //创建URL对象
                URL url = new URL(path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);

                httpURLConnection.setRequestProperty("Accept", "application/vnd.github.symmetra-preview+json");
                BASE64Encoder enc = new sun.misc.BASE64Encoder();
                String userpassword = "anychen003369" + ":" + "anychen003369**";
                String encodedAuthorization = enc.encode(userpassword.getBytes());
                httpURLConnection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(post.getBytes(), 0, post.getBytes().length);
                outputStream.close();
                //读取返回的数据
                //返回打开连接读取的输入流，输入流转化为StringBuffer类型，这一套流程要记住，常用
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    line = new String(line.getBytes("UTF-8"));
                    stringBuffer.append(line);
                }
                System.out.println(stringBuffer.toString());
                bufferedReader.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getPostDataString(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

}
