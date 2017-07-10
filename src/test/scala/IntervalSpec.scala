package com.madewithtea.twitterintervals

import org.scalatest.{FlatSpec, Matchers}
import com.twitter.conversions.time._
import com.twitter.util.{Time, Duration}

class IntervalSpec extends FlatSpec with Matchers { 
  behavior of "Interval intersects"

  it should "a,b should intersect, when b in a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now + 1.hour, Time.now + 1.hour) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should intersect, when b start in a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now + 1.hour, Time.now + 2.days) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should intersect, when b end in a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now - 1.day, Time.now + 1.hour) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should not intersect, when b behind a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now + 2.days, Time.now + 3.days) 

    // should not intersect
    Interval.intersects(a,b) shouldEqual false 
  }

  it should "a,b should not intersect, when b before a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now - 1.day, Time.now - 1.hour) 

    // should not intersect
    Interval.intersects(a,b) shouldEqual false 
  }

  behavior of "Interval minus"

  it should "return empty set when b does not intersect a" in { 
    val a = Interval(Time.now, Time.now + 1.day)
    val b = Interval(Time.now - 1.day, Time.now - 1.hour) 

    Interval.minus(a,b) shouldEqual Set()  
  }

  it should "return intersection when b before overlaps a" in { 
    val a = Interval(Time.now, Time.now + 2.day)
    val b = Interval(Time.now - 1.day, Time.now + 1.day)

    Interval.minus(a,b) shouldEqual Set(Interval(Time.now + 1.day, Time.now + 2.day))
  }

  it should "return intersection when b end overlaps a" in { 
    val a = Interval(Time.now, Time.now + 2.day)
    val b = Interval(Time.now + 1.day, Time.now + 3.day)

    Interval.minus(a,b) shouldEqual Set(Interval(Time.now, Time.now + 1.day))
  }

  it should "return intersections when b inside a" in { 
    val a = Interval(Time.now, Time.now + 2.day)
    val b = Interval(Time.now + 1.hour, Time.now + 1.day)

    val c = Interval(Time.now, Time.now + 1.hour)
    val d = Interval(Time.now + 1.day, Time.now + 2.day)

    Interval.minus(a,b) shouldEqual Set(c,d)
  }

}

