-keepattributes Signature

-keep class retrofit.** { *; }
-keep class retrofit.http.** { *; }
-keep class retrofit.client.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.codehaus.mojo.** { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keep class sun.misc.Unsafe { *; }

-dontwarn javax.xml.stream.events.**
-dontwarn rx.**
-dontwarn org.apache.lang.**
-keep class com.scorpion.splashwalls.models.** { *; }
-dontwarn com.squareup.okhttp.**

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#    -dontwarn com.hoanganhtuan95ptit.autoplayvideorecyclerview.**
-keepclassmembers class * extends com.littlemango.stacklayoutmanager.StackAnimation {
  public protected <init>(...);
}
-keepclassmembers class * extends com.littlemango.stacklayoutmanager.StackLayout {
  public protected <init>(...);
}