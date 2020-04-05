# RecLab

Library that implements the algorithm of collaborative filtering.

Input accepts HashMap where key is user(String) and value is HashMap that contains sample(String) and it's rating(Double).

Project contains 2 demo datasets, first in class "Test", second in file "data/sample" ([movielens](http://grouplens.org/datasets/movielens/)).
  
Library and demo's datasets based on material from the book "Programmings collective intelligence: building smart web 2.0 applications" by Toby Segaran.

Compile:  
```javac ./src/recommendations/*.java -d ./bin/```

Run:  
```java -cp bin recommendations.Test```
