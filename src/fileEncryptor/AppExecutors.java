
package fileEncryptor;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private Executor singleThread;

    private Executor ThreeThread;


    private AppExecutors(Executor singleThread, Executor ThreeThread) {
        this.singleThread = singleThread;
        this.ThreeThread = ThreeThread;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT));
    }

    private static class AppExecutorHolder{
        private static final AppExecutors INSTANCE = new AppExecutors();
    }

    public static AppExecutors getInstance(){
        return AppExecutorHolder.INSTANCE;
    }

    public Executor singleThread() {
        return singleThread;
    }

    public Executor ThreeThread() {
        return ThreeThread;
    }


    public interface NormalCallback<T>{
        void onFinish(T backData);
    }
}
