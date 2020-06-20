package jn_17201312.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IODeal {

    private IODeal() {
    }

    public static JSONObject translateToJSONObject(Object object) {
        JSONObject jsonObj = (JSONObject) JSON.toJSON(object);
        System.out.println("jsonObj" + jsonObj);
        return jsonObj;
    }

    public static boolean write(JSONObject jsonObj) {
        String str = JSONObject.toJSONString(jsonObj, true);
        String subject = toMail(jsonObj).getSubject();
        String receiver = toMail(jsonObj).getReceiver();
        File f;
        File fd = new File("Mails/" + receiver + "/");
        if (!fd.isDirectory() || !fd.exists()) {
            fd.mkdir();
        }
        f = new File("Mails/" + receiver + "/" + subject + ".json");
        if (f.exists()) {
            for (int i = 1; ; i++) {
                //新建一个文件对象，如果不存在则创建一个该文件
                f = new File("Mails/" + receiver + "/" + subject + "(" + i + ")" + ".json");
                if (!f.exists()) {
                    break;
                }
            }
        } else {
            f = new File("Mails/" + receiver + "/" + subject + ".json");
        }
        FileWriter fw;
        try {
            fw = new FileWriter(f);
            fw.write(str);//将字符串写入到指定的路径下的文件中
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 读取文件，以JSONObject对象读出
    public static JSONObject read(String path) {
        try {
            String fileStr = FileUtils.readFileToString(new File(path));
            JSONObject jsonObject = JSON.parseObject(fileStr);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 读取以JSONObject对象，解析并返回Mail对象
    public static Mail toMail(JSONObject jsonObj) {
        return JSONObject.toJavaObject(jsonObj, Mail.class);
    }

    // 从文件夹下获取所有文件

    /**
     * @param path   文件路径
     * @param suffix 文件后缀
     * @return File[] 返回一个文件夹下的符合后缀的文件数组
     */
    public static File[] getFilesFromFolder(String path, String suffix) {
        ArrayList<File> tempFiles = new ArrayList<File>();
        File[] files = new File(path).listFiles();
        File[] resFills;

        if (null != files) {
            for (File file : files) {
                if (file.getName().endsWith(suffix)) {
                    tempFiles.add(file);
                }
            }
        }

        resFills = new File[tempFiles.size()];

        for (int i = 0; i < tempFiles.size(); i++) {
            resFills[i] = tempFiles.get(i);
        }

        return resFills;
    }
}
