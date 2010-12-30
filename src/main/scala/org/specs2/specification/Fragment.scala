package org.specs2
package specification

import main.Arguments
import execute._
import text._
import text.MarkupString._
import text.Trim._
import control.LazyParameter

/**
 * A Fragment is a piece of a specification. It can be a piece of text, an action or
 * an Example
 */
sealed trait Fragment {
  val linkedTo: Option[SpecificationStructure] = None
  def matches(s: String) = true
}
case class SpecStart(name: SpecName, arguments: Arguments = Arguments()) extends Fragment {
  override def matches(s: String) = name matches s
  override def toString = "SpecStart("+name.name+")"
  def withArgs(args: Arguments) = SpecStart(name, args)
  def withName(n: SpecName) = copy(name = name.overrideWith(n))
  def overrideArgs(args: Arguments) = SpecStart(name, arguments.overrideWith(args))
}
object SpecStart {
  def apply(name: String): SpecStart  = new SpecStart(SpecName(name))
  def apply(name: String, args: Arguments): SpecStart = new SpecStart(SpecName(name), args)
}
case class SpecEnd(name: SpecName) extends Fragment {
  override def matches(s: String) = name matches s
  def withName(n: SpecName) = SpecEnd(n)
}
object SpecEnd {
  def apply(name: String): SpecEnd = new SpecEnd(SpecName(name))
}
case class Text(t: String) extends Fragment {
  override def matches(s: String) = t.matches(s)
}
case class Example private[specification] (desc: MarkupString = NoMarkup(""), body: () => Result) extends Fragment with Executable {
  def execute = body()
  override def matches(s: String) = asString(desc).removeAll("\\n").matches(s)
  override def toString = "Example("+desc+")"
  override def equals(a: Any) = {
    a match {
      case e: Example => desc equals e.desc 
      case _          => false
    }
  }
}
case object Example {
  def apply[T <% Result](desc: String, body: =>T) = new Example(NoMarkup(desc), () => body)
  def apply[T <% Result](markup: MarkupString, body: =>T) = new Example(markup, () => body)
}
case class Step private (action: () => Result) extends Fragment with Executable {
  def execute = action()
  override def toString = "Step"
}
case object Step {
  def apply(action: LazyParameter[Result]) = new Step(() => action.value)
}
case class See(link: HtmlLink) extends Fragment

/**
 * Those standard Fragments are used to format the specification text:
 *  * End() can be used to "reset" the indentation of text
 *  * Br() can be used to insert a newline
 *  * Tab() can be used to increment the indentation level
 *  * Backtab() can be used to decrement the indentation level
 */
private[specs2]
object StandardFragments {
  case class End() extends Fragment
  case class Br() extends Fragment
  case class Tab(n: Int = 1) extends Fragment
  case class Backtab(n: Int = 1) extends Fragment
}

