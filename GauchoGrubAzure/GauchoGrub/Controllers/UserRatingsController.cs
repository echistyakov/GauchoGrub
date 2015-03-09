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
    public class UserRatingsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        // POST: api/UserRatings
        [ResponseType(typeof(UserRating))]
        public async Task<IHttpActionResult> PostUserRating(UserRating userRating)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.UserRatings.Add(userRating);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = userRating.Id }, userRating);
        }

        // GET: api/UserRatings?userId={string}&menuId={id}&menuItemId={id}&rating={-1,0,1}
        [ResponseType(typeof(UserRating))]
        public async Task<IHttpActionResult> PostUserRating(string userId, int menuId, int menuItemId, int rating)
        {
            // Delete rating
            if (rating == 0)
            {
                DeleteUserRating(userId, menuId, menuItemId);
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

        private void DeleteUserRating(string userId, int menuId, int menuItemId)
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