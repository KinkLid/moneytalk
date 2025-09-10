file://<WORKSPACE>/src/main/scala/Main.scala
### java.lang.AssertionError: assertion failed: position not set for nn(<empty>) # -1 of class dotty.tools.dotc.ast.Trees$Apply in <WORKSPACE>/src/main/scala/Main.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 290
uri: file://<WORKSPACE>/src/main/scala/Main.scala
text:
```scala
/**
* Посчитать все последовательности одинаковых символов Ответ выдать в виде Seq[(Char, Int)] (символ и число
* последовательных повторений)
*/
object SameSymbolSequenceCount extends App {

val in = "Sstriings"


def solution(in: Seq[Char]): Seq[(Char, Int)] = {
  in.foldLeft(('', 0)){_.@@}
}
}

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.typer.Typer$.assertPositioned(Typer.scala:72)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3272)
	dotty.tools.dotc.typer.Applications.extMethodApply(Applications.scala:2458)
	dotty.tools.dotc.typer.Applications.extMethodApply$(Applications.scala:380)
	dotty.tools.dotc.typer.Typer.extMethodApply(Typer.scala:116)
	dotty.tools.dotc.typer.Applications.tryApplyingExtensionMethod(Applications.scala:2503)
	dotty.tools.dotc.typer.Applications.tryApplyingExtensionMethod$(Applications.scala:380)
	dotty.tools.dotc.typer.Typer.tryApplyingExtensionMethod(Typer.scala:116)
	dotty.tools.dotc.interactive.Completion$Completer.tryApplyingReceiverToExtension$1(Completion.scala:526)
	dotty.tools.dotc.interactive.Completion$Completer.$anonfun$23(Completion.scala:569)
	scala.collection.immutable.List.flatMap(List.scala:294)
	scala.collection.immutable.List.flatMap(List.scala:79)
	dotty.tools.dotc.interactive.Completion$Completer.extensionCompletions(Completion.scala:566)
	dotty.tools.dotc.interactive.Completion$Completer.selectionCompletions(Completion.scala:446)
	dotty.tools.dotc.interactive.Completion$.computeCompletions(Completion.scala:218)
	dotty.tools.dotc.interactive.Completion$.rawCompletions(Completion.scala:78)
	dotty.tools.pc.completions.Completions.enrichedCompilerCompletions(Completions.scala:114)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:136)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:135)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position not set for nn(<empty>) # -1 of class dotty.tools.dotc.ast.Trees$Apply in <WORKSPACE>/src/main/scala/Main.scala