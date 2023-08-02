package utils

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class FSMTest extends AnyFunSpec with Matchers:
  describe("A FSM"){
    it(" add phases connected with action"){
      val fsm = new FSMImpl:
        override type Phase = String
        override type Action = String
      fsm + ("p1", "a1", "p2")
      fsm + ("p2", "a2", "p3")
      val phases = Set("p1", "p2", "p3")
      assert(fsm.phases == phases)
    }

    it(" should not allow to connect two different phases using the same action"){
      val fsm = new FSMImpl:
        override type Phase = String
        override type Action = String

      assertThrows[Exception]{
        fsm + ("p1", "a1", "p2")
        fsm + ("p2", "a2", "p3")
        fsm + ("p1", "a1", "p3")
      }
    }

    it(" when is trigger by an action should go to the next phase if exist"){
      val fsm = new FSMImpl:
        override type Phase = String
        override type Action = String

      fsm + ("p1", "a1", "p2")
      fsm + ("p2", "a2", "p3")
      fsm + ("p3", "a3", "p4")

      assert(fsm.currentPhase == "p1")
      fsm trigger "a1"
      assert(fsm.currentPhase == "p2")
      fsm trigger "a2"
      assert(fsm.currentPhase == "p3")

      assertThrows[Exception] {
        fsm trigger "a5"
      }
    }

    it(" should make available the next permitted actions"){
      val fsm = new FSMImpl:
        override type Phase = String
        override type Action = String

      fsm + ("p1", "a1", "p2")
      fsm + ("p1", "a2", "p3")
      fsm + ("p1", "a3", "p4")

      val actions = Set("a1", "a2", "a3")

      assert(fsm.permittedAction == actions)
    }

  }