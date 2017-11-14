package com.ap.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 类描述：屏幕适配
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class MakeXmlUtils {

    //文件路径
    private final static String rootPath = "F:\\GitHub\\Bluetooth\\app\\src\\main\\res\\values-{0}x{1}";
    //原型设计尺寸
    private static float dwidth = 1080f;
    private static float dheight = 1920f;
    //手机dpi
    private static int dpi = 375;

    private final static String WTemplate = "    <dimen name=\"width{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "    <dimen name=\"height{0}\">{1}px</dimen>\n";

    public static void main(String[] args) {
        makeXml();
    }

    public static void makeXml() {
        /*makeString1(1280, 720);
        makeString1(1024, 768);
        makeString1(1280, 800);
        makeString1(1812, 1080);*/
        makeString1(1080, 1920);
    }

    /**
     * 数据生成，单位px
     *
     * @param w 宽度
     * @param h 高度
     */
    private static void makeString(int w, int h) {

        //width
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<resources>");
        float cellw = w / dwidth;
        for (int i = 1; i < (int) dwidth; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sb.append(WTemplate.replace("{0}", String.valueOf(dwidth)).replace("{1}", String.valueOf(w)));
        sb.append("</resources>");

        //height
        StringBuilder sb2 = new StringBuilder();
        sb2.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb2.append("<resources>");
        float cellh = h / dheight;
        for (int i = 1; i < (int) dheight; i++) {
            sb2.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sb2.append(HTemplate.replace("{0}", String.valueOf(dheight)).replace("{1}", String.valueOf(h)));
        sb2.append("</resources>");

        //文件路径
        String path = rootPath.replace("{0}", w + "").replace("{1}", h + "");
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File layxFile = new File(path + "\\dimens_x.xml");
        File layyFile = new File(path + "\\dimens_y.xml");

        //写入文件内容
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sb.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sb2.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据生成，单位px
     *
     * @param w 宽度
     * @param h 高度
     */
    private static void makeString1(int w, int h) {

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<resources>\n");
        //width
        sb.append("    <!--width-->\n");
        float cellw = w / dwidth;
        for (int i = 1; i < (int) dwidth; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sb.append(WTemplate.replace("{0}", String.valueOf((int) dwidth)).replace("{1}", String.valueOf(w)));
        sb.append("\n\n");
        //height
        sb.append("    <!--height-->\n");
        float cellh = h / dheight;
        for (int i = 1; i < (int) dheight; i++) {
            sb.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sb.append(HTemplate.replace("{0}", String.valueOf((int) dheight)).replace("{1}", String.valueOf(h)));
        sb.append("</resources>");

        //文件路径
        String path;
        if (w == (int) dwidth && h == (int) dheight) {
            path = rootPath.replace("-{0}x{1}", "");
        } else {
            path = rootPath.replace("{0}", w + "").replace("{1}", h + "");
        }
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File layxFile = new File(path + "\\dimens.xml");

        //写入文件内容
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sb.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f /** dpi / 160*/;
    }
}
