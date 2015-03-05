using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Quartz;
using Quartz.Impl;
using GauchoGrub.Models;
using System.Diagnostics;

namespace GauchoGrub.Jobs
{
    public class JobScheduler
    {
        public static void Start()
        {
            System.Diagnostics.Trace.WriteLine("Initializing Job Scheduler...");

            IScheduler scheduler = StdSchedulerFactory.GetDefaultScheduler();

            // The Job will run at the end of every RepeatedEvent
            GauchoGrubContext db = new GauchoGrubContext();
            // We only need unique ending times
            HashSet<KeyValuePair<TimeSpan, DayOfWeek>> times = new HashSet<KeyValuePair<TimeSpan, DayOfWeek>>();
            foreach (RepeatedEvent e in db.RepeatedEvents)
            {
                times.Add(new KeyValuePair<TimeSpan, DayOfWeek>(e.To, e.DayOfWeek));
            }
            times.Add(new KeyValuePair<TimeSpan, DayOfWeek>(new TimeSpan(0,15,0), DayOfWeek.Thursday));

            // Create a trigger for every unique time
            foreach (KeyValuePair<TimeSpan, DayOfWeek> p in times)
            {
                ITrigger trigger = buildTrigger(p.Key, p.Value);
                System.Diagnostics.Trace.WriteLine("Added trigger " + trigger.Description);
                // Create RatingDigestJob
                IJobDetail job = JobBuilder.Create<RatingDigestJob>().Build();
                scheduler.ScheduleJob(job, trigger);
            }

            scheduler.Start();
        }

        private static ITrigger buildTrigger(TimeSpan t, DayOfWeek d)
        {
            ITrigger trigger = TriggerBuilder.Create()
                .WithIdentity("RATING_DIGEST_TRIGGER_TIME_" + t.ToString() + "_DAY_" + d.ToString())
                .WithSchedule(CronScheduleBuilder.AtHourAndMinuteOnGivenDaysOfWeek(t.Hours, t.Minutes, d))
                .Build();
            return trigger;
        }
    }
}