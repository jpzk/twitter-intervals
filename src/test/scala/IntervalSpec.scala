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
  import Interval._

  behavior of "Properties"

  val TN = Time.now 
 
  def shouldIntersect(a: Interval, b: Interval, intersect: Boolean) = {
    a intersects b shouldEqual intersect 
    b intersects a shouldEqual intersect
  }
 
  it should "have right durations" in {
    val a = Interval(TN, TN + 1.day)
    a.duration shouldEqual 1.day
  }

  it should "equal other Interval" in {
    val a = Interval(TN, TN + 1.day)
    val b = a 
    a equal b shouldEqual true
    a == b shouldEqual true
  }

  it should "not equal other Interval" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 2.day)
    a equal b shouldEqual false
    a == b shouldEqual false
  }

  it should "have string representation" in {
    val a = Interval(Time.Zero, Time.Zero + 1.day)
    a.toString shouldEqual "1970-01-01 00:00:00 +0000 1970-01-02 00:00:00 +0000"
  }

  behavior of "Union"

  it should "a,b should union" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 2.day)
    a union b shouldEqual Interval(TN, TN + 2.day)
  }

  it should "a,b (not intersecting) union should raise exception" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 2.day, TN + 3.day)
    an[CannotUnionMustIntersect] should be thrownBy (a union b)
  }

  behavior of "Intersection"

  it should "a,b should intersection, when a = b" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 1.day) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 1.hour) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b in a, starts at the same time" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 1.hour) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b in a, ends at the same time" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 1.day) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b start in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 2.days) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b start at a start" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN, TN + 2.days) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b end in a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN + 1.hour) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should intersect, when b end in a end" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN + 1.day) 
    shouldIntersect(a,b,true)
  }

  it should "a,b should not intersect, when b behind a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 2.days, TN + 3.days) 
    shouldIntersect(a,b,false) 
  }

  it should "a,b should not intersect, when b before a" in { 
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN - 1.hour) 
    shouldIntersect(a,b,false) 
  }

  behavior of "Minus"
  
  it should "return a when b does not intersect a" in {
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN - 1.day, TN - 1.hour) 
    a minus b shouldEqual Set(Interval(TN, TN + 1.day))
  }

  it should "return empty set when b = a" in {
    val a = Interval(TN, TN + 1.day)
    val b = a
    a minus b shouldEqual Set()
  }

  it should "return intersection when b before overlaps a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN - 1.day, TN + 1.day)
    a minus b shouldEqual Set(Interval(TN + 1.day, TN + 2.day))
  }

  it should "return intersection when b end overlaps a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN + 1.day, TN + 3.day)
    a minus b shouldEqual Set(Interval(TN, TN + 1.day))
  }

  it should "return intersections when b inside a" in { 
    val a = Interval(TN, TN + 2.day)
    val b = Interval(TN + 1.hour, TN + 1.day)
    val c = Interval(TN, TN + 1.hour)
    val d = Interval(TN + 1.day, TN + 2.day)
    a minus b shouldEqual Set(c,d)
  }

  it should "return empty set when b overlaps a" in {
    val a = Interval(TN + 1.hour, TN + 1.day)
    val b = Interval(TN, TN + 2.day)
    a minus b shouldEqual Set()
  }
}
