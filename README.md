# etc
etc(Efficient tool controller)——高效工具控制库，一种减少手动加入大量代码、释放劳动力的工具库。通过注解的方式，在编译时进行处理完成相关初始化操作。通过apt和aspect两种不同的方式处理不同注解编译。接入者可以根据需要接入apt或者aspect、或者两者都同时接入。

### 背景
* 我们在处理点击事件的时候，比如点击按钮的时候发起了请求，屏幕的响应是很灵敏的，可能用户在点击的时候就连续点击了几次，那么编程的时候我们就需要防止重复请求处理，因此，一种在编译阶段，监听相关的点击事件处理函数，并进行事件防重点击就显得必要，而且也减少我们重复的处理逻辑；
* 同样的，我们经常需要进行多线程和主线程的切换，那么通过注解的方式在编译阶段就自动实现就能释放劳动力；
* 列表是需求功能经常用到的，那么上拉加载更多和下拉刷新就经常需要，但在开发过程，这种处理逻辑其实有点繁杂，那么通过注解的方式在编译阶段自动实现，也是很必要的；
* 权限申请的实现逻辑繁杂，编译统一处理；
* findViewById的琐碎都交给注解编译统一处理；
* ...

### 接入步骤
1. 根目录加上如下：
```
dependencies {
        classpath 'com.android.tools.build:gradle:3.5.3'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.8'  // aspect接入需要
        classpath 'org.aspectj:aspectjtools:1.8.13'  // aspect接入需要
    }
    
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
2. 在用到的模块中添加依赖
* 只使用apt注解编译
```
dependencies {
  annotationProcessor 'com.github.zhuozp.etc:apt:v2.0.1'
  implementation 'com.github.zhuozp.etc:apt_annotation:v2.0.1'
}

```
* 只使用aspect编译生成

```
apply plugin: 'android-aspectjx'

dependencies {
  implementation 'com.github.zhuozp.etc:etc:v2.0.1'
}

```
* 同时加上即可apt和aspect都使用，aspect编译可以在gradle进行aspect相关设置
```
aspectjx {

}
```

### 各个点使用介绍
* @ViewById 完成findById功能，apt编译
```
@ViewById 
RecyclerView recyclerView;
    
@ViewById
SwipeRefreshLayout refreshView;
```
使用ViewInject注入
```
ViewInjector.inject(this);
或
ViewInjector.inject(this, view);
```
* @DownloadMoreListener RecyclerView加载更多，支持LinearLayoutManager/GridLayoutManager/StaggeredGridLayoutManager，apt编译
```
@DownloadMoreListener(loadPosition = 5)
    void recyclerView() {
        mockData();
    }
```
* @RefreshListner 下拉刷新，SwipeRefreshLayout, apt编译
```
@RefreshListner()
@Background
void refreshView() throws InterruptedException {
        
    Thread.sleep(3000);
    stopRefresh();
}
```

* @ClickThrottle(value = 5000) 有点击事件防多次点击，aspect编译
```
textView.setOnClickListener(new View.OnClickListener() {
            @ClickThrottle(value = 5000)
            @Override
            public void onClick(View v) {
                Log.d(DoubleClickAspect.TAG, "textview1 click");
                click();
            }
        });
```

* @RequestPermissions 权限申请，aspect编译
```
@RequestPermissions({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void takPhoto(IPermissionCallback callback) {
        Toast.makeText(this, "permission is granted", Toast.LENGTH_LONG).show();
    }
```

* @Background  子线程执行，aspect编译
```
@RefreshListner()
    @Background
    void refreshView() throws InterruptedException {
        // TODO, in other thread, such as downloading res
        Thread.sleep(3000);
        stopRefresh();
    }
```

*  @UiThread 主线程执行，aspect编译
```
 @UiThread
    void stopRefresh() {
        refreshView.setRefreshing(false);
        mockHeadData();
    }
```

### 欢迎交流
          
