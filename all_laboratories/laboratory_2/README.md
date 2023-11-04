# What is this?

Here you can see a single file [SpawnableWorkers.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_2/SpawnableWorkers.java) which demonstrates how to deal with thread interruptions.

# Goal of this laboratory

The goal of this laboratory is to show how to deal with threads interruptions and how to avoid an **active thread waiting** (i.e. how to use **join()** method).

# SpawnableWorkers description

This code dynamically creates **Threads** which are stored in some **List**. Every created thread is numbered by a **nextId** variable (the main thread has $$Id = 0$$, the first created thread has $$Id = 1$$ and so on). Every created threads are immediately working and their work is only sleeping. To create new thread you simply type in the terminal some positive number **x**. This command will create thread which sleeps for **x** seconds. To interrupt some thread you simply type \

```
-thread_id
```

**Remark**: you can not interrupt the finished thread!

# Note! 

There is a first homework assignment available [here](https://github.com/MrRuper/Concurrent_programming/tree/master/smaller_homework_projects/assignment_1).