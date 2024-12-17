# AIdKit
Android各种Id获取库

# Android Q获取设备唯一ID（UDID\GUID\UUID\SSAID\GAID）

## 一、简介
### 1.1 问题背景
技术现状：存储设备需要用唯一设备ID（Unique Device Identifier）来标识用户身份，匹配该用户在设备中存储的数据，以便客户增删改查。但受隐私保护、权限收紧影响，获取设备唯一ID变难。

谷歌官方：从 Android Q 开始，应用必须具有 READ_PRIVILEGED_PHONE_STATE 特许权限（普通 app 申请不了）才能访问设备的不可重置标识符（包含 IMEI号 和SN号）。Android Q中Google彻底禁止第三方app获取IMEI，且WIFI和蓝牙的MAC地址返回均为：02:00:00:00:00:00。

手机厂商：小米、VIVO、华为等厂商成立移动安全联盟推出诸如OAID来保护用户隐私。
受影响的API如下：

```
Build.getSerial();
TelephonyManager.getImei();
TelephonyManager.getMeid()
TelephonyManager.getDeviceId();
TelephonyManager.getSubscriberId();
TelephonyManager.getSimSerialNumber();
```

### 1.2 关键技术
关键技术在于数据获取（唯一性&&稳定性）和数据储存。
- （1）数据获取——设备标识码

设备标识码是用来标识设备的特征码，常见的设备标识码有以下几种：
                                              
表1 常见设备标识码及其定义
|设备码|定义|
|:--|:--|
|IMEI(International Mobile Equipment Identity)|国际移动设备识别码：15位数字组成，与每台手机一一对应，且全球唯一，是GSM设备返回的，并且是写在主板上的，重装APP不会改变IMEI|
|MEID|全球唯一的56bit CDMA制式移动终端标识号：14位数字，标识号会被烧入终端里，且不能被修改。可用来对CDMA制式（电信运营）移动式设备进行身份识别和跟踪|
|DEVICE_ID|设备ID。Android系统为开发者提供的用于标识手机设备的串号；它根据不同的手机设备返回IMEI，MEID或ESN码；它返回的是设备的真实标识（Q上无法正常获取）|
|MAC ADDRESS|媒体访问控制地址，也称局域网地址、以太网地址、物理地址：它是一个用来确认网络设备位置的地址。MAC地址用于在网络中唯一标示一个网卡，一台设备若有一或多个网卡，则每个网卡都需要并会有一个唯一的MAC地址。|
|ANDROID_ID（SSAID）|设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。当设备被wipe后该值会被重置（wipe：恢复出厂设置、刷机等)|
|SN(Serial Number)|产品序列号：是为了验证“产品的合法身份”而引入的，它是用来保障用户的正版权益，享受合法服务的；一套正版的产品只对应一组产品序列号。
|ESN (Electronic Serial Number )|美国联邦通信委员会规定的，每一台移动设备（智能手机、平板电脑等）独有的参数，其长度为32位。ESN码一开始使用于AMPS和D-AMPS手机上，当前则于CDMA手机上最为常见；|
|UUID(Universally Unique Identifier)|通用唯一标识符：一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。由以下几部分组合：当前日期和时间(第一个部分与时间有关)，时钟序列，全局唯一的IEEE机器识别号（如果有网卡，从网卡获得，没有网卡以其他方式获得）|
|GAID(Uni Identifier)|广告ID：是用户特殊、独特、可重置的id，由Google Play Service提供，它用于广告目的的匿名标示符和或者重置起标示符或者退出以利益为基础的Google Play的医用程序。|
|WIDEVINE_UUID|数字产权管理（DRM）设备 ID。DRM API 中提供一个MediaDrm类，作用是用来获取用于解密受保护的媒体流的密钥。|

- （2）数据存储

1)存储方式
SP、File、SQLite数据库、网络、ContentProvider

2)Android Q 新变化
Q以上，采用沙箱（Sandboxie）存储机制；
Q以下，采用老的文件存储方式。

沙箱存储机制：
① 访问自己沙盒中的文件无需特定权限
② 访问系统媒体文件（沙盒外的媒体共享文件，如照片\音乐\视频），需要申请新的媒体权限:READ_MEDIA_IMAGES,READ_MEDIA_VIDEO,READ_MEDIA_AUDIO, 申请方法同原来。
③ 访问系统下载文件，暂时没做限制。但要访问其他应用的文件，必须允许用户使用系统
的文件选择器应用来选择文件。
注：Android 10目前已采用以下策略暂时解决兼容，但Android 11将严格采用新的存储机制。
ini 代码解读复制代码android:requestLegacyExternalStorage="true"

## 二、解决方案

## 2.1 谷歌官方推荐方案 (4种)
        
谷歌官方给予了设备唯一ID最佳做法，但是此方法给出的ID可变。

最佳做法：

1）避免使用硬件标识符。在大多数用例中，您可以避免使用硬件标识符，例如 SSAID (Android ID)，而不会限制所需的功能。Android 10（API 级别 29）对不可重置的标识符（包括 IMEI 和序列号）添加了限制。您的应用必须是设备或个人资料所有者应用，具有特殊运营商权限或具有 READ_PRIVILEGED_PHONE_STATE 特许权限，才能访问这些标识符。

2）只针对用户剖析或广告用例使用广告 ID。在使用广告 ID 时，请始终遵循用户关于广告跟踪的选择。此外，请确保标识符无法关联到个人身份信息 (PII)，并避免桥接广告 ID 重置。

3）尽一切可能针对防欺诈支付和电话以外的所有其他用例使用实例 ID **或私密存储的 GUID。**对于绝大多数非广告用例，使用实例 ID 或 GUID 应已足够。

4）**使用适合您的用例的 API 以尽量降低隐私权风险。**使用 DRM API 保护重要内容，并使用 SafetyNet API 防止滥用行为。SafetyNet API 是能够确定设备真伪而不会招致隐私权风险的最简单方法。
       
首先对表1中的设备码进行初步分析， IMEI\MEID号、SN号、DEVICE_ID（一般情况获取手机的也就是手机的IMEI码），Android Q后已禁止获取；MAC ADDRESS在Android Q后返回常量 “02:00:00:00:00:00” ；剩余ANDROID_ID、UUID、WIDEVINE_UUID等信息可以尝试获取。
注：其实还用GAID、IDFASafetyNET、Advertising ID、Firebase InstanceID等方法，但都依赖Google Play Service。中国发行的国行手机，google地图、Play等基础App被阉割掉了，你懂的。
      
下面我们就详细看看这几个方案具体如何实现，或者有没有别的方案

此库参考:
作者：西子逍遥
链接：https://juejin.cn/post/6861590894177419277