# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:/android-sdk/tools/proguard/proguard-android.txt
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

# Google Play in App Billing
-keep class com.android.vending.billing.**

# AMAZON App Store in App Billing
-dontwarn com.amazon.**
-keep class com.amazon.** {*;}
-keepattributes *Annotation*
-dontoptimize

# SAMSUNG App Store in App Billing
-keep class com.sec.android.iap.**