using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity.Migrations;
using System.Web;
using GauchoGrub.Models;
using System.Diagnostics;

namespace RatingDigest
{
    /*
     * RatingDigestJob - web job that gets executed on a daily basis.
     */
    public class RatingDigestJob
    {
        /*
         * Web job entry point.
         */
        public static void Main()
        {
            // Execute job
            new RatingDigestJob().Start();
        }

        /*
         * Loops through all available UserRatings incrementing corresponding Ratings
         * (by incrementing specific Ratings within the main loop).
         * Also increments overall rating of corresponding MenuItems.
         * UserRatings table gets cleared in the end.
         */
        public void Start()
        {
            Log("Performing RatingDigest Job...");

            GauchoGrubContext db = new GauchoGrubContext();

            Log("Entering initialization loop...");
            List<UserRating> userRatings = db.UserRatings.ToList();
            List<Rating> ratings = new List<Rating>();
            foreach (UserRating ur in userRatings)
            {
                if (!ratings.Exists(r => r.MenuId == ur.MenuId && r.MenuItemId == ur.MenuItemId))
                {
                    Log("Creating Rating: " + ur.ToString());
                    ratings.Add(new Rating { MenuId = ur.MenuId, MenuItemId = ur.MenuItemId });
                }
            }
            Log("Exiting initialization loop...");


            Log("Entering main loop...");
            foreach (UserRating ur in userRatings)
            {
                Log("Processing UserRating: " + ur.ToString());

                // Update Menu-specific item rating
                Rating rating = ratings.Single(r => r.MenuId == ur.MenuId && r.MenuItemId == ur.MenuItemId);
                rating.TotalRatings += 1;
                rating.PositiveRatings += (ur.PositiveRating) ? 1 : 0;
                Log("Rating updated successfully...");

                // Remove UserRating
                db.UserRatings.Remove(ur);
                Log("UserRating deleted...");
            }
            Log("Exiting main loop...");

            Log("Entering post-processing loop...");
            foreach (Rating rating in ratings)
            {
                AddOrUpdateRating(rating, db);
                Log("Inserted/updated Rating in DB...");

                // Update MenuItem overall rating
                MenuItem item = db.MenuItems.SingleOrDefault(m => m.Id == rating.MenuItemId);
                if (item != null)
                {
                    item.TotalRatings += rating.TotalRatings;
                    item.TotalPositiveRatings += rating.PositiveRatings;
                }
                Log("Updated MenuItem overall rating...");
            }
            Log("Exiting post-processing loop...");

            Log("Saving changes to DB...");
            db.SaveChanges();
            Log("Changes saved successfully...");
            Log("Exiting...");
        }

        // Adds or Updates a Rating
        private void AddOrUpdateRating(Rating rating, GauchoGrubContext db)
        {
            // Try to fetch rating with corresponding MenuId and MenuItemId from the DB
            try
            {
                Rating dbRating = db.Ratings.Single(r => r.MenuId == rating.MenuId && r.MenuItemId == rating.MenuItemId);
                dbRating.TotalRatings += rating.TotalRatings;
                dbRating.PositiveRatings += rating.PositiveRatings;
            }
            catch (Exception)
            {
                db.Ratings.Add(rating);
            }
        }

        // Logs job progress
        private void Log(String message)
        {
            Console.WriteLine(message);
        }
    }
}