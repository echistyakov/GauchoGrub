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
     * MenusController - controller for the Menu model.
     */
    public class MenusController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        /*
         * Returns a list of all Menus.
         * GET: api/Menus
         */
        public IQueryable<Menu> GetMenus()
        {
            return db.Menus;
        }

        /*
         * Returns a Menu with the specified Id.
         * GET: api/Menus/5
         */
        [ResponseType(typeof(Menu))]
        public async Task<IHttpActionResult> GetMenu(int id)
        {
            Menu menu = await db.Menus.FindAsync(id);
            if (menu == null)
            {
                return NotFound();
            }
            return Ok(menu);
        }

        /*
         * Returns a list Menu in the specified DiningCommon on the specified Date.
         * GET: api/Menus?diningCommon=Ortega&date=02/18/2015
         */
        [ResponseType(typeof(List<Menu>))]
        public async Task<List<Menu>> GetMenus(string diningCommon, DateTime date)
        {
            int diningCommonId = db.DiningCommons.Single(d => d.Name.ToLower().Equals(diningCommon.ToLower())).Id;
            return db.Menus
                .Where(m => m.Date.Equals(date) && m.Event.DiningCommonId.Equals(diningCommonId))
                .Include(m => m.Event)
                .Include(m => m.Event.Meal)
                .Include(m => m.MenuItems)
                .Include(m => m.MenuItems.Select(p => p.MenuCategory))
                .Include(m => m.MenuItems.Select(p => p.MenuItemType))
                .ToList();
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool MenuExists(int id)
        {
            return db.Menus.Count(e => e.Id == id) > 0;
        }
    }
}