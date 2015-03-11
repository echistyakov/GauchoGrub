using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * GaiuchoGrubContext - DataBase context.
     */
    public class GauchoGrubContext : DbContext
    {
        public GauchoGrubContext() : base("name=GauchoGrubContext")
        {
            this.Database.Log = s => System.Diagnostics.Debug.WriteLine(s);
        }

        public System.Data.Entity.DbSet<GauchoGrub.Models.DiningCommon> DiningCommons { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.MenuItem> MenuItems { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.Menu> Menus { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.RepeatedEvent> RepeatedEvents { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.Meal> Meals { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.MenuCategory> MenuCategories { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.MenuItemType> MenuItemTypes { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.Rating> Ratings { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.UserRating> UserRatings { get; set; }
    
    }
}
