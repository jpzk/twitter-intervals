/**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
  * this work for additional information regarding copyright ownership.
  * The ASF licenses this file to You under the Apache License, Version 2.0
  * (the "License"); you may not use this file except in compliance with
  * the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.madewithtea.twitterintervals

import org.scalatest.{FlatSpec, Matchers}
import com.twitter.conversions.time._
import com.twitter.util.{Time, Duration}

class IntervalSpec extends FlatSpec with Matchers { 
  
  behavior of "Interval intersects"

  val TN = Time.now 

  it should "raise exception when one of interval not valid with intersection - a" in {
    val a = Interval(TN, TN - 1.day)
    val b = Interval(TN, TN + 1.day)

    an[InvalidInterval] should be thrownBy Interval.intersects(a,b) 
  }

  it should "raise exception when one of interval not valid with intersection - b" in {
    val a = Interval(TN, TN - 1.day)
    val b = Interval(TN, TN + 1.day)

    an[InvalidInterval] should be thrownBy Interval.intersects(b,a) 
  }

  it should "a,b should intersection, when a = b" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 1.day) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should intersect, when b in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 1.hour) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should intersect, when b start in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 2.days) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should intersect, when b end in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN + 1.hour) 

    // should intersect
    Interval.intersects(a,b) shouldEqual true
  }

  it should "a,b should not intersect, when b behind a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 2.days, TN + 3.days) 

    // should not intersect
    Interval.intersects(a,b) shouldEqual false 
  }

  it should "a,b should not intersect, when b before a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN - 1.hour) 

    // should not intersect
    Interval.intersects(a,b) shouldEqual false 
  }

  behavior of "Interval minus"

  it should "return empty set when b does not intersect a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN - 1.hour) 

    Interval.minus(a,b) shouldEqual Set()  
  }

  it should "return intersection when b before overlaps a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN - 1.day, TN + 1.day)

    Interval.minus(a,b) shouldEqual Set(Interval(TN + 1.day, TN + 2.day))
  }

  it should "return intersection when b end overlaps a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN + 1.day, TN + 3.day)

    Interval.minus(a,b) shouldEqual Set(Interval(TN, TN + 1.day))
  }

  it should "return intersections when b inside a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN + 1.hour, TN + 1.day)

    val c = Interval(TN, TN + 1.hour)
    val d = Interval(TN + 1.day, TN + 2.day)

    Interval.minus(a,b) shouldEqual Set(c,d)
  }

  it should "raise exception when one of interval not valid with minus - a" in {
    val a = Interval(TN, TN - 1.day)
    val b = Interval(TN, TN + 1.day)

    an[InvalidInterval] should be thrownBy Interval.minus(a,b) 
  }

  it should "raise exception when one of interval not valid with minus - b" in {
    val a = Interval(TN, TN - 1.day)
    val b = Interval(TN, TN + 1.day)

    an[InvalidInterval] should be thrownBy Interval.minus(b,a) 
  }


}

