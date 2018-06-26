# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#================ Common Config =================#
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
# 混淆后类名都为小写   Aa aA
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#不做预校验的操作
-dontpreverify
# 混淆时不记录日志
-verbose
# 混淆采用的算法.
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#不混淆泛型
-keepattributes Signature
#避免混淆注解类
-dontwarn android.annotation
-keepattributes *Annotation*
#避免混淆内部类
-keepattributes InnerClasses
#保留代码行号，方便异常信息的追踪
-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# flatten package name
-flattenpackagehierarchy 'obfuscated'
#定制混淆字典
-obfuscationdictionary ../obfuscate/keywords.txt
#dump文件列出apk包内所有class的内部结构
-dump proguard/class_files.txt
#seeds.txt文件列出未混淆的类和成员
-printseeds proguard/seeds.txt
#usage.txt文件列出从apk中删除的代码
-printusage proguard/unused.txt
#mapping文件列出混淆前后的映射
-printmapping proguard/mapping.txt

#ACRA specifics
# Restore some Source file names and restore approximate line numbers in the stack traces,
# otherwise the stack traces are pretty useless
-keepattributes SourceFile,LineNumberTable
# ACRA needs "annotations" so add this...
# Note: This may already be defined in the default "proguard-android-optimize.txt"
# file in the SDK. If it is, then you don't need to duplicate it. See your
# "project.properties" file to get the path to the default "proguard-android-optimize.txt".
-keepattributes *Annotation*
# Keep all the ACRA classes
-keep class org.acra.** {*;}

#================ Base Obstacle =================#
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#support.v4/v7包不混淆
-keep class android.support.** {*;}
-keep class android.support.v4.** {*;}
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** {*;}
-keep class android.support.v7.** {*;}
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** {*;}
-dontwarn android.support.**

#保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}

#避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
    native <methods>;
}

#避免混淆自定义组件在Activity中的调用
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

#避免混淆自定义控件类的get/set方法和构造函数
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet,int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#避免混淆R内部的静态变量
-keep class **.R$* {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#不混淆Parcelable和它的实现子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#不混淆Serializable和它的实现子类、其成员变量
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

#================ Custom Obstacle =================#

#避免混淆属性动画兼容库
-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}

#避免混淆实体类
-keepattributes Signature,InnerClasses,EnclosingMethod

#================ 3rd SDK Obstacle =================#

#高德地图 - 定位
-dontwarn com.amap.api.location.**
-dontwarn com.amap.api.fence.**
-dontwarn com.autonavi.aps.amapapi.model.**
-keep class com.amap.api.location.** {*;}
-keep class com.amap.api.fence.** {*;}
-keep class com.autonavi.aps.amapapi.model.** {*;}
#高德地图 - 3D地图
-dontwarn com.amap.api.maps.**
-dontwarn com.autonavi.amap.mapcore.**
-dontwarn com.amap.api.trace.**
-keep class com.amap.api.maps.** {*;}
-keep class com.autonavi.amap.mapcore.* {*;}
-keep class com.amap.api.trace.** {*;}
#高德地图 - 搜索
-dontwarn com.amap.api.services.**
-keep class com.amap.api.services.** {*;}

#shareSDK
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class cn.sharesdk.** {*;}
-keep class com.sina.** {*;}
-keep class **.R$* {*;}
-keep class **.R {*;}
-keep class com.mob.** {*;}
-keep class android.net.http.SslError
-keep class android.webkit.** {*;}
-keep class m.framework.** {*;}

#SlindingMenu
-dontwarn com.jeremyfeinstein.slidingmenu.lib.**
-keep class com.jeremyfeinstein.slidingmenu.lib.** {*;}

# Gson
-keep class sun.misc.Unsafe {*;}
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** {*;}

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp.**

#okio
-dontwarn okio.**
-keep class okio.** {*;}

#Rxjava/RxAndroid
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class rx.** { *; }
-keep interface rx.** { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }

-dontwarn java.lang.invoke.*

-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn rx.internal.util.unsafe.**

#GreenDao/SqlCipher
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** {*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class * implements java.sql.Driver
-keep class net.sqlcipher.** {*;}
-keep class net.sqlcipher.database.** {*;}
-keep public interface net.sqlcipher.database.**

#xstream
-dontwarn com.thoughtworks.xstream.**
-keep class com.thoughtworks.xstream.** {*; }
-keep interface com.thoughtworks.xstream.** {*; }

#xmlpullparser
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** {*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#MQTT Could be obstate

#multidex ???

#log4j
-dontwarn org.apache.log4j.**
-keep class  org.apache.log4j.** {*;}

#ormlite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.j256.ormlite.field.DatabaseField *;
}
-keepattributes Signature

#slf4j
-dontwarn org.slf4j.**
-keep class org.slf4j.** {*;}

#thrift
-dontwarn org.apache.thrift.**
-keep class org.apache.thrift.** {*;}

#circleimageview
-dontwarn de.hdodenhof.circleimageview.**
-keep class de.hdodenhof.circleimageview.** {*;}

#SwipeToLoadLayout
-dontwarn com.aspsine.swipetoloadlayout.**
-keep class com.aspsine.swipetoloadlayout.** {*;}

#shadow-layout
-dontwarn com.dd.**
-keep class com.dd.** {*;}

#carouselpicker
-dontwarn in.goodiebag.carouselpicker.**
-keep class in.goodiebag.carouselpicker.** {*;}

#MPAndroidChart
-dontwarn com.github.mikephil.charting.**
-keep class com.github.mikephil.charting.** {*;}

#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** {*;}
-dontwarn com.uuzuche.lib_zxing.**
-keep class com.uuzuche.lib_zxing.** {*;}

#AndPermission
-dontwarn com.yanzhenjie.permission.**
-keep class com.yanzhenjie.permission.** {*;}

#Youth Banner
-dontwarn com.youth.banner.**
-keep class com.youth.banner.** {*;}

#MarsDaemon
-keep class com.marswin89.marsdaemon.NativeDaemonBase{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI20{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI21{*;}
-keep class com.marswin89.marsdaemon.DaemonApplication{*;}
-keep class com.marswin89.marsdaemon.DaemonClient{*;}
-keepattributes Exceptions,InnerClasses,...
-keep class com.marswin89.marsdaemon.DaemonConfigurations{*;}
-keep class com.marswin89.marsdaemon.DaemonConfigurations$*{*;}

#Field
-keep class com.chery.cloudrive3.utils.FontUtils {*;}
-keepclassmember class com.chery.cloudrive3.CrashHandler {
    public void collectDeviceInfo(android.content.Context);
}
-keep class com.chery.cloudrive3.utils.DrawableUtils {*;}

#freemarker
-dontwarn freemarker.**
-keep class freemarker.** {*;}

#javax
-keep public class javax.**

#UMeng
-keep class com.umeng.commonsdk.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

#ShortcutBadge
-keep class me.leolin.shortcutbadger.impl.AdwHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.ApexHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.AsusHomeLauncher { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.DefaultBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.NewHtcHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.NovaHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.SolidHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.SonyHomeBadger { <init>(...); }
-keep class me.leolin.shortcutbadger.impl.XiaomiHomeBadger { <init>(...); }

#SpongyCastle
-dontwarn javax.naming.**
-ignorewarnings -keep class * { public private *; }
-keep class org.spongycastle.** {*;}
