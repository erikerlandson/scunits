package scunits.types

import scunits._

trait IList {
  type apply[E <: EList] = convertingDims[E,ENil]
  protected type convertingDims[E <: EList, Res <: EList] <: Box[EList]
}
trait INel extends IList {
  type head <: Box[Integer]
  type tail <: IList
}
trait =:[L <: Box[Integer], R <: IList] extends INel {
  type head = L
  type tail = R

  protected type convertingDims[E <: EList, Res <: EList] = ({
    type recurse[R <: EList] = tail#convertingDims[E#tail, R]
    type set[H <: Integer] = Res#set[H,E#head]
    type apply = E#head#isZero#branch[
      Box[EList],
      recurse[Res], // E#head == _0, do nothing.
      head#mapTo[EList, set]#flatMap[recurse] // E#head != _0, convert if possible, abort if not.
    ]
  })#apply
}

trait INil extends IList {
  protected type convertingDims[E <: EList, Res <: EList] = Full[EList, Res]
}