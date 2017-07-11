# Twitter Intervals

[![Build Status](https://travis-ci.org/jpzk/twitter-intervals.svg?branch=master)](https://travis-ci.org/jpzk/twitter-intervals)  [![codecov](https://codecov.io/gh/jpzk/twitter-intervals/branch/master/graph/badge.svg)](https://codecov.io/gh/jpzk/twitter-intervals) [![License](http://img.shields.io/:license-Apache%202-grey.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt) [![GitHub stars](https://img.shields.io/github/stars/jpzk/twitter-intervals.svg?style=flat)](https://github.com/jpzk/twitter-intervals/stargazers) 

The missing intervals for the Twitter Util Time library. Feel free to contribute.

    libraryDependencies += "com.madewithtea" %% "twitter-intervals" % "1.0.0-SNAPSHOT" 

## Example
    
    import com.madewithtea.twitterintervals.Interval
    import com.twitter.util.{Time, Duration}

    val TN = Time.now
    val a = Interval(TN, TN + 1.day)
    val b = Interval(TN + 1.hour, TN + 1.day)

    a intersects b shouldEqual true 
    a minus b shouldEqual Interval(TN, TN + 1.hour)
    a.duration shouldEqual 1.day
