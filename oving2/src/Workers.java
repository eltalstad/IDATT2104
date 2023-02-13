import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Workers extends Thread {

    ArrayList<Thread> threads;
    private int thread_count;
    public ArrayList<Runnable> tasks = new ArrayList<>();
    Lock tasksLock = new ReentrantLock();
    Condition cv = tasksLock.newCondition();
    AtomicBoolean running = new AtomicBoolean(false);

    public Workers (int thread_count) {
        this.thread_count = thread_count;
        threads = new ArrayList<>();
    }

    public void start() {
        running.set(true);
        for (int i = 0; i < thread_count; i++) {
            threads.add(new Thread(() -> {
                while(running.get()) {
                    Runnable task = null;
                    tasksLock.lock();
                    while (tasks.isEmpty()) {
                        try {
                            cv.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!tasks.isEmpty()) {
                            task = tasks.remove(0);
                            break;
                        }
                    }
                    tasksLock.unlock();
                    task.run();
                }
            }));
            threads.get(i).start();
        }
    }

    public void post(final Runnable task) {
        tasksLock.lock();
        tasks.add(task);
        cv.signal();
        tasksLock.unlock();
    }

    public void postTimeout(final Runnable task, final long timeout) {
       new Thread(() -> {
           try {
               Thread.sleep(timeout);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           post(task);
       }).start();
    }

    /*
    public void postTimeout2(final Runnable task, final long timeout) {
        post(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.run();
        });
    }
    */

    public void joinThreads() {
        for (int i = 0; i < thread_count; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThreads() {
        running.set(false);
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) {
        Workers worker_threads = new Workers(4);
        Workers event_loop = new Workers(1);

        worker_threads.start();
        event_loop.start();

        worker_threads.postTimeout(() -> System.out.println("Task A"), 1000);

        worker_threads.post(() -> System.out.println("Task B"));

        event_loop.postTimeout(() -> System.out.println("Task C"), 1000);

        event_loop.post(() -> System.out.println("Task D"));

        worker_threads.joinThreads();
        event_loop.joinThreads();

        worker_threads.stopThreads();
        event_loop.stopThreads();
    }
}

