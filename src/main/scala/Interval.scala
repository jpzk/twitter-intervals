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

case class Interval(start: Time, end: Time) 

case class InvalidInterval() extends Exception("end < start or end == start is not a valid interval")

object Interval { 
  def beforeOverlap(a: Interval, b: Interval) = 
    (b.start < a.start) && (b.end > a.start) 
  
  def inside(a: Interval, b: Interval) = 
    (b.start > a.start) && (a.end > b.end)
  
  def endOverlap(a: Interval, b: Interval) =
    (a.start < b.start) && (b.end > a.end) && (b.start < a.end)
  
  def isValid[A](a: Interval, b: Interval)(f: => A) = 
    if(a.start > a.end || b.start > b.end) throw InvalidInterval() else f
  
  def equal(a: Interval, b: Interval) = 
    (a.start == b.start && a.end == b.end)
  
  // check if interval b intersects interval a
  def intersects(a: Interval, b: Interval) = isValid(a,b) { (a,b) match {
    case (a,b) if beforeOverlap(a,b) => true
    case (a,b) if inside(a,b) => true
    case (a,b) if endOverlap(a,b) => true 
    case (a,b) if equal(a,b) => true
    case _ => false
  }}
  
  // check if interval b is in interval a 
  def minus(a: Interval, b: Interval): Set[Interval] = isValid(a,b) { (a,b) match {
    case (a,b) if beforeOverlap(a,b) => Set(Interval(b.end, a.end)) 
    case (a,b) if inside(a,b) => Set(Interval(a.start, b.start), Interval(b.end, a.end))
    case (a,b) if endOverlap(a,b) => Set(Interval(a.start, b.start))
    case _ => Set[Interval]()
  }}
}

