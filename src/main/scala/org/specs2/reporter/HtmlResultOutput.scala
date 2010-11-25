package org.specs2
package reporter

import java.io.Writer
import main.Arguments

class HtmlResultOutput(out: Writer) extends ResultOutput {
  def printSuccess(message: String)(implicit args: Arguments) = {
    out.write(message)
  }
  def printError(message: String)(implicit args: Arguments) = {
    out.write(message)
  }
  def printSkipped(message: String)(implicit args: Arguments) = {
    out.write(message)
  }
  def printPending(message: String)(implicit args: Arguments) = {
    out.write(message)
  }
  /** print some text, splitting it on several lines */
  def printMessage(message: String)(implicit args: Arguments) = {
    out.write(message)
  }
  def printLines(message: String)(implicit args: Arguments) = {
    val splitted = message.split("\n")
    if (splitted.size > 1) splitted.foreach(m => printLine(m))
    else printLine(message)
  }
  /** print one line */
  def printLine(message: String)(implicit args: Arguments) = {
    out.write(message + "<br/>")
  }
}
