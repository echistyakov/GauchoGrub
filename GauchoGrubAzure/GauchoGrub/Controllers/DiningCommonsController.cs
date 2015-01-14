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
    public class DiningCommonsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        // GET: api/DiningCommons
        public IQueryable<DiningCommon> GetDiningCommons()
        {
            return db.DiningCommons;
        }

        // GET: api/DiningCommons/5
        [ResponseType(typeof(DiningCommon))]
        public async Task<IHttpActionResult> GetDiningCommon(int id)
        {
            DiningCommon diningCommon = await db.DiningCommons.FindAsync(id);
            if (diningCommon == null)
            {
                return NotFound();
            }

            return Ok(diningCommon);
        }

        // PUT: api/DiningCommons/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutDiningCommon(int id, DiningCommon diningCommon)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != diningCommon.Id)
            {
                return BadRequest();
            }

            db.Entry(diningCommon).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DiningCommonExists(id))
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

        // POST: api/DiningCommons
        [ResponseType(typeof(DiningCommon))]
        public async Task<IHttpActionResult> PostDiningCommon(DiningCommon diningCommon)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.DiningCommons.Add(diningCommon);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = diningCommon.Id }, diningCommon);
        }

        // DELETE: api/DiningCommons/5
        [ResponseType(typeof(DiningCommon))]
        public async Task<IHttpActionResult> DeleteDiningCommon(int id)
        {
            DiningCommon diningCommon = await db.DiningCommons.FindAsync(id);
            if (diningCommon == null)
            {
                return NotFound();
            }

            db.DiningCommons.Remove(diningCommon);
            await db.SaveChangesAsync();

            return Ok(diningCommon);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool DiningCommonExists(int id)
        {
            return db.DiningCommons.Count(e => e.Id == id) > 0;
        }
    }
}