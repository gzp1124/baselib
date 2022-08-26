

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

业务数据的转换：@InverseMethod



