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
     * DiningCommonsController - controller for the DiningCommon model.
     */
    public class DiningCommonsController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        /*
         * Returns a list of all DiningCommons.
         * GET: api/DiningCommons
         */
        public IQueryable<DiningCommon> GetDiningCommons()
        {
            return db.DiningCommons;
        }

        /*
         * Returns a DiningCommon witht the specified Id.
         * GET: api/DiningCommons/5
         */
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