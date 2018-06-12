# AndroidPerformance
Android性能帧率监控工具
## 使用方法
gradle:
```
compile "com.xfy.androidperformance:aplib:1.0.0"
```
开始监控：参考[MainActivity](sample/src/main/java/com/xfy/sample/MainActivity.java)
```
new FPSMoniterBuilder(context, WindowPermissionAdapter)
    .setAutoStartWhenShakePhone(true)   //关闭后，摇动手机自动开启面板
    .setUpdateDelay(0)                  //刷新面板延迟
    .setMaxFPSDataSize(128)             //设置帧率缓存最大数量
    .build(true);                       //true: 显示面板

//关闭摇晃手机监控
ShakeManager.release();
```
## 最后
迎提出意见及建议。

email: s18810577589@sina.com