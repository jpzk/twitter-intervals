# Twitter Intervals

[![Build Status](https://travis-ci.org/jpzk/twitter-intervals.svg?branch=master)](https://travis-ci.org/jpzk/twitter-intervals)  [![codecov](https://codecov.io/gh/jpzk/twitter-intervals/branch/master/graph/badge.svg)](https://codecov.io/gh/jpzk/twitter-intervals) [![License](http://img.shields.io/:license-Apache%202-grey.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt) [![GitHub stars](https://img.shields.io/github/stars/jpzk/twitter-intervals.svg?style=flat)](https://github.com/jpzk/twitter-intervals/stargazers) 

The missing intervals for the [Twitter Util](https://github.com/twitter/util) library. Currently supports test on intersection, and operations union and minus. Feel free to contribute.

## Install

Available on Maven Central Repositories

    libraryDependencies += "com.madewithtea" %% "twitterintervals" % "0.6.0" 

## Example
 
    import com.twitter.conversions.time._
    import com.twitter.util.{Time, Duration}
    import com.madewithtea.twitterintervals.Interval

    val a = Interval(Time.Zero, Time.Zero + 1.day)
    val b = Interval(Time.Zero + 1.hour, Time.Zero + 1.day)

    // test on intersection
    a intersects b shouldEqual true 

    // union 
    a union b shouldEqual Interval(Time.Zero, Time.Zero + 1.day)

    // returning intersection intervals as Set of intervals
    a minus b shouldEqual Set(Interval(Time.Zero, Time.Zero + 1.hour))

    // can be empty
    a minus a shouldEqual Set()

    // using Twitter Time and Duration (conversions) with Intervals
    a.duration shouldEqual 1.day

    
