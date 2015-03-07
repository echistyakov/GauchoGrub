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

        // GET: api/UserRatings?userId={string}&menuId={id}&menuItemId={id}&positive={bool}
        [ResponseType(typeof(UserRating))]
        public async Task<IHttpActionResult> PostUserRating(string userId, int menuId, int menuItemId, bool positive)
        {
            // Verify that the rating is being submitted during the corresponding RepeatedEvent
            RepeatedEvent re = db.Menus.Single(m => m.Id == menuId).Event;
            TimeSpan now = DateTime.Now.TimeOfDay;
            if (now < re.From && now > re.To)
            {
                return BadRequest("Rating can only be submitted during the event");
            }

            // Add or Update rating
            UserRating ur = null;
            try
            {
                ur = db.UserRatings.Single(r => r.UserId.Equals(userId) && r.MenuId == menuId && r.MenuItemId == menuItemId);
                ur.PositiveRating = positive;
            }
            catch (Exception e)
            {
                ur = new UserRating { UserId = userId, MenuId = menuId, MenuItemId = menuItemId, PositiveRating = positive };
            }

            db.UserRatings.AddOrUpdate(ur);
            db.SaveChanges();

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
    }
}