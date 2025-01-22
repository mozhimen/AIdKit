# 实现方案1——GUID方案
该方案是根据谷歌推荐的方案3）私密存储的GUID改进而来（存储改进），具体如下：

## 1.整体逻辑
第一次生成后存到SP和外部存储的uuid.txt中，下次进入先从SP获取，如果没有依次去外部存储UUID_PATH读取。

## 2.生成逻辑

### 1）Android Q之前
利用以前可以拿到的标识（如：SN\Android ID\IMEI\Device ID）去处理（如：混合等）。老的办法网上很多，此处不做介绍；如果为空，则利用randomUUID()随机生成一个。

### 2）Android Q之后
利用randomUUID()生成一个
```
uniqueId = UUID.randomUUID().toString();
```

## 3.数据存储
方式：SP + 外部存储的隐藏文件夹
注：外部存储路径：
```
UUID_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appName/.uuid/.uuid.txt";
```

## 4. 优缺点
1）优点
Q以后的获取方式简单（randomUUID()）。

2）风险
风险包括Q以前的数据获取的唯一性和整体数据存储的稳定性。

① 数据获取（Q以前）
DeviceId            （刷机会变）
ANDROID_ID     （恢复出厂 + 刷机会变）
SerialNumber    （android 9需要设备权限）
此外：ANDROID_ID还具有以下缺点

i）现在网上已有修改设备ANDROID_ID值的APP应用。
ii) 某些厂商定制系统会导致不同的设备产生相同的ANDROID_ID或返回值为null。
iii) 对于CDMA制式的设备，ANDROID_ID和DeviceId返回的值相同

② 数据存储
SP中的数据会随着app的卸载而消失；
隐藏文件夹中存储的uuid.txt存在被用户手动删除或被系统和第三方管理软件清理的风险。

## 5. 改进方向
1)数据获取——降低的重复率
vbscript 代码解读复制代码String uuid = new Date().getTime() + "_AndroidQ_" + UUID.randomUUID().toString();

       该方法拼接了写入时间的毫秒数+标志位（也可加设备信息等）+UUID的组合，以降低重复。
测试结果：uuid = 1597109073335_AndroidQ_07e0ee6f-5d6e-41c1-8dc1-725fab218b3f
或者
arduino 代码解读复制代码/生成15位唯一性的设备ID号
private static String getRandomUUID() {
    int random = (int) (Math.random() * 9 + 1);//随机生成一位整数
    String valueOf = String.valueOf(random);
    int hashCode = UUID.randomUUID().toString().hashCode();//生成uuid的hashCode值
    if (hashCode < 0) {  hashCode = -hashCode; } //可能为负数
    String value = valueOf + String.format("%014d", hashCode);
    return value;
}

      该方法将获取的随机uuid生成其hashCode值，并进行字符串格式化处理，提高其唯一性。
测试结果：value = 400001981159719
2）数据存储——存储文件名提醒
文件名（现为uuid.txt）建议使用警示语，例如System XXX、重要文件勿动之类的，降低用户手动删除的风险。
注：数据存储适配。由于权限变化，Android 11后现有存在SD卡里文件的方法需要做适配。
