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
    public class MenusController : ApiController
    {
        private GauchoGrubContext db = new GauchoGrubContext();

        // GET: api/Menus
        public IQueryable<Menu> GetMenus()
        {
            return db.Menus;
        }

        // GET: api/Menus/5
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

        // GET: api/Menus?diningCommon=Ortega&date=02/18/2015
        [ResponseType(typeof(List<Menu>))]
        public async Task<List<Menu>> GetMenus(string diningCommon, DateTime date)
        {
            int diningCommonId = db.DiningCommons.Where(d => d.Name.Equals(diningCommon)).First().Id;
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