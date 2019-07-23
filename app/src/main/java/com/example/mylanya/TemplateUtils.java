package com.example.mylanya;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.com.wewin.extapi.model.BarcodeBlock;
import cn.com.wewin.extapi.model.Block;
import cn.com.wewin.extapi.model.GraphicBlock;
import cn.com.wewin.extapi.model.Label;
import cn.com.wewin.extapi.model.LineBlock;
import cn.com.wewin.extapi.model.QrcodeBlock;
import cn.com.wewin.extapi.model.RectBlock;
import cn.com.wewin.extapi.model.TextBlock;
import cn.com.wewin.extapi.toast.ToastUtils;
import cn.com.wewin.extapi.universal.ArrayUtils;
import cn.com.wewin.extapi.universal.WwBoundDeviceUtils;
import cn.com.wewin.extapi.universal.WwCommon;

public class TemplateUtils {
    /**
     * 创建Label对象数组，进行打印内容初始化(所有位置，大小均是毫米为单位)
     *
     * @param context
     *            当需要读取本地资源时，context不能为null
     * @param xml
     * @return
     */
    public static List<Label> initLabels(Context context, String xml) {

        List<PrintInfo> printInfos = XmlUtil.getPrintInfos(xml);
        // 初始化标签数组
        List<Label> labels = new ArrayList<Label>();

        if (null == printInfos) {
            ToastUtils.show("xml 解析错误");
            return labels;
        }

        for (PrintInfo printInfo : printInfos) {

            if (printInfo == null)
                continue;

            String Type = printInfo.getType();
            String Code = printInfo.getCode();
            if (Type.equals("1001")) {
                if (!ArrayUtils.testArray(printInfo.getTextlist(), 2)) {
                    continue;
                }

                // 单张标签初始化
                Label label = new Label();
                label.labelWidth = 45;
                label.labelHeight = 30;
                label.rfid = "abcdef123456";
                label.previewGraphicName = "width.png";

                if (WwBoundDeviceUtils.getWwBoundDevice().getDeviceType() == WwCommon.DeviceType.p30)
                    label.oritention = WwCommon.Oritention.Oritention90;
                else if (WwBoundDeviceUtils.getWwBoundDevice().getDeviceType() == WwCommon.DeviceType.p50)
                    label.oritention = WwCommon.Oritention.Oritention0;

                // 文本
                TextBlock txtBlock = new TextBlock();
                txtBlock.x = 2;
                txtBlock.y = 10;
                txtBlock.content = printInfo.getTextlist().get(0);
                txtBlock.fontSize = 3;
                txtBlock.needResize = false;
                txtBlock.oritention = WwCommon.Oritention.Oritention0;

                // 二维码
                QrcodeBlock qrcodeBlock = new QrcodeBlock();
                qrcodeBlock.x = 2;
                qrcodeBlock.y = 15;
                qrcodeBlock.content = Code;
                qrcodeBlock.width = 13;

                // 条码
                BarcodeBlock barcodeBlock = new BarcodeBlock();
                barcodeBlock.x = 5;
                barcodeBlock.y = 5;
                barcodeBlock.content = printInfo.getTextlist().get(1);
                barcodeBlock.width = 25;
                barcodeBlock.height = 5;

                // 图片
                GraphicBlock graphicBlock = new GraphicBlock(context);
                graphicBlock.x = 1;
                graphicBlock.y = 1;
                graphicBlock.width = 10;
                graphicBlock.height = 10;
                graphicBlock.graphic = "test.png";// 可传入String
                // path或者用户自定义bitmap图片

                // 线条
                LineBlock lineBlock = new LineBlock();
                lineBlock.x = 5;
                lineBlock.y = 5;
                lineBlock.width = 10;
                lineBlock.height = 0.5f;

                // 矩形
                RectBlock rectBlock = new RectBlock();
                rectBlock.x = 5;
                rectBlock.y = 5;
                rectBlock.thinkness = 0.5f;
                rectBlock.width = 40;
                rectBlock.height = 12;

                // 追加到lable里面
                Block blocks[] = { txtBlock, graphicBlock };
                label.blocks = blocks;

                // 添加一张标签
                labels.add(label);
            }
        }
        return labels;
    }
}
