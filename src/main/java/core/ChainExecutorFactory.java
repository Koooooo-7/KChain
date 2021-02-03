package core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class ChainExecutorFactory {
    public enum Executor {
        DEFAULT(new DefaultExecutor());

        private IExecutor executor;

        Executor(IExecutor executor) {
            this.executor = executor;
        }

        public IExecutor getExecutor() {
            return executor;
        }
    }

    private static final int CORE_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 6;
    private static final int DEFAULT_NEED_SPLIT_SIZE = 100;
    private static final ThreadPoolExecutor DEFAULT_POOL = new ThreadPoolExecutor(
            CORE_POOL_SIZE
            , MAX_POOL_SIZE
            , 0L
            , TimeUnit.MILLISECONDS
            , new LinkedBlockingQueue<>(2000)
            , new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "DEFAULT-K-CHAIN-EXECUTOR-" + threadNumber.getAndIncrement());
        }
    }, new ChainWaitRejectedExecutionHandler());


    public interface IExecutor {
        <T, G> void exec(IChain<T, G> chain, ChainContext ctx, List<T> data);
    }

    private static class DefaultExecutor implements IExecutor {

        @Override
        public <T, G> void exec(IChain<T, G> chain, ChainContext ctx, List<T> data) {

            if (data.size() < DEFAULT_NEED_SPLIT_SIZE) {

                for (T it : data) {
                    chain.test(ctx, it);
                }
                return;
            }

            CopyOnWriteArrayList<T> collection = Lists.newCopyOnWriteArrayList(data);
            int size = collection.size();
            int batchTimes = CORE_POOL_SIZE;
            int fromIndex = 0;
            int toIndex;
            int subSize = getSubSize(size);

            CountDownLatch countDownLatch = new CountDownLatch(batchTimes);
            try {
                for (int i = 0; i < batchTimes; i++) {
                    toIndex = Math.min(fromIndex + subSize, size);
                    List<T> subList = collection.subList(fromIndex, toIndex);
                    fromIndex = toIndex;
                    DEFAULT_POOL.execute(() -> {
                        try {
                            for (T it : subList) {
                                chain.test(ctx, it);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    });
                }

                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        private static int getSubSize(int size) {
            int slice = size % CORE_POOL_SIZE;
            int subSize = size / CORE_POOL_SIZE;
            if (slice == 0) {
                return subSize;
            }
            return getClosestBiggerThanSize(size, subSize, slice);

        }


        /**
         * To find a sub size which it more closer and bigger than size to make the sub list more `average` in 6 thread.
         * f.e
         * size = 1000
         * size / 6 = 166 (called it base)
         * size % 6 = 4 (called it plus)
         * if we use the 166+4 = 170 as the sub size it is okay , cuz 170*6 > 1000
         * so, how about make the `plus` smaller ?
         * so, try to calculate the base+plus/2 to get the smaller sub size.
         * 166 + 4/2 = 168 and 168*6 > 1000, so, chose 168 as the sub size.
         * and when the size>100, it gets the better performance.
         *
         * @param size data size
         * @param base the base subSize on size/6
         * @param plus the size need add to size/6
         * @return sub size
         */
        private static int getClosestBiggerThanSize(int size, int base, int plus) {
            if ((base + plus) * CORE_POOL_SIZE < size || plus == 1) {
                return base + plus * 2;
            }

            return getClosestBiggerThanSize(size, base, plus / 2);
        }

    }

    private static class ChainWaitRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            final AtomicInteger count = new AtomicInteger(1);
            while (!executor.isShutdown()) {
                LockSupport.parkNanos(count.intValue() * 100000);
                if (executor.getQueue().remainingCapacity() > 10) {
                    executor.execute(r);
                    break;
                }
                if (count.incrementAndGet() > 100) {
                    throw new RejectedExecutionException("Task " + r.toString() +
                            " rejected more than 100 times from " +
                            executor.toString());
                }

            }
        }
    }
}
