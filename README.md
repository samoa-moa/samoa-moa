samoa-moa
=========

SAMOA plugin that integrates MOA into SAMOA.

This plugin allows to use MOA classifiers and clusterers inside SAMOA.

##Setup

You need to have installed [SAMOA](https://github.com/yahoo/samoa).
Download

- [the new developer version of MOA](https://sourceforge.net/projects/moa-datastream/files/MOA/2013%20August/moa-dev-13-11.jar) | [Source Code](https://code.google.com/r/abifet-moa-dev/)
- [samoa-moa.jar](https://sourceforge.net/projects/moa-datastream/files/MOA/2013%20August/samoa-moa.jar) 

into `samoa/target/`.

The jar for the developer version of MOA is available in the `/lib/` folder of this project.
You can build `samoa-moa.jar` by cloning this project and issuing `mvn package` on the command line.

##Example

To use a MOA classifier you only need to use the SAMOA `SingleClassifier` with the `MOAClassifierAdapter` wrapper. For example to use a MOA HoeffdingTree:

`classifiers.SingleClassifier -l  (MOAClassifierAdapter -l moa.classifiers.trees.HoeffdingTree)`

A complete example to use SAMOA bagging with MOA HoeffdingTree:

```bash
bin/samoa local target/SAMOA-Local-0.0.1-SNAPSHOT.jar:samoa-moa-0.0.1-SNAPSHOT.jar:moa-dev-13-11.jar "PrequentialEvaluation -i 100000 -f 10000 -l (classifiers.ensemble.Bagging -s 10 -l (classifiers.SingleClassifier -l (MOAClassifierAdapter -l moa.classifiers.trees.HoeffdingTree)))"
```

Notice that this example uses the Local mode of SAMOA.

