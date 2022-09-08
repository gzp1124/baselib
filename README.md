

1. 页面状态 加载中，请求失败点击重试等
    原理：把页面状态对应成 View 封装到对象中，再在 BaseVmFragment/BaseVmActivity 中动态添加到真实的布局中
    再监听页面状态的 LiveData 进行对应 View 的显示隐藏

2. ViewModel 自动请求数据问题
    查看 BaseViewModel 中 refreshTrigger，和 page 相关说明
    主要原理就是使用 LiveData 观察 这两个对象，对象有变化就进行相应的请求，从而实现自动请求


3. 提交新版本到阿里云云效 maven
   1. 修改 baselib -> build.gradle 中的版本信息
   2. AndroidStdio右侧 gradle -> baselib -> publishing -> 双击 publishReleasePublicationToMavenRepository
   3. 如果baselib 下方没有 publishing，Command+,打开设置页 - Experimental - 取消勾选 Do not build Gradle task....

4. 在其他项目中使用的方法
   1. 项目的 settings.gradle 文件中，只读密码放心用
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url 'https://maven.aliyun.com/repository/public'
        }
        maven {
            credentials {
                username '6308a60201acbf842a4d05c0'
                password ']5pr_euLTUHE'
            }
            url 'https://packages.aliyun.com/maven/repository/2124813-release-mj2kic/'
        }
        maven { url "https://jitpack.io" }
        google()
        mavenCentral()
    }
}
```
2. APP 的 gradle 文件中
```
    plugins {
        id 'com.android.application'
        id 'kotlin-android'
        id 'kotlin-kapt'
        id 'kotlin-parcelize'
        id 'maven-publish'
    }

    kapt 和 applicationId 同级，defaultConfig 中
    kapt {
        includeCompileClasspath = true
        arguments {
            arg("AROUTER_MODULE_NAME", project.getName())
        }
    }

    使用baselib
    implementation 'com.gzp1124.baselib:baselib:1.4.0'
    kapt "com.alibaba:arouter-compiler:1.5.2"
    kapt 'androidx.annotation:annotation:1.3.0'
    用到 xui 的话添加
    // XUI UI库 https://github.com/xuexiangjys/XUI/wiki
    implementation 'com.github.xuexiangjys:XUI:1.1.9'
```

5. 使用欢迎页让用户接受隐私政策，不接受隐私政策不进入APP
   因为最新的隐私政策原因，不这么做APP可能无法上线
   参考：https://juejin.cn/post/6990643611130363917
   步骤：
    1. Settings.useSplashCheckPrivacy = true
    2. Manifast 中 application 下 添加如下代码，name 固定，value 是欢迎页的全路径
       <meta-data
       android:name="com.gzp1124.check.activity"
       android:value="com.thirtydays.kelake.module.splash.view.SplashActivity" />
    3. 欢迎页 CheckApp.getApp().isUserAgree() 来判断是否接受，注意 onResume 也要判断
       查看隐私政策详情也要在欢迎页打开，不能跳转其他页面打开
    4. 点击接受：CheckApp.getApp().agree(this,false,getIntent().getExtras());
       执行完上面的代码，application 中的 initSDK() 会自动进行执行。
    5. 然后进行后续的继续操作。打开新页面之类的。

更多信息查看 说明.txt ，或者看代码


