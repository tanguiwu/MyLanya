package com.example.mylanya;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

    /**
     * pull方式解析xml
     *
     * @param xmlStr
     *            xml字符串
     * @return list
     */
    public static List<PrintInfo> getPrintInfos(String xmlStr) {
        List<PrintInfo> printInfos = null;
        PrintInfo printInfo = null;
        List<String> list = null;

        XmlPullParser parser = Xml.newPullParser();
        try {

            InputStream is = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        printInfos = new ArrayList<PrintInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Print".equals(parser.getName())) {
                            printInfo = new PrintInfo();
                            list = new ArrayList<String>();
                        } else if ("Code".equals(parser.getName())) {
                            String code = parser.nextText();
                            printInfo.setCode(code);
                        } else if ("Type".equals(parser.getName())) {
                            String type = parser.nextText();
                            printInfo.setType(type);
                        } else {
                            if (!"Text".equals(parser.getName()))
                                break;
                            String text = parser.nextText();
                            list.add(text);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (!"Print".equals(parser.getName()))
                            break;
                        printInfo.setTextlist(list);
                        printInfos.add(printInfo);
                        printInfo = null;
                        break;
                    default:
                        break;
                }

                event = parser.next();
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return printInfos;
    }
}