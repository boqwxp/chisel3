// See LICENSE for license details.

package chisel3.tester

import scala.util.DynamicVariable

import chisel3._

object Context {
  class Instance(val backend: BackendInterface, val env: TestEnvInterface) {
  }

  private var context = new DynamicVariable[Option[Instance]](None)

  def run[T <: Module](backend: BackendInstance[T], env: TestEnvInterface, testFn: T => Unit) {
    context.withValue(Some(new Instance(backend, env))) {
      backend.run(testFn)
    }
  }

  // TODO: better integration points for default tester selection
  def createDefaultTester[T <: Module](dutGen: => T): BackendInstance[T] = {
    Firrterpreter.start(dutGen)
  }

  def apply(): Instance = context.value.get
}