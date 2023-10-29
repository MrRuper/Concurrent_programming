# What is this?

This are notes from the first laboratory of concurrent programming.

# What are this notes about?

This notes demonstrate how to create several threads in Java and how to give them some work to do. Here I use an "active waiting" of the processor to end threads and that is quite bad solution (better approach will be given in laboratory_2).

# Short description of the given files
- [SingleThread.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/SingleThread.java) - here the main thread methods are showed basing on the **main** thread (so no additional thread is created).  
- [TwoThreads.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/TwoThreads.java) - here one additional thread with a simple **run** method is created. 
- [TwoWriters.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/TwoWriters.java) - here we create two additional threads which are printing numbers and symbols respectively (while compiling you will see the that numbers and symbols are interwoven with each other and that is ok because we have no control on how processor manage this threads).
- [TwoWritersSynchronized.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/TwoWritersSynchronized.java) - here we use volatile variables to actually synchronized this two threads.
- [ManyThreads.java](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/ManyThreads.java) and [program.cpp](https://github.com/MrRuper/Concurrent_programming/blob/master/all_laboratories/laboratory_1/program.cpp) are linked files - in the first one we create thread which creates another thread and so on. This approach is actually sequential and thus to interrupt it I use **Thread.sleep()** method with some random time. The **program.cpp** file finds the first "unsequanced" thread number.


# How to use files in that folder?
You can run all that files by your own typing:
```
javac file_name.java
```

and after that just run it by:
```
java file_name
```

In the case of **ManyThreads.java** you have to compile it as above and additionally compile **program.cpp**:

```
g++ program.cpp -o program
```

and after that join them typing:

```
java ManyThreads | ./program
```