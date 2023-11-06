# What is this?

This is the second smaller homework assignment given during the third laboratory meeting of Concurrent programming course. 

# Problem description

For a given matrix **A** you have to sum up all of its rows. The rules to do it are next:

- there is a high price of walking through matrix **A** by its row (you can assume that we have some function which codes our elements in **A** in the way that it is "simply" to decode values by walking through column and its very costly to do the same in the row),
- we have to do it concurrently (of course :) ),
- we are not allow to use **O(size_of_A)** additional memory (this is quite natural assumption).

The simulation of that "high cost" is made by **IntBinaryOperator** function which codes elements of **A** in the way that elements in one column linearly depends on each other when elements in one row are defined by some highly nonlinear function.

## How to solve this problem?

Lets create (unfortunatelly naively) number_of_columns threads and each thread will take care of some column of **A**. Each thread will firstly decode the corresponding value of **A** and secondly will save it to some **additional_array**. To synchronize all threads (i.e. to force them to read one row in the same time) we use the **Cyclic barrier**. Finally when all threads will read "whole row" in one move the barrier will sum all decoded values saved in **additional_array** and will save it to the result array. 