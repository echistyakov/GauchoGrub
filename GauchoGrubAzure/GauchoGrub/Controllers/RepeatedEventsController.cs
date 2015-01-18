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
    public class RepeatedEventsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        // GET: api/RepeatedEvents
        public IQueryable<RepeatedEvent> GetRepeatedEvents()
        {
            return db.RepeatedEvents;
        }

        // GET: api/RepeatedEvents/5
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

        // PUT: api/RepeatedEvents/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutRepeatedEvent(int id, RepeatedEvent repeatedEvent)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != repeatedEvent.Id)
            {
                return BadRequest();
            }

            db.Entry(repeatedEvent).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!RepeatedEventExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/RepeatedEvents
        [ResponseType(typeof(RepeatedEvent))]
        public async Task<IHttpActionResult> PostRepeatedEvent(RepeatedEvent repeatedEvent)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.RepeatedEvents.Add(repeatedEvent);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = repeatedEvent.Id }, repeatedEvent);
        }

        // DELETE: api/RepeatedEvents/5
        [ResponseType(typeof(RepeatedEvent))]
        public async Task<IHttpActionResult> DeleteRepeatedEvent(int id)
        {
            RepeatedEvent repeatedEvent = await db.RepeatedEvents.FindAsync(id);
            if (repeatedEvent == null)
            {
                return NotFound();
            }

            db.RepeatedEvents.Remove(repeatedEvent);
            await db.SaveChangesAsync();

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