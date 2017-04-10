-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

-keep class com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-keep class org.json.JSONStringer { *; }
-dontwarn org.json.**

-keep class com.jjoe64.graphview.** { *;}
-dontwarn com.jjoe64.graphview.**


-keepattributes Signature
-keepclassmembers class * extends com.onegravity.rteditor.spans.RTSpan {
    public <init>(int);
}

# EventBus see: http://greenrobot.org/eventbus/documentation/proguard/
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
