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

import com.twitter.util.{Future, Duration, Time}

object Interval {
  def apply(start: Time, end: Time) = new Interval(start, end)

  case class InvalidInterval() extends Exception(
    "end < start or end == start is not a valid interval")

  case class CannotUnionMustIntersect() extends Exception(
    "intervals are not intersecting")
}

sealed class Interval(val start: Time, val end: Time) {
  import Interval._
  if(start > end) throw InvalidInterval() 

  override def toString = s"$start $end"

  override def equals(that: Any) = that match {
    case that: Interval => equal(that) 
    case _ => false
  }

  def length: Duration = end - start

  def duration: Duration = length

  def intersects(b: Interval) = b match {
    case b if inside(b) || overlap(b) || beforeOverlap(b) || endOverlap(b) || equal(b) => true
    case _ => false
  }
  
  def union(b: Interval): Interval = {
    if(!(this intersects b)) 
      throw CannotUnionMustIntersect()
    val start = if (this.start < b.start) this.start else b.start
    val end = if (this.end > b.end) this.end else b.end
    Interval(start, end)
  }

  def minus(b: Interval): Set[Interval] = b match {
    case b if beforeOverlap(b) => Set(Interval(b.end, this.end)) 
    case b if inside(b) => Set(Interval(this.start, b.start), Interval(b.end, this.end))
    case b if overlap(b) => Set(Interval(b.start, this.start), Interval(this.end, b.end))
    case b if endOverlap(b) => Set(Interval(this.start, b.start))
    case _ => Set[Interval]()
  }

  def beforeOverlap(b: Interval) = 
    (b.start < this.start) && (b.end <= this.end) && (b.end > this.start)
  
  def inside(b: Interval) = 
    (b.start > this.start) && (this.end > b.end) ||
    (b.start >= this.start) && (this.end > b.end) ||
    (b.start > this.start) && (this.end >= b.end)

  def overlap(b: Interval) = 
    (b.start < this.start) && (b.end > this.end)
 
  def equal(b: Interval) = 
    (b.start == this.start) && (this.end == b.end)

  def endOverlap(b: Interval) =
    (this.start <= b.start) && (b.end > this.end) && (b.start < this.end)
}

