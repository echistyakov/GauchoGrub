using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity.Migrations;
using System.Web;
using GauchoGrub.Models;
using System.Diagnostics;

namespace RatingDigest
{
    public class RatingDigestJob
    {
        public static void Main()
        {
            // Execute job
            new RatingDigestJob().Start();
        }

        public void Start()
        {
            Debug.WriteLine("Performing RatingDigest Job");

            GauchoGrubContext db = new GauchoGrubContext();
            
            foreach (UserRating ur in db.UserRatings.ToList())
            {
                Debug.WriteLine("Processing UserRating (Id): " + ur.Id);

                // Update Menu-specific item rating
                Rating rating = GetOrCreateRating(ur.MenuId, ur.MenuItemId, db);
                rating.TotalRatings += 1;
                rating.PositiveRatings += (ur.PositiveRating) ? 1 : 0;
                TryAddRating(rating, db);
                Debug.WriteLine("Rating added/updated: " + rating.MenuId + " - MenuId, " + rating.MenuItemId + " - MenuItemId");

                // Update MenuItem total overall rating
                MenuItem item = db.MenuItems.SingleOrDefault(m => m.Id == ur.MenuItemId);
                if (item != null)
                {
                    item.TotalRatings += 1;
                    item.TotalPositiveRatings += (ur.PositiveRating) ? 1 : 0;
                }
                Debug.WriteLine("Updated MenuItem (Id):" + rating.MenuItemId);

                // Remove UserRating
                db.UserRatings.Remove(ur);
                Debug.WriteLine("UserRating deleted (Id): " + ur.Id);
            }
            db.SaveChanges();
        }

        // Either fetches the corresponding Rating from the Database or creates a new one
        private Rating GetOrCreateRating(int menuId, int menuItemId, GauchoGrubContext db)
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

        // Tries adding a rating to DB
        private void TryAddRating(Rating r, GauchoGrubContext db)
        {
            try
            {
                db.Ratings.Add(r);
            }
            catch (Exception e)
            {
                // Do nothing
            }
        }
    }
}