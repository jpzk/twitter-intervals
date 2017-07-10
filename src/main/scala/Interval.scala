package com.madewithtea.twitterintervals

import com.twitter.util.{Future, Duration, Time}

case class Interval(start: Time, end: Time)

object Interval { 
	def beforeOverlap(a: Interval, b: Interval) = 
		(b.start < a.start) && (b.end > a.start) 

	def inside(a: Interval, b: Interval) = 
		(b.start > a.start) && (a.end > b.end)

	def endOverlap(a: Interval, b: Interval) = 
		(a.start < b.start) && (b.end > a.end) && (b.start < a.end)

	// check if interval b intersects interval a
	def intersects(a: Interval, b: Interval) = (a,b) match {
		case (a,b) if beforeOverlap(a,b) => true
		case (a,b) if inside(a,b) => true
		case (a,b) if endOverlap(a,b) => true 
		case _ => false
	}

	// check if interval b is in interval a 
	def minus(a: Interval, b: Interval): Set[Interval] = (a,b) match {
		case (a,b) if beforeOverlap(a,b) => Set(Interval(b.end, a.end)) 
		case (a,b) if inside(a,b) => Set(Interval(a.start, b.start), Interval(b.end, a.end))
		case (a,b) if endOverlap(a,b) => Set(Interval(a.start, b.start))
		case _ => Set[Interval]()
	}
}


