package com.janyee.bladea.Tools;

/**
 * Created by kmlixh on 2014/8/26.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Random;


/**
 * @author nuatar
 *         文件操作类，内容还不完善
 */
public class FileTools {
    public static String IsExist = "dw23d34";
    public static String IsNosExist = "32dsd34";
    public static String OK = "3er432e3";
    public static String WRONG = "sds2323";
    public static String CANCEL = "sd2323dsd";

    public static String PrepareDir(String savePath) {
        File file = new File(savePath.substring(0, savePath.lastIndexOf('/')));
        if (!file.exists()) {
            return file.mkdirs() ? FileTools.OK : FileTools.WRONG;
        } else {
            return FileTools.IsExist;
        }
    }

    public static StringBuffer getString(InputStream inputStream, int start, int length, String encoding) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, encoding);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static String CopyTo(File f1, File f2, int bufferLength) {
        try {
            int length = bufferLength;
            FileInputStream in = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            int i = 0;
            while (true) {
                if (inC.position() == inC.size()) {
                    inC.close();
                    outC.close();
                    return FileTools.OK;
                }
                if ((inC.size() - inC.position()) < 209715200) {
                    length = (int) (inC.size() - inC.position());
                } else {
                    length = bufferLength;
                }
                inC.transferTo(inC.position(), length, outC);
                inC.position(inC.position() + length);
                i++;
            }
        } catch (Exception e) {
            return FileTools.WRONG;
        }
    }

    public static boolean IsFileExist(String filePath) {
        return new File(filePath).exists();
    }

    public static void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void fileChannelCopy(InputStream iss, File t) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = (FileInputStream) iss;
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param path 要删除的文件、文件夹路径
     * @return true on Success
     */
    public static boolean delete(String path){
        File dir=new File(path);
        if(dir.exists()&&dir.isDirectory()){
            if(dir.list()!=null&&dir.list().length>0){
                for(File temp:dir.listFiles()){
                    if(!temp.isFile()){
                        delete(temp.getAbsolutePath());
                    }
                    temp.delete();
                }
            }
        }
        dir.delete();
        return true;
    }
    public static boolean isSameFile(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(path2);
        if (file1.exists() && file2.exists() && file1.length() == file2.length()) {
            int length = (int) file1.length();
            try {
                boolean result = true;
                RandomAccessFile rf1 = new RandomAccessFile(file1, "r");
                RandomAccessFile rf2 = new RandomAccessFile(file2, "r");
                Random random = new Random(System.currentTimeMillis());
                for (int i = 0; i < 10; i++) {
                    int index = Math.abs(random.nextInt(length));
                    rf1.seek(index);
                    rf2.seek(index);
                    if (rf1.readByte() != rf2.readByte()) {
                        result = false;
                        break;
                    }
                }
                return result;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}