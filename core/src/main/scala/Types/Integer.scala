package scunits.types

import scunits._

sealed trait Integer {
  type isZero <: Bool
  type isPos <: Bool
  type isNeg <: Bool
  type succ <: Integer
  
  type add[N <: Integer] <: Integer

  type pred <: Integer
  type sub[N <: Integer] <: Integer
  type neg <: Integer

  type loop[B,F[_ <: B] <: B, Res <: B] <: B
}
sealed trait NonNegInt extends Integer {
  type isNeg = False
  type succ <: PosInt
  type neg <: NonPosInt
}
sealed trait NonPosInt extends Integer {
  type isPos = False
  type pred <: NegInt
  type neg <: NonNegInt
  type loop[B,F[_ <: B] <: B, Res <: B] = Res
}
sealed trait NonZeroInt extends Integer {
  type isZero = False
}
sealed trait NegInt extends NonPosInt with NonZeroInt {
  type isNeg = True
  type succ <: NonPosInt
}
sealed trait PosInt extends NonNegInt with NonZeroInt {
  type isPos = True
  type pred <: NonNegInt
  type loop[B,F[_ <: B] <: B, Res <: B] = pred#loop[B,F,F[Res]]
}

sealed trait SuccInt[P <: NonNegInt] extends PosInt {
  type succ = SuccInt[SuccInt[P]]
  type add[N <: Integer] = P#add[N#succ]
  type pred = P
  type sub[N <: Integer] = P#sub[N#pred]
  type neg = PredInt[P#neg]
}

sealed trait PredInt[S <: NonPosInt] extends NegInt {
  type succ = S
  type add[N <: Integer] = S#add[N#pred]
  type pred = PredInt[PredInt[S]]
  type sub[N <: Integer] = S#sub[N#succ]
  type neg = SuccInt[S#neg]
}

sealed trait _0 extends NonNegInt with NonPosInt {
  type isZero = True
  type succ = SuccInt[_0]
  type add[N <: Integer] = N
  type pred = PredInt[_0]
  type sub[N <: Integer] = N#neg
  type neg = _0
}