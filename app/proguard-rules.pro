# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/zeeshan/Android/Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 3
-overloadaggressively
-repackageclasses ''
-allowaccessmodification


-dontwarn com.immersion.**
-dontnote com.immersion.**
-dontwarn com.androidquery.**
-dontwarn com.google.android**
-dontwarn com.squareup.picasso.**
-dontwarn uk.co.senab.**
-dontwarn retrofit2.**
-dontwarn org.apache.commons.collections.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class com.opentok.** { *; }
-keep class org.webrtc.** { *; }

-keep class com.google.**
-dontwarn com.google.**
-keep class com.google.** { *; }

-keep class com.android.support.**
-dontwarn com.android.support.**
-keep class com.android.support.** { *; }


-keepattributes *Annotation*

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class retrofit.converter.gson.** { *; }
-keepclassmembers class retrofit.converter.gson.** {
   *;}
-dontwarn com.immersion.**
-dontnote com.immersion.**
-dontwarn com.parse.**
-dontwarn org.hamcrest.**
-dontwarn ch.lambdaj.**
-dontwarn com.androidquery.**
-dontwarn com.google.android**
-dontwarn com.squareup.**
-dontwarn com.squareup.picasso.**
-dontwarn org.apache.commons.collections.**
-dontwarn com.squareup.okhttp.**

-keep class com.google.**
-dontwarn com.google.**
-keep class com.google.** { *; }

-keep class com.android.support.**
-dontwarn com.android.support.**
-keep class com.android.support.** { *; }

-keep class com.balysv.**
-dontwarn com.balysv.**
-keep class com.balysv.** { *; }

-keepattributes EnclosingMethod
-keepattributes *Annotation*

-dontwarn com.tkurimura.**

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class retrofit.converter.gson.** { *; }
-keepclassmembers class retrofit.converter.gson.** {
   *;
}

-keepclassmembers class com.opentok.** {
   *;
}

-keepattributes *Annotation*
-keepattributes Signature
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn com.oppwa.**
-dontwarn com.opentok.**


-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit.client.ApacheClient$GenericEntityHttpRequest
-dontwarn retrofit.client.ApacheClient$GenericHttpRequest
-dontwarn retrofit.client.ApacheClient$TypedOutputEntity

##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''


-keep public class * extends android.app.Activity
-keep public class * extends android.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.database.sqlite.SQLiteOpenHelper
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

-keepattributes *Annotation*

-keep class com.devicebee.tryapply.** {
*;
}

-keepclassmembers class * {
   private boolean *(android.database.sqlite.SQLiteDatabase);
}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry


#Android
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v13.app.** { *; }
-keep interface android.support.v13.app.** { *; }

-keep class com.devicebee.tryapply.** { *; }

#------------------------For Marshlling & Parcel Error ----------------------------#

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}