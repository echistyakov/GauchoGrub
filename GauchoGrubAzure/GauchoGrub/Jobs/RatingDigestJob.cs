using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity.Migrations;
using System.Web;
using Quartz;
using GauchoGrub.Models;
using System.Diagnostics;

namespace GauchoGrub.Jobs
{
    public class RatingDigestJob : IJob
    {
        GauchoGrubContext db = new GauchoGrubContext();

        public void Execute(IJobExecutionContext context)
        {
            System.Diagnostics.Trace.WriteLine("Performing RatingDigest Job");
            foreach (UserRating ur in db.UserRatings)
            {
                int increment = (ur.PositiveRating) ? 1 : 0;
                Rating rating = GetOrCreate(ur.MenuId, ur.MenuItemId);
                rating.TotalRatings += 1;
                rating.PositiveRatings += increment;
                db.Ratings.AddOrUpdate(rating);
                db.UserRatings.Remove(ur);
            }
            db.SaveChangesAsync();
        }

        // Either fetches the corresponding Ratig from the Database or creates a new one
        private Rating GetOrCreate(int menuId, int menuItemId)
        {
            Rating rating = null;
            try
            {
                rating = db.Ratings.Single(r => r.MenuId == menuId && r.MenuItemId == menuItemId);
            }
            catch (Exception e)
            {
                rating = new Rating { MenuId = menuId, MenuItemId = menuItemId, PositiveRatings = 0, TotalRatings = 0 };
            }
            return rating;
        }
    }
}