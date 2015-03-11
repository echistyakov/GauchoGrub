using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
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
     * RepeatedEventsController - controller for the RepeatedEvent model.
     */
    public class RepeatedEventsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        /*
         * Returns a list of all RepeatedEvents.
         * GET: api/RepeatedEvents
         */
        public IQueryable<RepeatedEvent> GetRepeatedEvents()
        {
            return db.RepeatedEvents;
        }

        /*
         * Returns a RepeatedEvent with the specified Id.
         * GET: api/RepeatedEvents/5
         */
        [ResponseType(typeof(RepeatedEvent))]
        public async Task<IHttpActionResult> GetRepeatedEvent(int id)
        {
            RepeatedEvent repeatedEvent = await db.RepeatedEvents.FindAsync(id);
            if (repeatedEvent == null)
            {
                return NotFound();
            }
            return Ok(repeatedEvent);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool RepeatedEventExists(int id)
        {
            return db.RepeatedEvents.Count(e => e.Id == id) > 0;
        }
    }
}