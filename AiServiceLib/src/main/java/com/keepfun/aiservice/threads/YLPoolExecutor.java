package com.keepfun.aiservice.threads;

import com.keepfun.blankj.util.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenchangjun on 18/6/6.
 * <p>
 * 原子性是指不可再分的最小操作指令，即单条机器指令，原子性操作任意时刻只能有一个线程，因此是线程安全的。java中基本数据类型的访问读写是原子性操作。
 * <p>
 * 可见性是指当一个线程修改了共享变量的值，其他线程可以立即得知这个修改。
 * Java中通过volatile、final和synchronized这三个关键字保证可见性：
 * 1：volatile：通过刷新变量值确保可见性。
 * 2：synchronized：同步块通过变量lock锁定前必须清空工作内存中变量值，重新从主内存中读取变量值，unlock解锁前必须把变量值同步回主内存来确保可见性。
 * 3：final：被final修饰的字段在构造器中一旦被初始化完成，并且构造器没有把this引用传递进去，那么在其他线程中就能看见final字段的值，无需同步就可以被其他线程正确访问。
 */

public class YLPoolExecutor extends ThreadPoolExecutor {

    private static final int MAX_THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    private static final int INIT_THREAD_COUNT =2;// 核心线程数设置
    private static final long SURPLUS_THREAD_LIFE = 30L;//线程空闲多久后结束


    private static YLPoolExecutor instance = getInstance();


//    private static JJPoolExecutor instance;

    /**
     * 关于如何设置参数, 这里有个明确的说明
     * https://www.cnblogs.com/waytobestcoder/p/5323130.html
     *
     * @return
     */
    public static YLPoolExecutor getInstance() {
        if (null == instance) {
            synchronized (YLPoolExecutor.class) {
                if (null == instance) {
                    instance = new YLPoolExecutor(
                            INIT_THREAD_COUNT,//为了减少开支, 让核心线程为2, 当需要的时候 重新创建线程 //当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
                            MAX_THREAD_COUNT,
                            SURPLUS_THREAD_LIFE,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new YLThreadFactory());

                    instance.allowCoreThreadTimeOut(true);//核心线程会超时关闭

                }
            }
        }
        return instance;
    }


    private YLPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                LogUtils.e("线程数量超过线程池最大容量，线程执行失败");
                //executor.execute(r);
            }
        });
    }

//    public void execute(Runnable command) {
//        new Thread(() -> YLPoolExecutor.super.execute(command)).start();
//    }

}
