package com.example.mylanya;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wewin.wewinprinter_api.wewinPrinterOperateAPI;
import com.wewin.wewinprinter_connect.bluetooth.BluetoothSearchHelper;
import com.wewin.wewinprinter_connect.wifi.WifiSearchHelper;

import java.util.List;

import cn.com.wewin.extapi.imp.IPrintLabelCallback;
import cn.com.wewin.extapi.imp.IPrintPieceLabelCallback;
import cn.com.wewin.extapi.imp.IPrintSetCallback;
import cn.com.wewin.extapi.universal.WwCommon;
import cn.com.wewin.extapi.universal.WwCommon.PrintResult;
import cn.com.wewin.extapi.universal.WwPrintUtils;

public class MainActivity extends AppCompatActivity implements IPrintPieceLabelCallback {


    private Button syncPrintBtn, asyncPrintBtn, previewPrintBtn,
            asyncNoDialogPrintBtn, singleSetDarnessBtn,
            singleSetCutOptionBtn,searchBluetooth,searchWifi,connectBluetooth,connectWifi,printSuccess;

    private BluetoothDevice bluetoothDevice;
    private ScanResult scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Data><Print><Type>1001</Type><Code>DEV-240-03-01-001-00201</Code></Print><Print><Type>1001</Type><Code>DEV-240-03-01-001-00203</Code></Print></Data>";

        // 同步打印
        syncPrintBtn = (Button) findViewById(R.id.syncPrintBtn);
        syncPrintBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View btnView) {
                // 同步打印
                PrintResult printResult = WwPrintUtils.getInstance(
                        MainActivity.this).syncPrint(
                        TemplateUtils.initLabels(null, xml));
                Log.d("tgw1",printResult.getValue()+"");
            }
        });

        // 异步打印-带进度框，回调有UI操作，请使用Handler
        asyncPrintBtn = (Button) findViewById(R.id.asyncPrintBtn);
        asyncPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                // 异步打印
                WwPrintUtils.getInstance(MainActivity.this).asyncPrint(
                        TemplateUtils.initLabels(MainActivity.this, xml),
                        new IPrintLabelCallback() {

                            @Override
                            public void OnPrintSuccessEvent() {
                                Log.d("tgw2","打印成功");
                            }

                            @Override
                            public void OnPrintErrorEvent(PrintResult errorType) {
                                Log.d("tgw2","打印失败"+errorType.getValue());
                            }
                        });
            }
        });

        // 异步打印-不带进度框，回调有UI操作，请使用Handler
        asyncNoDialogPrintBtn = (Button) findViewById(R.id.asyncNoDialogPrintBtn);
        asyncNoDialogPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                // 异步打印-不带进度框
                WwPrintUtils.getInstance(null).asyncPrint(
                        TemplateUtils.initLabels(MainActivity.this, xml),
                        new IPrintLabelCallback() {

                            @Override
                            public void OnPrintSuccessEvent() {
                                Log.d("tgw异步打印-不带进度框","打印成功");
                            }

                            @Override
                            public void OnPrintErrorEvent(PrintResult errorType) {
                                Log.d("tgw异步打印-不带进度框","打印失败"+errorType.getValue());
                            }
                        });
            }
        });

        // 异步打印-带预览
        previewPrintBtn = (Button) findViewById(R.id.previewPrintBtn);
        previewPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WwPrintUtils.getInstance(MainActivity.this).previewPrint(
                        TemplateUtils.initLabels(MainActivity.this, xml),
                        new IPrintLabelCallback() {
                            @Override
                            public void OnPrintSuccessEvent() {
                                Log.d("tgw4","打印成功");
                            }

                            @Override
                            public void OnPrintErrorEvent(PrintResult errorType) {
                                Log.d("tgw4","打印失败"+errorType.getValue());
                            }
                        });
            }
        });

        // 单独设置黑度(P50、P30、P1200、H50)
        singleSetDarnessBtn = (Button) findViewById(R.id.singleSetDarnessBtn);
        singleSetDarnessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WwPrintUtils.getInstance(MainActivity.this).singleSetDarkness(
                        10, new IPrintSetCallback() {
                            @Override
                            public void OnSetResultEvent(WwCommon.SetResult result) {
                                Log.d("tgw5","单独设置黑度"+result.getValue());
                            }
                        });
            }
        });

        // P50 单独设置切刀
        singleSetCutOptionBtn = (Button) findViewById(R.id.singleSetCutOptionBtn);
        singleSetCutOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WwPrintUtils.getInstance(MainActivity.this).singleSetCutOption(
                        WwCommon.CutOption.Option1, new IPrintSetCallback() {
                            @Override
                            public void OnSetResultEvent(WwCommon.SetResult result) {
                                Log.d("tgw6","单独设置切刀"+result.getValue());
                            }
                        });
            }
        });


        searchBluetooth = (Button)findViewById(R.id.search_bluetooth);

        searchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WwPrintUtils.getInstance(MainActivity.this).searchDevice(new BluetoothSearchHelper.SearchBluetoothListener() {
                    @Override
                    public void onSearchBluetoothFailed(BluetoothSearchHelper.BluetoothErrorType bluetoothErrorType) {
                        Log.d("tgw7","蓝牙搜索失败"+bluetoothErrorType);
                    }

                    @Override
                    public void onSearchBluetoothSuccess(List<BluetoothDevice> list) {
                        Log.d("tgw7","蓝牙搜索成功");
                        bluetoothDevice= list.get(0);
                        Log.d("tgw7",list.get(0).getName());
                    }

                    @Override
                    public void onSearchBluetoothOver(List<BluetoothDevice> list) {
                        for(int i=0;i<list.size();i++){
                            Log.d("tgw7",list.get(i).getName());
                            if(list.get(i).getName().equals("P5018J1592")){
                                WwPrintUtils.getInstance(MainActivity.this).connectDevice(wewinPrinterOperateAPI.wewinPrinterOperatePrinterType.bluetooth,list.get(i), new wewinPrinterOperateAPI.IPrinterConnectionInterface() {
                                    @Override
                                    public void OnPrinterConnectionChangeListener(boolean b, Object o) {

//                                        if(true==b){
                                            Log.d("tgw7","蓝牙连接成功"+o.toString());
//                                            bluetoothDevice = (BluetoothDevice) o;
//                                        }
                                    }

                                    @Override
                                    public void OnPrinterDisconnectChangeListener(wewinPrinterOperateAPI.PrinterDisconnectReason printerDisconnectReason) {
                                        Log.d("tgw7","蓝牙连接失败原因："+printerDisconnectReason);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        searchWifi = (Button)findViewById(R.id.search_wifi);
        searchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WwPrintUtils.getInstance(MainActivity.this).searchDevice(new WifiSearchHelper.SearchWifiListener() {
                    @Override
                    public void onSearchWifiFailed(WifiSearchHelper.ErrorType errorType) {
                        Log.d("tgw8","wifi搜索失败"+errorType);
                    }

                    @Override
                    public void onSearchWifiSuccess(List<ScanResult> list) {
                        Log.d("tgw8","wifi搜索成功");
                        Log.d("tgw8",list.get(0).SSID);
                    }

                    @Override
                    public void onSearchWifiOver(List<ScanResult> list) {
                        for(int i=0;i<list.size();i++){
                            Log.d("tgw8",list.get(i).SSID);
                            if(list.get(i).SSID.equals("P5018J1592")){
                                WwPrintUtils.getInstance(MainActivity.this).connectDevice(wewinPrinterOperateAPI.wewinPrinterOperatePrinterType.wifi,list.get(i), new wewinPrinterOperateAPI.IPrinterConnectionInterface() {
                                    @Override
                                    public void OnPrinterConnectionChangeListener(boolean b, Object o) {
                                        if(true==b){
                                            Log.d("tgw8","wifi连接成功"+o.toString());
                                            scanResult = (ScanResult) o;
                                        }
                                    }

                                    @Override
                                    public void OnPrinterDisconnectChangeListener(wewinPrinterOperateAPI.PrinterDisconnectReason printerDisconnectReason) {
                                        Log.d("tgw8","wifi连接失败原因"+printerDisconnectReason);
                                         }
                                });
                            }
                        }
                    }
                });
            }
        });

        connectBluetooth = (Button)findViewById(R.id.connect_bluetooth);
        connectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接蓝牙bluetoothDevice为BluetoothDevice
                WwPrintUtils.getInstance(MainActivity.this).connectDevice(wewinPrinterOperateAPI.wewinPrinterOperatePrinterType.bluetooth,bluetoothDevice, new wewinPrinterOperateAPI.IPrinterConnectionInterface() {
                    @Override
                    public void OnPrinterConnectionChangeListener(boolean b, Object o) {
                        Log.d("tgw91","蓝牙状态改变");
                        if(true==b){
                            Log.d("tgw9","蓝牙连接成功");
                        }
                    }

                    @Override
                    public void OnPrinterDisconnectChangeListener(wewinPrinterOperateAPI.PrinterDisconnectReason printerDisconnectReason) {
                        Log.d("tgw9","蓝牙连接成功连接失败，原因："+printerDisconnectReason);
                    }
                });

            }
        });


        connectWifi = (Button)findViewById(R.id.connect_wifi);
        connectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //连接WiFi需要权限 ：<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

                WwPrintUtils.getInstance(MainActivity.this).connectDevice(wewinPrinterOperateAPI.wewinPrinterOperatePrinterType.wifi,scanResult, new wewinPrinterOperateAPI.IPrinterConnectionInterface() {
                    @Override
                    public void OnPrinterConnectionChangeListener(boolean b, Object o) {
                        Log.d("tgw101","wifi状态改变");
                        if(true==b){
                            Log.d("tgw10","wifi连接成功");
                        }
                    }

                    @Override
                    public void OnPrinterDisconnectChangeListener(wewinPrinterOperateAPI.PrinterDisconnectReason printerDisconnectReason) {
                        Log.d("tgw10","蓝牙连接成功连接失败，原因："+printerDisconnectReason);
                    }
                });
            }
        });

        printSuccess =(Button) findViewById(R.id.print_success);
        printSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WwPrintUtils.getInstance(MainActivity.this).setiPrintPieceLabelCallback(new IPrintPieceLabelCallback() {
                    @Override
                    public void OnPrintPieceSuccessEvent(int index) {
                        Log.d("tgw11", "OnPrintPieceSuccessEvent: 打印成功"+index);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 断开连接
        WwPrintUtils.getInstance(MainActivity.this).closeConnection();
    }

    @Override
    public void OnPrintPieceSuccessEvent(int i) {

    }
}
