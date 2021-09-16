package com.thirtydays.baselibdev.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.aligit.base.ext.startCommonFragment
import com.aligit.base.ext.tool.log
import com.aligit.base.ext.view.click
import com.aligit.base.framework.mvvm.scope.VMScope
import com.aligit.base.ui.fragment.BaseVmFragment
import com.thirtydays.baselibdev.R
import com.thirtydays.baselibdev.databinding.FragmentTestErrorDataBinding
import com.thirtydays.baselibdev.vm.TestViewModel

/**
 * 测试 livedata 的数据倒灌页面
 * 页面说明：
 *  MainActivity : 主页面，没实际意义，打开 TestErrorDataFragment
 *  TestErrorDataFragment：监听 ViewModel 中的对象，并进行相应的操作
 *      openNewPage：监听该对象，打开新的页面，点击按钮会设置该对象的值，从而正常打开页面
 *      在 MainActivity 中定义 @VMScope("TestErrorData") lateinit var testViewModel: TestViewModel
 *      并在 TestErrorDataFragment 和 TestErrorDataEditFragment 页面中定义同样的 ViewModel 对象
 *      业务上理解，相当于这三个页面要进行数据共享，所以使用 vmscope 共享了 viewmodel
 *
 * 复现 livedata 的数据倒灌的步骤如下：
 *      1. 在 Mainactivity 中打开 TestErrorDataFragment
 *      2. TestErrorDataFragment 中点击按钮打开 TestErrorDataEditFragment 页面 （监听 ViewModel 中的 openNewPage 从而打开新页面）
 *      3. TestErrorDataEditFragment 中点击按钮，给 ViewModel 中的字符串内容设置新值
 *      4. 返回到 TestErrorDataFragment 可以看到页面上显示的字符串被修改成 TestErrorDataEditFragment 中刚设置的值 （到这里可以理解为操作正确，我们就是希望在编辑页修改的值自动同步到当前页）
 *      5. 返回到 MainActivity （到这里也正常的）
 *      6. 在 MainActivity 打开 TestErrorDataFragment，发现 页面自动又打开了 TestErrorDataEditFragment 页面 （这里就出错了）
 *          上面第6部就出现了数据倒灌，打开 TestErrorDataFragment 的时候不应该把 TestErrorDataEditFragment 打开啊
 *          正常应该是点击按钮修改 openNewPage 的值，然后我们监听 openNewPage 的值的变化，再打开页面啊
 *          难道刚打开 TestErrorDataFragment 的时候 openNewPage 的值就发生了变化了？
 *              其实不是 openNewPage 的值发生了变化。而是上一次 openNewPage 的变化倒灌到了这一次。
 *              简单说就是从我们的直观理解上上一次点击的 openNewPage 变化已经被使用了，这一次打开页面就不应该再有影响了。
 *          真正导致倒灌发生的原因是，每一次 livedata 的对象发生变化，那么该 livedata 内部有个 version 对象就 +1，相当于 livedata 的版本号 +1
 *          不管任何时候，观察 livedata 对象的时候，livedata 内部会进行版本校验，如果你当前页面的版本和 livedata 的版本不一致，livedata 就会自动给你发生一次最新的数据
 *          而每次页面新打开的时候可以理解为对应的 livedata 版本是 0，而 实际 livedata 对象的版本 因为上一次已经有了版本号+1，所以版本不一致就会给当前页面发生数据。
 *
 * 数据倒灌的解决方案：
 *      参考：https://juejin.cn/post/6986895858239275015
 *      1. 使用 UnPeek-LiveData：https://github.com/KunMinX/UnPeek-LiveData
 *      2. 使用反射修改 livedata 的 mVersion 从而保证版本一直（美团的方案，具体参考：https://tech.meituan.com/2018/07/26/android-livedatabus.html）
 *      3. 官方扩展的SingleLiveEvent：https://github.com/android/architecture-samples/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java
 *
 * 什么时候要防止数据倒灌？
 *      单次的 livedata 必须要防止数据倒灌。
 *      比如 按钮点击的 livedata ，一定要防止数据倒灌（点击按钮修改 livedata 的值，其他地方观察 livedata 从而进行下一步操作，这时候一定要防止倒灌）
 *
 * 什么时候就是要数据倒灌？
 *      共享的数据可以倒灌
 *      比如 用户个人信息，全局任何地方使用，都应该保证是最新的，所以倒灌也不影响，倒灌反而实现了数据的统一
 *
 * 一句话总结：
 *      单次的 livedata 防止倒灌，共享的数据可以倒灌
 *      事件类型的 livedata 防止倒灌，数据类型的 livedata 结合业务考虑
 *
 * 具体项目中使用 UnPeekLiveData ，还是 MutableLiveData  ？
 *      一般使用 UnPeekLiveData 就足够了，防止了数据倒灌，同时保证了数据是最新的。
 *      除非想要在页面重建时 livedata 有新数据，需要触发 observe ，再使用 MutableLiveData（但是这个 observe 会重复触发，把页面关闭再次打开还会触发，除非 ViewModel 也被回收）
 *      或者 UnPeekLiveData 发现了其他 bug 时使用 MutableLiveData
 *
 * 注意：
 *      UnPeekLiveData 防止数据倒灌，只是再页面重建后不会从新执行 observe 中的代码，如果在布局中获取 livedata 的值，取到的还是最新的值
 *      可以把 TestViewModel.testData 使用 UnPeekLiveData 和 MutableLiveData ，来观察 log 的打印
 */
@Route(path = "/test/errordata")
class TestErrorDataFragment:BaseVmFragment<FragmentTestErrorDataBinding>(R.layout.fragment_test_error_data) {

    @VMScope("TestErrorData") lateinit var testViewModel: TestViewModel

    override fun requestData() {
    }

    override fun onInitDataBinding() {
        mDataBinding.vm = testViewModel
        mDataBinding.openPageBtn.text = "打开编辑页面"

        // 观察 ViewModel 中的 openNewPage ，如果有新值，就打开编辑页
        testViewModel.openNewPage.observe(this){
            startCommonFragment("/test/errordata_edit")
        }

        testViewModel.testData.observe(this){
            log("test content 有了新的值了"+it)
        }

        mDataBinding.openPageBtn.click {
            // 点击打开新页面的按钮，给 ViewModel 中的 openNewPage 设置新值
            // openNewPage 设置新值后，上面的观察代码就自动执行，打开新的页面
            testViewModel.openNewPage.postValue(true)
        }
    }
}