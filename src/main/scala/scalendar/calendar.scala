package com.github.philcali
package scalendar 

import conversions._
import operations.RichSupport

import java.util.Calendar
import Calendar._
import Month._
import Day._


object Pattern {
  def apply(pattern: String) = 
    new java.text.SimpleDateFormat(pattern)
  def unapply(formatter: java.text.SimpleDateFormat) =
    Some(formatter.toPattern)
}

object Scalendar {
  def now = new Scalendar() 

  def apply(millis: Long) = new Scalendar(millis)
 
  def apply(millisecond: Int = 0, second: Int = 0, minute: Int = 0,
            hour: Int = 0, day: Int = 0, month: Int = 0, year: Int = 0) = {
    val start = new Scalendar() 
    val workingYear = if(year <= 0) start.year.value else year
    val workingMonth = if(month <= 0) start.month.value else month
    val workingDay = if(day <= 0) start.day.value else day

    start.year(workingYear)
         .month(workingMonth)
         .day(workingDay)
         .hour(hour)
         .minute(minute)
         .second(second)
         .millisecond(millisecond)
  }

  def dayOfWeek(day: Int) = Day.values.find(_.id == day) match {
    case Some(d) => d.toString
    case None => "Undefined day"
  }
  
  def monthName(month: Int) = Month.values.find(_.id == month) match {
    case Some(mon) => mon.toString
    case None => "Undefined month"
  }

  def daynames = Day.values map(_.toString.substring(0, 3))

  def beginDay(cal: Scalendar) = {
    cal.hour(0).minute(0).second(0).millisecond(0)
  }

  def endDay(cal: Scalendar) = {
    cal.hour(23).minute(59).second(59)
  }

  def beginWeek(cal: Scalendar) = {
    beginDay(cal.inWeek(Sunday))
  }

  def endWeek(cal: Scalendar) = {
    endDay(cal.inWeek(Saturday))
  }
}

object CalendarDayDuration {
  import Scalendar._

  def apply(cal: Scalendar) = {
    beginDay(cal) to endDay(cal)
  }
}

object CalendarWeekDuration {
  import Scalendar._

  def apply(cal: Scalendar) = {
    beginWeek(cal) to endWeek(cal)
  }
}

object CalendarMonthDuration {
  import Scalendar._

  def apply(cal: Scalendar) = {
    val nextMonth = cal.day(1) + (1 month) - (1 day)

    beginWeek(cal.day(1)) to
    endWeek(nextMonth)
  }
}

class Scalendar(now: Long = System.currentTimeMillis) extends Ordered[Scalendar] 
                                                             with RichSupport {  
  import Scalendar._

  protected val javaTime = {
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(now)
    calendar
  }

  def compare(that: Scalendar) = this.time compare that.time

  override def equals(something: Any) = something match {
    case cal: Scalendar => cal.time == this.time
    case millis: Long => millis == time
    case _ => false
  }

  def time = javaTime.getTimeInMillis 

  def copy = new Scalendar(time)

  def +(eval: Evaluated) = {
    val newTime = Calendar.getInstance
    newTime.setTimeInMillis(time)
    newTime.add(eval.field, eval.number)
    
    val diff = newTime.getTimeInMillis - time
    
    new Scalendar(time + diff)
  }

  def -(eval: Evaluated) = this + Evaluated(eval.field, -1 * eval.number)

  def isIn(duration: Duration) = 
    time >= duration.start.time && time <= duration.end.time

  def to(to: Scalendar) = 
    new Duration(time, to.time)

  def to(to: Long) = 
    new Duration(time, to)

  def calendarMonth = CalendarMonthDuration(this) 
  
  def calendarWeek = CalendarWeekDuration(this) 

  def calendarDay = CalendarDayDuration(this) 

  override def toString = Pattern("MM/dd/yyyy HH:mm:ss").format(javaTime.getTime)
}
