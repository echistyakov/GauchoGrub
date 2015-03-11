using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using GauchoGrub.Models;

namespace GauchoGrub.Controllers
{
    /*
     * UserRatingsController - controller for the UserRating model.
     */
    public class UserRatingsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        /*
         * Saves/updates or deletes a UserRating from DB.
         * UserId is a unique identification string that helps prevent rating duplication.
         * MenuId and MenuItemId identify the item being rated.
         * Rating: -1 for negative, 0 for neutral (delete rating), 1 for positive.
         * POST: api/UserRatings?userId={string}&menuId={id}&menuItemId={id}&rating={-1,0,1}
         */
        [ResponseType(typeof(UserRating))]
        public async Task<IHttpActionResult> PostUserRating(string userId, int menuId, int menuItemId, int rating)
        {
            // Delete rating
            if (rating == 0)
            {
                TryDeleteUserRating(userId, menuId, menuItemId);
            }
            // Add or Update rating
            else
            {
                AddOrUpdateUserRating(userId, menuId, menuItemId, rating);
            }
            
            await db.SaveChangesAsync();
            return Ok();
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UserRatingExists(int id)
        {
            return db.UserRatings.Count(e => e.Id == id) > 0;
        }

        /*
         * Private helper - attempts to delete a UserRating from DB.
         */
        private void TryDeleteUserRating(string userId, int menuId, int menuItemId)
        {
            try
            {
                UserRating ur = db.UserRatings.Single(r => r.UserId.Equals(userId) && r.MenuId == menuId && r.MenuItemId == menuItemId);
                db.UserRatings.Remove(ur);
            }
            catch (Exception)
            {
                // Rating was not in the DB, pass silently
            }
        }

        /*
         * Private helper - adds a new UserRating or updates it in DB.
         */
        private void AddOrUpdateUserRating(string userId, int menuId, int menuItemId, int rating)
        {
            bool positive = (rating == 1) ? true : false;
            UserRating ur = null;
            try
            {
                ur = db.UserRatings.Single(r => r.UserId.Equals(userId) && r.MenuId == menuId && r.MenuItemId == menuItemId);
                ur.PositiveRating = positive;
            }
            catch (Exception)
            {
                ur = new UserRating { UserId = userId, MenuId = menuId, MenuItemId = menuItemId, PositiveRating = positive };
            }
            db.UserRatings.AddOrUpdate(ur);
        }
    }
}