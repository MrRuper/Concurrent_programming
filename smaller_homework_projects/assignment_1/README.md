# What is this?

This is the first small homework project given in the second laboratory meeting during the Concurrent programming course.

# Short problem description
Basically the question is to sum two integer arrays of the same size concurrently i.e. we have to create some number of threads
and each thread is summing some block independently (in this problem the command was to give to each threads independently baches 
of 10 elements). 

# Note
Creating of one thread is quite expensive and thus there appears a very good question: is this approach faster than a normal 
sequential summing or should we create the less number of threads and give to each of them a bigger work to do? The answer is - 
"yes" this approach is not the fastest one but it shows how to create threads, how to correctly join them, how to deal with exceptions and so on.

# Full problem description

## Implementing Vector Operations with Multithreading

In the `Vector` class, which represents vectors of a specified length, implement the following methods:

### Vector sum(Vector other)
Calculate the sum of the current vector with another vector of the same length.

### int dot(Vector other)
Calculate the dot product of the current vector with another vector.

Both of these calculations should be implemented using multithreading. Delegate the tasks of adding or multiplying vector segments of length 10 to auxiliary threads.

Manage the synchronization and waiting for the auxiliary threads to complete their work using the `join()` method and ensure proper handling of thread interruptions.
