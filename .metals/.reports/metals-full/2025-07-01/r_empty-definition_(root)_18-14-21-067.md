error id: file://<WORKSPACE>/src/main/scala/Main.scala:`<none>`.
file://<WORKSPACE>/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 331
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
    val result = in.foldLeft(Seq.empty[Tuple2[Char, Int]]){cas@@e (seqTuple, char) =>
      val i = if seqTuple.last._1 == char then seqTuple.last._2 + 1
      else 1
      Seq(seqTuple.last._1)
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.