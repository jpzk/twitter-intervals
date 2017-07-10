# Twitter Intervals

[![Build Status](https://travis-ci.org/jpzk/twitter-intervals.svg?branch=master)](https://travis-ci.org/jpzk/twitter-intervals)  [![codecov](https://codecov.io/gh/jpzk/twitter-intervals/branch/master/graph/badge.svg)](https://codecov.io/gh/jpzk/twitter-intervals) [![License](http://img.shields.io/:license-Apache%202-grey.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt) [![GitHub stars](https://img.shields.io/github/stars/jpzk/twitter-intervals.svg?style=flat)](https://github.com/jpzk/twitter-intervals/stargazers) 

The missing intervals for the Twitter Util Time library. Feel free to contribute.

## Example

  package com.madewithtea.twitterintervals.Interval

  val intervalA = Interval(Time.now, Time.now + 1.day)
  val intervalB = Interval(Time.now + 1.hour, Time.now + day)
  Interval.minus(intervalA, intervalB)

