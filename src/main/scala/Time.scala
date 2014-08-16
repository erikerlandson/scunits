package scunits

package object time {
  import scunits.quantity.Physical._
  
  val second = UnitM[Time.type]("second","s")
  val minute = UnitM[Time.type]("minute","min",60.0)
  val hour = UnitM[Time.type]("hour","hr",3600.0)
}