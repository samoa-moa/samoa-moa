samoa-moa
=========

SAMOA Extension that integrates MOA into SAMOA

With this extension, it is possible to use MOA classifiers and clusterers inside SAMOA.

##Setup

You need to have installed SAMOA. Download 

- [the new developer version of MOA](https://sourceforge.net/projects/moa-datastream/files/MOA/2013%20August/moa-dev-13-11.jar/download) | [Source Code](https://code.google.com/r/abifet-moa-dev/)
- [samoa-moa.jar](https://sourceforge.net/projects/moa-datastream/files/MOA/2013%20August/samoa-moa.jar/download) 

into `samoa/target`.

##Example

To use a MOA classifier you only need to use the SAMOA `SingleClassifier` with the `MOAClassifierAdapter` wrapper. For example to use a MOA HoeffdingTree:

`classifiers.SingleClassifier -l  (MOAClassifierAdapter -l moa.classifiers.trees.HoeffdingTree)`

A complete example to use SAMOA bagging with MOA HoeffdingTree:

```bash
java -cp samoa/target/*.jar com.yahoo.labs.samoa.LocalDoTask "PrequentialEvaluation 
-l (classifiers.ensemble.Bagging -s 10 -l (classifiers.SingleClassifier -l  
(MOAClassifierAdapter -l moa.classifiers.trees.HoeffdingTree)))  -f 100000 -i 1000000"
```

Notice that this example uses the Local Testing mode of SAMOA.

