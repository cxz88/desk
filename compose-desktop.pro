-keep class org.sqlite.** { *; }
-ignorewarnings
-keep class kotlinx.coroutines.** {*;}
-keep class org.jetbrains.skia.** {*;}
-keep class org.jetbrains.skiko.** {*;}
-keep class io.ktor.** {*;}
-keep class com.sun.javafx.** {*;}
-keep class com.chenxinzhi.model.** {*;}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-keep public class MainKt {
    public void main();
}